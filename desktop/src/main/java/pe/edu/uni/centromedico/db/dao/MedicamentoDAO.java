package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Medicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {

    // Para TablaManager
    public List<Medicamento> obtenerTodos() {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicamentos ORDER BY nombre";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setId(rs.getString("id"));
                m.setNombre(rs.getString("nombre"));
                m.setStock(rs.getInt("stock"));
                m.setTipo(rs.getString("tipo"));
                lista.add(m);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
        }
        return lista;
    }

    // Para el combobox al crear receta
    public List<Medicamento> obtenerConStock() {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicamentos WHERE stock > 0 ORDER BY nombre";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setId(rs.getString("id"));
                m.setNombre(rs.getString("nombre"));
                m.setStock(rs.getInt("stock"));
                m.setTipo(rs.getString("tipo"));
                lista.add(m);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarStock(String id, int nuevoStock) {
        String sql = "UPDATE medicamentos SET stock = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevoStock);
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
}