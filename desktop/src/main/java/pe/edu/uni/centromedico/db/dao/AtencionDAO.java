package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.AtencionCita;

import java.sql.*;

public class AtencionDAO {

    // Registra la atención + marca la cita como ATENDIDA en una transacción.
    // Devuelve el id generado de la atención, o -1 si falla.
    public int registrar(AtencionCita atencion) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            int idAtencion;
            String sqlAtencion = """
                    INSERT INTO atencion_cita (id_cita, diagnostico, comentarios)
                    VALUES (?, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(
                    sqlAtencion, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, atencion.getIdCita());
                stmt.setString(2, atencion.getDiagnostico());
                stmt.setString(3, atencion.getComentarios());
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("No se generó id de atención");
                    idAtencion = keys.getInt(1);
                }
            }

            String sqlCita = "UPDATE citas SET estado = 'ATENDIDA' WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCita)) {
                stmt.setInt(1, atencion.getIdCita());
                stmt.executeUpdate();
            }

            conn.commit();
            return idAtencion;

        } catch (SQLException e) {
            System.err.println("Error al registrar atención: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return -1;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

}
