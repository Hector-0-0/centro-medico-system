package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.AtencionCita;

import java.sql.*;

public class AtencionDAO {

    public boolean registrar(AtencionCita atencion) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            // 1. Insertar la atención
            String sqlAtencion = """
                    INSERT INTO atencion_cita (id_cita, diagnostico, comentarios)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        diagnostico = VALUES(diagnostico),
                        comentarios = VALUES(comentarios)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlAtencion)) {
                stmt.setInt(1, atencion.getIdCita());
                stmt.setString(2, atencion.getDiagnostico());
                stmt.setString(3, atencion.getComentarios());
                stmt.executeUpdate();
            }

            // 2. Marcar la cita como ATENDIDA
            String sqlCita = "UPDATE citas SET estado = 'ATENDIDA' WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCita)) {
                stmt.setInt(1, atencion.getIdCita());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar atención: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

    public AtencionCita obtenerPorCita(int idCita) {
        String sql = "SELECT * FROM atencion_cita WHERE id_cita = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCita);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                AtencionCita a = new AtencionCita();
                a.setId(rs.getInt("id"));
                a.setIdCita(rs.getInt("id_cita"));
                a.setDiagnostico(rs.getString("diagnostico"));
                a.setComentarios(rs.getString("comentarios"));
                return a;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener atención: " + e.getMessage());
        }
        return null;
    }
}
