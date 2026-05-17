package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // Para TablaManager — devuelve todos
    public List<Doctor> obtenerTodos() {
        List<Doctor> lista = new ArrayList<>();
        String sql = """
            SELECT d.id_usuario, d.nombre, d.especialidad,
                   d.consultorio, d.activo
            FROM doctores d
            JOIN usuarios u ON d.id_usuario = u.id
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

    // Para el combobox de especialidades al agendar cita
    public List<String> obtenerEspecialidades() {
        List<String> lista = new ArrayList<>();
        String sql = """
            SELECT DISTINCT especialidad
            FROM doctores
            WHERE activo = 1
            ORDER BY especialidad
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

    // Para filtrar doctores por especialidad — NuevaCitaDialog
    public List<Doctor> obtenerPorEspecialidad(String especialidad) {
        List<Doctor> lista = new ArrayList<>();
        String sql = """
            SELECT * FROM doctores
            WHERE especialidad = ? AND activo = 1
            ORDER BY nombre
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

    public Doctor obtenerPorId(String id) {
        String sql = "SELECT * FROM doctores WHERE id_usuario = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getString("id_usuario"));
                d.setNombre(rs.getString("nombre"));
                d.setEspecialidad(rs.getString("especialidad"));
                d.setConsultorio(rs.getString("consultorio"));
                d.setActivo(rs.getInt("activo") == 1);
                return d;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener doctor: " + e.getMessage());
        }
        return null;
    }
}