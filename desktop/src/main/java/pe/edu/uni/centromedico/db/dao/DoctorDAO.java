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
            SELECT d.id_usuario, d.nombre, d.especialidad,
                   d.consultorio, d.activo
            FROM doctores d
            JOIN usuarios u ON d.id_usuario = u.id
            WHERE u.eliminado = 0
            ORDER BY d.especialidad, d.nombre
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getString("id_usuario"));
                d.setNombre(rs.getString("nombre"));
                d.setEspecialidad(rs.getString("especialidad"));
                d.setConsultorio(rs.getString("consultorio"));
                d.setActivo(rs.getInt("activo") == 1);
                lista.add(d);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener doctores: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerEspecialidades() {
        List<String> lista = new ArrayList<>();
        String sql = """
            SELECT DISTINCT d.especialidad
            FROM doctores d
            JOIN usuarios u ON d.id_usuario = u.id
            WHERE d.activo = 1 AND u.eliminado = 0
            ORDER BY d.especialidad
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(rs.getString("especialidad"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener especialidades: " + e.getMessage());
        }
        return lista;
    }

    public List<Doctor> obtenerPorEspecialidad(String especialidad) {
        List<Doctor> lista = new ArrayList<>();
        String sql = """
            SELECT d.* FROM doctores d
            JOIN usuarios u ON d.id_usuario = u.id
            WHERE d.especialidad = ? AND d.activo = 1 AND u.eliminado = 0
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
                d.setEspecialidad(rs.getString("especialidad"));
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
                    INSERT INTO doctores (id_usuario, nombre, especialidad, consultorio, activo)
                    VALUES (?, ?, ?, ?, 1)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlDoc)) {
                stmt.setString(1, doctor.getId());
                stmt.setString(2, doctor.getNombre());
                stmt.setString(3, doctor.getEspecialidad());
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

    // Soft delete — preserva historial de citas e integridad referencial
    public boolean eliminar(String id) {
        String sql = "UPDATE usuarios SET eliminado = 1 WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar doctor: " + e.getMessage());
            return false;
        }
    }

}