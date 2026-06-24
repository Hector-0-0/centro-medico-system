package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.*;

import java.sql.*;

public class UsuarioDAO {

    public Persona login(String id, String password) {

        // Primero verificamos en la tabla usuarios
        String sqlUsuario = "SELECT * FROM usuarios WHERE id = ? AND password = ? AND eliminado = 0";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {

            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null; // usuario no encontrado o password incorrecto
            }

            String rol = rs.getString("rol");

            // Según el rol, buscamos en la tabla de perfil correspondiente
            switch (rol) {
                case "ESTUDIANTE":
                    return buscarEstudiante(conn, id);
                case "DOCTOR":
                    return buscarDoctor(conn, id);
                case "ADMIN":
                    return buscarAdmin(conn, id);
                case "FARMACIA":
                    return buscarFarmacia(conn, id);
                default:
                    return null;
            }

        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
            return null;
        }
    }

    private Estudiante buscarEstudiante(Connection conn, String id) throws SQLException {
        String sql = """
                SELECT e.id_usuario, e.nombre, e.carrera, e.email, u.fecha_nacimiento
                FROM estudiantes e
                JOIN usuarios u ON e.id_usuario = u.id
                WHERE e.id_usuario = ? AND e.eliminado = 0
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                return null;

            Estudiante e = new Estudiante();
            e.setId(rs.getString("id_usuario"));
            e.setNombre(rs.getString("nombre"));
            e.setCarrera(rs.getString("carrera"));
            e.setEmail(rs.getString("email"));
            Date fn = rs.getDate("fecha_nacimiento");
            if (fn != null) e.setFechaNacimiento(fn.toLocalDate());
            return e;
        }
    }

    private Doctor buscarDoctor(Connection conn, String id) throws SQLException {
        String sql = """
                SELECT d.id_usuario, d.nombre, d.especialidad_id, d.consultorio,
                       d.activo, e.nombre AS especialidad
                FROM doctores d
                LEFT JOIN especialidades e ON d.especialidad_id = e.id
                WHERE d.id_usuario = ? AND d.eliminado = 0
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                return null;

            Doctor d = new Doctor();
            d.setId(rs.getString("id_usuario"));
            d.setNombre(rs.getString("nombre"));
            d.setEspecialidadId(rs.getInt("especialidad_id"));
            d.setEspecialidad(rs.getString("especialidad"));
            d.setConsultorio(rs.getString("consultorio"));
            d.setActivo(rs.getInt("activo") == 1);
            return d;
        }
    }

    private Admin buscarAdmin(Connection conn, String id) throws SQLException {
        String sql = "SELECT * FROM administradores WHERE id_usuario = ? AND eliminado = 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                return null;

            Admin a = new Admin();
            a.setId(rs.getString("id_usuario"));
            a.setNombre(rs.getString("nombre"));
            return a;
        }
    }

    private Farmacia buscarFarmacia(Connection conn, String id) throws SQLException {
        String sql = "SELECT * FROM farmacia_usuarios WHERE id_usuario = ? AND eliminado = 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                return null;

            Farmacia f = new Farmacia();
            f.setId(rs.getString("id_usuario"));
            f.setNombre(rs.getString("nombre"));
            return f;
        }
    }
}
