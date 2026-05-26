package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Disponibilidad;
import pe.edu.uni.centromedico.models.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadDAO {

    public List<Disponibilidad> obtenerPorDoctor(String idDoctor) {
        List<Disponibilidad> lista = new ArrayList<>();
        String sql = """
                SELECT * FROM disponibilidad_doctor
                WHERE id_doctor = ? AND eliminado = 0
                ORDER BY dia_semana, hora_inicio
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idDoctor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Disponibilidad d = new Disponibilidad();
                d.setId(rs.getInt("id"));
                d.setIdDoctor(rs.getString("id_doctor"));
                d.setDiaSemana(rs.getString("dia_semana"));
                d.setHoraInicio(rs.getString("hora_inicio"));
                d.setHoraFin(rs.getString("hora_fin"));
                lista.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener disponibilidades: " + e.getMessage());
        }
        return lista;
    }

    // Verifica si el rango tiene slots con citas pendientes
    public boolean tieneCitasPendientes(String idDoctor, String diaSemana,
            String horaInicio, String horaFin) {
        String sql = """
                SELECT COUNT(*) FROM citas c
                JOIN slots_disponibilidad s ON c.id_slot = s.id
                JOIN disponibilidad_doctor d ON s.id_disponibilidad = d.id
                WHERE d.id_doctor  = ?
                AND d.dia_semana = ?
                AND s.hora_inicio >= ?
                AND s.hora_fin   <= ?
                AND c.estado     = 'PENDIENTE'
                AND d.eliminado  = 0
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idDoctor);
            stmt.setString(2, diaSemana);
            stmt.setString(3, horaInicio);
            stmt.setString(4, horaFin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar citas: " + e.getMessage());
        }
        return true; // ante la duda, bloquear
    }

    // Guarda un nuevo rango y genera sus slots automáticamente
    public boolean guardar(String idDoctor, String diaSemana,
            String horaInicio, String horaFin) {

        // Bloquear si hay citas pendientes en ese día
        if (tieneCitasPendientes(idDoctor, diaSemana, horaInicio, horaFin)) {
            System.err.println("No se puede modificar: hay citas pendientes en ese rango.");
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            // 1. Marcar como eliminados los slots disponibles de ese día
            String sqlEliminarSlots = """
                    UPDATE slots_disponibilidad s
                    JOIN disponibilidad_doctor d ON s.id_disponibilidad = d.id
                    SET s.eliminado = 1
                    WHERE d.id_doctor  = ?
                      AND d.dia_semana = ?
                      AND s.disponible = 1
                      AND s.eliminado  = 0
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarSlots)) {
                stmt.setString(1, idDoctor);
                stmt.setString(2, diaSemana);
                stmt.executeUpdate();
            }

            // 2. Marcar como eliminada la disponibilidad anterior de ese día
            String sqlEliminarDisp = """
                    UPDATE disponibilidad_doctor
                    SET eliminado = 1
                    WHERE id_doctor  = ?
                      AND dia_semana = ?
                      AND eliminado  = 0
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarDisp)) {
                stmt.setString(1, idDoctor);
                stmt.setString(2, diaSemana);
                stmt.executeUpdate();
            }

            // 3. Insertar nueva disponibilidad
            String sqlInsert = """
                    INSERT INTO disponibilidad_doctor
                        (id_doctor, dia_semana, hora_inicio, hora_fin)
                    VALUES (?, ?, ?, ?)
                    """;
            int idDisponibilidad;
            try (PreparedStatement stmt = conn.prepareStatement(
                    sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, idDoctor);
                stmt.setString(2, diaSemana);
                stmt.setString(3, horaInicio);
                stmt.setString(4, horaFin);
                stmt.executeUpdate();
                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();
                idDisponibilidad = keys.getInt(1);
            }

            // 4. Generar e insertar slots de 30 minutos
            List<String[]> slots = generarSlots(horaInicio, horaFin);
            String sqlSlot = """
                    INSERT INTO slots_disponibilidad
                        (id_disponibilidad, id_doctor, dia_semana, hora_inicio, hora_fin)
                    VALUES (?, ?, ?, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlSlot)) {
                for (String[] slot : slots) {
                    stmt.setInt(1, idDisponibilidad);
                    stmt.setString(2, idDoctor);
                    stmt.setString(3, diaSemana);
                    stmt.setString(4, slot[0]);
                    stmt.setString(5, slot[1]);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar disponibilidad: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String[]> generarSlots(String horaInicio, String horaFin) {
        List<String[]> slots = new ArrayList<>();
        int inicio = toMinutos(horaInicio);
        int fin = toMinutos(horaFin);
        while (inicio + 30 <= fin) {
            slots.add(new String[] { toHora(inicio), toHora(inicio + 30) });
            inicio += 30;
        }
        return slots;
    }

    private int toMinutos(String hora) {
        String[] p = hora.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    private String toHora(int minutos) {
        return String.format("%02d:%02d", minutos / 60, minutos % 60);
    }
}
