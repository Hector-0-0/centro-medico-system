package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.AtencionCita;
import pe.edu.uni.centromedico.models.AtencionDiagnostico;

import java.sql.*;
import java.util.List;

public class AtencionDAO {

    // Registra la atención + diagnósticos CIE + marca la cita como ATENDIDA
    // en una transacción. Devuelve el id generado de la atención, o -1 si falla.
    // Obtiene la atención asociada a una cita (para historial/detalle)
    public AtencionCita obtenerPorCitaId(int idCita) {
        String sql = """
                SELECT id, id_cita, diagnostico, comentarios, fecha_atencion
                FROM atencion_cita
                WHERE id_cita = ?
                """;
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
                a.setFechaAtencion(rs.getTimestamp("fecha_atencion") != null
                    ? rs.getTimestamp("fecha_atencion").toLocalDateTime() : null);
                return a;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener atención por cita: " + e.getMessage());
        }
        return null;
    }

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

            // Insertar diagnósticos CIE
            List<AtencionDiagnostico> diagnosticos = atencion.getDiagnosticos();
            if (diagnosticos != null && !diagnosticos.isEmpty()) {
                String sqlDiag = """
                        INSERT INTO atencion_diagnostico
                            (id_atencion, id_cie, observacion)
                        VALUES (?, ?, ?)
                        """;
                try (PreparedStatement stmt = conn.prepareStatement(sqlDiag)) {
                    for (AtencionDiagnostico d : diagnosticos) {
                        stmt.setInt(1, idAtencion);
                        stmt.setInt(2, d.getIdCie());
                        stmt.setString(3, d.getObservacion());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
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
