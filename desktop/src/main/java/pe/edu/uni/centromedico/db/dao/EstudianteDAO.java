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
            SELECT e.id_usuario, e.nombre, e.edad, e.carrera, e.email
            FROM estudiantes e
            JOIN usuarios u ON e.id_usuario = u.id
            WHERE u.eliminado = 0
            ORDER BY e.nombre
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estudiante e = new Estudiante();
                e.setId(rs.getString("id_usuario"));
                e.setNombre(rs.getString("nombre"));
                e.setEdad(rs.getInt("edad"));
                e.setCarrera(rs.getString("carrera"));
                e.setEmail(rs.getString("email"));
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

            String sqlUsuario = "INSERT INTO usuarios (id, password, rol) VALUES (?, ?, 'ESTUDIANTE')";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, estudiante.getId());
                stmt.setString(2, password);
                stmt.executeUpdate();
            }

            String sqlEst = """
                    INSERT INTO estudiantes (id_usuario, nombre, edad, carrera, email)
                    VALUES (?, ?, ?, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlEst)) {
                stmt.setString(1, estudiante.getId());
                stmt.setString(2, estudiante.getNombre());
                stmt.setInt(3, estudiante.getEdad());
                stmt.setString(4, estudiante.getCarrera());
                stmt.setString(5, estudiante.getEmail());
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

    // Soft delete — preserva historial de citas e integridad referencial
    public boolean eliminar(String id) {
        String sql = "UPDATE usuarios SET eliminado = 1 WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }

}
