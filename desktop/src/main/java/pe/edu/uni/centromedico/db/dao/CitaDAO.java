package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Cita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // Crear cita y marcar slot como ocupado (transacción atómica)
    public boolean crearCita(String idEstudiante, String idDoctor,
                             int idSlot, String motivo) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlCita = """
                    INSERT INTO citas (id_estudiante, id_doctor, id_slot, motivo, estado)
                    VALUES (?, ?, ?, ?, 'PENDIENTE')
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlCita)) {
                stmt.setString(1, idEstudiante);
                stmt.setString(2, idDoctor);
                stmt.setInt(3, idSlot);
                stmt.setString(4, motivo);
                stmt.executeUpdate();
            }

            String sqlSlot = "UPDATE slots_disponibilidad SET disponible = 0 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSlot)) {
                stmt.setInt(1, idSlot);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear cita: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

    // Historial del estudiante (con datos del doctor y slot)
    public List<Cita> obtenerPorEstudiante(String idEstudiante) {
        List<Cita> lista = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_slot,
                       c.motivo, c.estado, c.fecha_creacion,
                       d.nombre       AS nombre_doctor,
                       e.nombre       AS especialidad,
                       s.dia_semana, s.hora_inicio, s.hora_fin
                FROM citas c
                JOIN doctores              d ON c.id_doctor = d.id_usuario
                LEFT JOIN especialidades   e ON d.especialidad_id = e.id
                JOIN slots_disponibilidad  s ON c.id_slot   = s.id
                WHERE c.id_estudiante = ? AND c.eliminado = 0
                ORDER BY c.id DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEstudiante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs, true, false));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas del estudiante: " + e.getMessage());
        }
        return lista;
    }

    // Todas las citas del sistema (Admin)
    public List<Cita> obtenerTodas() {
        List<Cita> lista = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_slot,
                       c.motivo, c.estado, c.fecha_creacion,
                       est.nombre AS nombre_estudiante,
                       d.nombre   AS nombre_doctor,
                       e.nombre   AS especialidad,
                       s.dia_semana, s.hora_inicio, s.hora_fin
                FROM citas c
                JOIN estudiantes           est ON c.id_estudiante = est.id_usuario
                JOIN doctores              d   ON c.id_doctor     = d.id_usuario
                LEFT JOIN especialidades   e   ON d.especialidad_id = e.id
                JOIN slots_disponibilidad  s   ON c.id_slot       = s.id
                WHERE c.eliminado = 0
                ORDER BY c.id DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs, true, true));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las citas: " + e.getMessage());
        }
        return lista;
    }

    // Citas de un doctor (cualquier estado)
    public List<Cita> obtenerPorDoctor(String idDoctor) {
        List<Cita> lista = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_slot,
                       c.motivo, c.estado, c.fecha_creacion,
                       e.nombre AS nombre_estudiante,
                       s.dia_semana, s.hora_inicio, s.hora_fin
                FROM citas c
                JOIN estudiantes           e ON c.id_estudiante = e.id_usuario
                JOIN slots_disponibilidad  s ON c.id_slot       = s.id
                WHERE c.id_doctor = ? AND c.eliminado = 0
                ORDER BY c.id DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idDoctor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs, false, true));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas del doctor: " + e.getMessage());
        }
        return lista;
    }

    // Mapeo único — flags indican qué columnas trae el ResultSet
    private Cita mapear(ResultSet rs, boolean incluyeDoctor,
                        boolean incluyeEstudiante) throws SQLException {
        Cita c = new Cita();
        c.setId(rs.getInt("id"));
        c.setIdEstudiante(rs.getString("id_estudiante"));
        c.setIdDoctor(rs.getString("id_doctor"));
        c.setIdSlot(rs.getInt("id_slot"));
        c.setMotivo(rs.getString("motivo"));
        c.setEstado(rs.getString("estado"));
        c.setFechaCreacion(rs.getString("fecha_creacion"));
        c.setDiaSemana(rs.getString("dia_semana"));
        c.setHoraInicio(rs.getString("hora_inicio"));
        c.setHoraFin(rs.getString("hora_fin"));
        if (incluyeDoctor) {
            c.setNombreDoctor(rs.getString("nombre_doctor"));
            c.setEspecialidad(rs.getString("especialidad"));
        }
        if (incluyeEstudiante) {
            c.setNombreEstudiante(rs.getString("nombre_estudiante"));
        }
        return c;
    }

    public boolean cancelarCita(int idCita) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            // Obtener id_slot de la cita
            int idSlot;
            try (PreparedStatement st = conn.prepareStatement(
                    "SELECT id_slot FROM citas WHERE id = ? AND eliminado = 0")) {
                st.setInt(1, idCita);
                ResultSet rs = st.executeQuery();
                if (!rs.next()) return false;
                idSlot = rs.getInt("id_slot");
            }

            try (PreparedStatement st = conn.prepareStatement(
                    "UPDATE citas SET estado = 'CANCELADA' WHERE id = ? AND estado = 'PENDIENTE'")) {
                st.setInt(1, idCita);
                if (st.executeUpdate() == 0) return false;
            }

            try (PreparedStatement st = conn.prepareStatement(
                    "UPDATE slots_disponibilidad SET disponible = 1 WHERE id = ?")) {
                st.setInt(1, idSlot);
                st.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al cancelar cita: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }
}
