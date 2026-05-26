package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Cita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // Crear nueva cita y marcar horario como no disponible
    public boolean crearCita(String idEstudiante, String idDoctor,
            int idSlot, String motivo) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // transacción — las dos operaciones deben ocurrir juntas

            // 1. Insertar la cita
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

            // 2. Marcar el slot como no disponible
            String sqlSlot = "UPDATE slots_disponibilidad SET disponible = 0 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSlot)) {
                stmt.setInt(1, idSlot);
                stmt.executeUpdate();
            }

            conn.commit(); // confirmar ambas operaciones
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear cita: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback(); // si algo falla, deshacer todo
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true); // restaurar comportamiento normal
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    // Citas de un estudiante — para su historial
    public List<Cita> obtenerPorEstudiante(String idEstudiante) {
        List<Cita> lista = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_slot,
                       c.motivo, c.estado,
                       d.nombre       AS nombre_doctor,
                       d.especialidad,
                       s.dia_semana, s.hora_inicio, s.hora_fin
                FROM citas c
                JOIN doctores              d ON c.id_doctor = d.id_usuario
                JOIN slots_disponibilidad  s ON c.id_slot   = s.id
                WHERE c.id_estudiante = ? AND c.eliminado = 0
                ORDER BY c.id DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEstudiante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cita c = mapear(rs);
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas: " + e.getMessage());
        }
        return lista;
    }

    // Todas las citas del sistema (para el Admin)
    public List<Cita> obtenerTodas() {
        List<Cita> lista = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_slot,
                       c.motivo, c.estado,
                       e.nombre AS nombre_estudiante,
                       d.nombre AS nombre_doctor,
                       d.especialidad,
                       s.dia_semana, s.hora_inicio, s.hora_fin
                FROM citas c
                JOIN estudiantes           e ON c.id_estudiante = e.id_usuario
                JOIN doctores              d ON c.id_doctor     = d.id_usuario
                JOIN slots_disponibilidad  s ON c.id_slot       = s.id
                WHERE c.eliminado = 0
                ORDER BY c.id DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cita c = mapear(rs);
                c.setNombreEstudiante(rs.getString("nombre_estudiante"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las citas: " + e.getMessage());
        }
        return lista;
    }

    // Todas las citas de un doctor (cualquier estado)
    public List<Cita> obtenerPorDoctor(String idDoctor) {
        List<Cita> lista = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_slot,
                       c.motivo, c.estado,
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
                Cita c = mapear(rs);
                c.setNombreEstudiante(rs.getString("nombre_estudiante"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas del doctor: " + e.getMessage());
        }
        return lista;
    }

    // Método privado reutilizable — mapea ResultSet a Cita
    private Cita mapear(ResultSet rs) throws SQLException {
        Cita c = new Cita();
        c.setId(rs.getInt("id"));
        c.setIdEstudiante(rs.getString("id_estudiante"));
        c.setIdDoctor(rs.getString("id_doctor"));
        c.setIdSlot(rs.getInt("id_slot")); // ← idSlot
        c.setMotivo(rs.getString("motivo"));
        c.setEstado(rs.getString("estado"));
        c.setNombreDoctor(rs.getString("nombre_doctor"));
        c.setEspecialidad(rs.getString("especialidad"));
        c.setDiaSemana(rs.getString("dia_semana"));
        c.setHoraInicio(rs.getString("hora_inicio"));
        c.setHoraFin(rs.getString("hora_fin"));
        return c;
    }
}
