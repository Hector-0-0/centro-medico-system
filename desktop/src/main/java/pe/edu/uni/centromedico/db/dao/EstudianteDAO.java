package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = """
            SELECT e.id_usuario, e.nombre, e.carrera, e.email, u.fecha_nacimiento
            FROM estudiantes e
            JOIN usuarios u ON e.id_usuario = u.id
            WHERE u.eliminado = 0 AND e.eliminado = 0
            ORDER BY e.nombre
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estudiante e = new Estudiante();
                e.setId(rs.getString("id_usuario"));
                e.setNombre(rs.getString("nombre"));
                e.setCarrera(rs.getString("carrera"));
                e.setEmail(rs.getString("email"));
                Date fn = rs.getDate("fecha_nacimiento");
                if (fn != null) e.setFechaNacimiento(fn.toLocalDate());
                lista.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener estudiantes: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Estudiante estudiante, String password) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlUsuario = """
                    INSERT INTO usuarios (id, password, rol, fecha_nacimiento)
                    VALUES (?, ?, 'ESTUDIANTE', ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, estudiante.getId());
                stmt.setString(2, password);
                if (estudiante.getFechaNacimiento() != null) {
                    stmt.setDate(3, java.sql.Date.valueOf(estudiante.getFechaNacimiento()));
                } else {
                    stmt.setNull(3, java.sql.Types.DATE);
                }
                stmt.executeUpdate();
            }

            String sqlEst = """
                    INSERT INTO estudiantes (id_usuario, nombre, carrera, email)
                    VALUES (?, ?, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlEst)) {
                stmt.setString(1, estudiante.getId());
                stmt.setString(2, estudiante.getNombre());
                stmt.setString(3, estudiante.getCarrera());
                stmt.setString(4, estudiante.getEmail());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar estudiante: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

    public boolean actualizar(Estudiante est) {
        String sql = """
                UPDATE estudiantes SET nombre = ?, carrera = ?, email = ?
                WHERE id_usuario = ?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, est.getNombre());
            stmt.setString(2, est.getCarrera());
            stmt.setString(3, est.getEmail());
            stmt.setString(4, est.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estudiante: " + e.getMessage());
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
                    "UPDATE estudiantes SET eliminado = 1 WHERE id_usuario = ?")) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }
}
