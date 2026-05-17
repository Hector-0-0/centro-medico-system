package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    // Para TablaManager — devuelve todos
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = """
            SELECT e.id_usuario, e.nombre, e.edad, e.carrera, e.email
            FROM estudiantes e
            JOIN usuarios u ON e.id_usuario = u.id
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

    // Para buscar uno solo — login, perfil
    public Estudiante obtenerPorId(String id) {
        String sql = "SELECT * FROM estudiantes WHERE id_usuario = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Estudiante e = new Estudiante();
                e.setId(rs.getString("id_usuario"));
                e.setNombre(rs.getString("nombre"));
                e.setEdad(rs.getInt("edad"));
                e.setCarrera(rs.getString("carrera"));
                e.setEmail(rs.getString("email"));
                return e;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener estudiante: " + e.getMessage());
        }
        return null;
    }
}
