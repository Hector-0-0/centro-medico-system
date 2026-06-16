package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.CodigoCie;
import pe.edu.uni.centromedico.models.AtencionDiagnostico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CodigoCieDAO {

    public List<CodigoCie> buscarPorTexto(String texto) {
        List<CodigoCie> lista = new ArrayList<>();
        String sql = """
                SELECT id, codigo, descripcion
                FROM codigos_cie
                WHERE codigo LIKE ? OR descripcion LIKE ?
                ORDER BY codigo
                """;
        String pattern = "%" + texto + "%";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar códigos CIE: " + e.getMessage());
        }
        return lista;
    }

    public List<CodigoCie> listarTodos() {
        List<CodigoCie> lista = new ArrayList<>();
        String sql = "SELECT id, codigo, descripcion FROM codigos_cie ORDER BY codigo";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar códigos CIE: " + e.getMessage());
        }
        return lista;
    }

    public List<AtencionDiagnostico> obtenerPorAtencion(int idAtencion) {
        List<AtencionDiagnostico> lista = new ArrayList<>();
        String sql = """
                SELECT ad.id, ad.id_atencion, ad.id_cie, ad.observacion,
                       cc.codigo, cc.descripcion
                FROM atencion_diagnostico ad
                JOIN codigos_cie cc ON ad.id_cie = cc.id
                WHERE ad.id_atencion = ?
                ORDER BY cc.codigo
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAtencion);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AtencionDiagnostico d = new AtencionDiagnostico();
                d.setId(rs.getInt("id"));
                d.setIdAtencion(rs.getInt("id_atencion"));
                d.setIdCie(rs.getInt("id_cie"));
                d.setObservacion(rs.getString("observacion"));
                d.setCodigoCie(rs.getString("codigo"));
                d.setDescripcionCie(rs.getString("descripcion"));
                lista.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener diagnósticos por atención: " + e.getMessage());
        }
        return lista;
    }

    public CodigoCie obtenerPorId(int id) {
        String sql = "SELECT id, codigo, descripcion FROM codigos_cie WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener código CIE por id: " + e.getMessage());
        }
        return null;
    }

    private CodigoCie mapear(ResultSet rs) throws SQLException {
        return new CodigoCie(
            rs.getInt("id"),
            rs.getString("codigo"),
            rs.getString("descripcion")
        );
    }
}
