package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public List<Doctor> obtenerTodos() {
        List<Doctor> lista = new ArrayList<>();
        String sql = """
            SELECT d.id_usuario, d.nombre, d.especialidad_id, d.consultorio,
                   d.activo, e.nombre AS especialidad_nombre
            FROM doctores d
            LEFT JOIN especialidades e ON d.especialidad_id = e.id
            JOIN usuarios u ON d.id_usuario = u.id
            WHERE u.eliminado = 0 AND d.eliminado = 0
            ORDER BY e.nombre, d.nombre
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getString("id_usuario"));
                d.setNombre(rs.getString("nombre"));
                d.setEspecialidadId(rs.getInt("especialidad_id"));
                d.setEspecialidad(rs.getString("especialidad_nombre"));
                d.setConsultorio(rs.getString("consultorio"));
                d.setActivo(rs.getInt("activo") == 1);
                lista.add(d);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener doctores: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerNombresEspecialidad() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM especialidades ORDER BY nombre";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidades: " + e.getMessage());
        }
        return lista;
    }

    public List<Doctor> obtenerPorEspecialidad(String especialidad) {
        List<Doctor> lista = new ArrayList<>();
        String sql = """
            SELECT d.id_usuario, d.nombre, d.especialidad_id, d.consultorio,
                   d.activo, e.nombre AS especialidad_nombre
            FROM doctores d
            LEFT JOIN especialidades e ON d.especialidad_id = e.id
            JOIN usuarios u ON d.id_usuario = u.id
            WHERE e.nombre = ? AND d.activo = 1 AND u.eliminado = 0 AND d.eliminado = 0
            ORDER BY d.nombre
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, especialidad);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getString("id_usuario"));
                d.setNombre(rs.getString("nombre"));
                d.setEspecialidadId(rs.getInt("especialidad_id"));
                d.setEspecialidad(rs.getString("especialidad_nombre"));
                d.setConsultorio(rs.getString("consultorio"));
                d.setActivo(rs.getInt("activo") == 1);
                lista.add(d);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener doctores: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Doctor doctor, String password) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlUsuario = "INSERT INTO usuarios (id, password, rol) VALUES (?, ?, 'DOCTOR')";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, doctor.getId());
                stmt.setString(2, password);
                stmt.executeUpdate();
            }

            String sqlDoc = """
                    INSERT INTO doctores (id_usuario, nombre, especialidad_id, consultorio, activo)
                    VALUES (?, ?, ?, ?, 1)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlDoc)) {
                stmt.setString(1, doctor.getId());
                stmt.setString(2, doctor.getNombre());
                stmt.setInt(3, doctor.getEspecialidadId());
                stmt.setString(4, doctor.getConsultorio());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar doctor: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

    public boolean actualizar(Doctor doc) {
        String sql = """
                UPDATE doctores SET nombre = ?, consultorio = ?
                WHERE id_usuario = ?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doc.getNombre());
            stmt.setString(2, doc.getConsultorio());
            stmt.setString(3, doc.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar doctor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String id) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE usuarios SET eliminado = 1 WHERE id = ?")) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE doctores SET eliminado = 1 WHERE id_usuario = ?")) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar doctor: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }
}
