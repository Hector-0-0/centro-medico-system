package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Especialidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {

    public List<Especialidad> obtenerTodas() {
        List<Especialidad> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM especialidades ORDER BY nombre";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidades: " + e.getMessage());
        }
        return lista;
    }

    public Especialidad obtenerPorId(int id) {
        String sql = "SELECT id, nombre FROM especialidades WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidad: " + e.getMessage());
        }
        return null;
    }

    public Especialidad obtenerPorNombre(String nombre) {
        String sql = "SELECT id, nombre FROM especialidades WHERE nombre = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar especialidad: " + e.getMessage());
        }
        return null;
    }

    private Especialidad mapear(ResultSet rs) throws SQLException {
        return new Especialidad(rs.getInt("id"), rs.getString("nombre"));
    }
}
