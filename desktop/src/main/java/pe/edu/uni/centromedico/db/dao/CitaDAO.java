package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Cita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // Crear nueva cita y marcar horario como no disponible
    public boolean crearCita(String idEstudiante, String idDoctor,
            int idHorario, String motivo) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // transacción — las dos operaciones deben ocurrir juntas

            // 1. Insertar la cita
            String sqlCita = """
                    INSERT INTO citas (id_estudiante, id_doctor, id_horario, motivo, estado)
                    VALUES (?, ?, ?, ?, 'PENDIENTE')
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlCita)) {
                stmt.setString(1, idEstudiante);
                stmt.setString(2, idDoctor);
                stmt.setInt(3, idHorario);
                stmt.setString(4, motivo);
                stmt.executeUpdate();
            }

            // 2. Marcar el horario como no disponible
            String sqlHorario = "UPDATE horarios_doctor SET disponible = 0 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlHorario)) {
                stmt.setInt(1, idHorario);
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
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_horario,
                       c.motivo, c.estado,
                       d.nombre      AS nombre_doctor,
                       d.especialidad,
                       h.dia_semana, h.hora_inicio, h.hora_fin
                FROM citas c
                JOIN doctores        d ON c.id_doctor  = d.id_usuario
                JOIN horarios_doctor h ON c.id_horario = h.id
                WHERE c.id_estudiante = ?
                ORDER BY c.id DESC
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idEstudiante);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cita c = new Cita();
                c.setId(rs.getInt("id"));
                c.setIdEstudiante(rs.getString("id_estudiante"));
                c.setIdDoctor(rs.getString("id_doctor"));
                c.setIdHorario(rs.getInt("id_horario"));
                c.setMotivo(rs.getString("motivo"));
                c.setEstado(rs.getString("estado"));
                c.setNombreDoctor(rs.getString("nombre_doctor"));
                c.setEspecialidad(rs.getString("especialidad"));
                c.setDiaSemana(rs.getString("dia_semana"));
                c.setHoraInicio(rs.getString("hora_inicio"));
                c.setHoraFin(rs.getString("hora_fin"));
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
                SELECT c.id, c.id_estudiante, c.id_doctor,
                       c.id_horario, c.motivo, c.estado,
                       e.nombre  AS nombre_estudiante,
                       d.nombre  AS nombre_doctor,
                       d.especialidad,
                       h.dia_semana, h.hora_inicio, h.hora_fin
                FROM citas c
                JOIN estudiantes    e ON c.id_estudiante = e.id_usuario
                JOIN doctores       d ON c.id_doctor     = d.id_usuario
                JOIN horarios_doctor h ON c.id_horario   = h.id
                ORDER BY c.id DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cita c = new Cita();
                c.setId(rs.getInt("id"));
                c.setIdEstudiante(rs.getString("id_estudiante"));
                c.setIdDoctor(rs.getString("id_doctor"));
                c.setIdHorario(rs.getInt("id_horario"));
                c.setMotivo(rs.getString("motivo"));
                c.setEstado(rs.getString("estado"));
                c.setNombreEstudiante(rs.getString("nombre_estudiante"));
                c.setNombreDoctor(rs.getString("nombre_doctor"));
                c.setEspecialidad(rs.getString("especialidad"));
                c.setDiaSemana(rs.getString("dia_semana"));
                c.setHoraInicio(rs.getString("hora_inicio"));
                c.setHoraFin(rs.getString("hora_fin"));
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
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_horario,
                       c.motivo, c.estado,
                       e.nombre AS nombre_estudiante,
                       h.dia_semana, h.hora_inicio, h.hora_fin
                FROM citas c
                JOIN estudiantes e ON c.id_estudiante = e.id_usuario
                JOIN horarios_doctor h ON c.id_horario = h.id
                WHERE c.id_doctor = ?
                ORDER BY c.id DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idDoctor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cita c = new Cita();
                c.setId(rs.getInt("id"));
                c.setIdEstudiante(rs.getString("id_estudiante"));
                c.setIdDoctor(rs.getString("id_doctor"));
                c.setIdHorario(rs.getInt("id_horario"));
                c.setMotivo(rs.getString("motivo"));
                c.setEstado(rs.getString("estado"));
                c.setNombreEstudiante(rs.getString("nombre_estudiante"));
                c.setDiaSemana(rs.getString("dia_semana"));
                c.setHoraInicio(rs.getString("hora_inicio"));
                c.setHoraFin(rs.getString("hora_fin"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas del doctor: " + e.getMessage());
        }
        return lista;
    }

    // Citas pendientes de un doctor
    public List<Cita> obtenerPendientesPorDoctor(String idDoctor) {
        List<Cita> lista = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_estudiante, c.id_doctor, c.id_horario,
                       c.motivo, c.estado,
                       e.nombre AS nombre_estudiante,
                       h.dia_semana, h.hora_inicio, h.hora_fin
                FROM citas c
                JOIN estudiantes e ON c.id_estudiante = e.id_usuario
                JOIN horarios_doctor h ON c.id_horario = h.id
                WHERE c.id_doctor = ? AND c.estado = 'PENDIENTE'
                ORDER BY c.id DESC
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idDoctor);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cita c = new Cita();
                c.setId(rs.getInt("id"));
                c.setIdEstudiante(rs.getString("id_estudiante"));
                c.setIdDoctor(rs.getString("id_doctor"));
                c.setIdHorario(rs.getInt("id_horario"));
                c.setMotivo(rs.getString("motivo"));
                c.setEstado(rs.getString("estado"));
                // campos extra para mostrar en tabla
                c.setNombreEstudiante(rs.getString("nombre_estudiante"));
                c.setDiaSemana(rs.getString("dia_semana"));
                c.setHoraInicio(rs.getString("hora_inicio"));
                c.setHoraFin(rs.getString("hora_fin"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener citas: " + e.getMessage());
        }
        return lista;
    }
}
