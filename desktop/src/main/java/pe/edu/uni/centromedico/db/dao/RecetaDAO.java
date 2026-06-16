package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Receta;
import pe.edu.uni.centromedico.models.RecetaDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO {

    // Recetas pendientes con datos de estudiante y cita para mostrar en tabla
    // Muestra códigos CIE concatenados, o el texto antiguo si no hay CIE.
    public List<Receta> obtenerPendientes() {
        List<Receta> lista = new ArrayList<>();
        String sql = """
                SELECT r.id AS id_receta,
                       e.nombre AS nombre_estudiante,
                       c.id     AS id_cita,
                       COALESCE(
                           (SELECT STRING_AGG(cc.codigo + ' - ' + cc.descripcion, ' | ')
                            FROM atencion_diagnostico ad
                            JOIN codigos_cie cc ON ad.id_cie = cc.id
                            WHERE ad.id_atencion = a.id),
                           a.diagnostico
                       ) AS diagnostico,
                       r.estado,
                       r.id_atencion
                FROM recetas r
                JOIN atencion_cita a ON r.id_atencion = a.id
                JOIN citas         c ON a.id_cita      = c.id
                JOIN estudiantes   e ON c.id_estudiante = e.id_usuario
                WHERE r.estado = 'PENDIENTE'
                ORDER BY r.id DESC
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Receta receta = new Receta();
                receta.setId(rs.getInt("id_receta"));
                receta.setIdAtencion(rs.getInt("id_atencion"));
                receta.setEstado(rs.getString("estado"));
                receta.setNombrePaciente(rs.getString("nombre_estudiante"));
                receta.setDiagnostico(rs.getString("diagnostico"));
                receta.setIdCita(rs.getInt("id_cita"));
                lista.add(receta);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener recetas pendientes: " + e.getMessage());
        }
        return lista;
    }

    // Crea receta + detalles en una sola transacción atómica
    public boolean registrarConDetalles(int idAtencion, List<RecetaDetalle> detalles) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            int idReceta;
            String sqlReceta = "INSERT INTO recetas (id_atencion, estado) VALUES (?, 'PENDIENTE')";
            try (PreparedStatement stmt = conn.prepareStatement(sqlReceta, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, idAtencion);
                stmt.executeUpdate();
                ResultSet keys = stmt.getGeneratedKeys();
                if (!keys.next()) throw new SQLException("No se generó id de receta");
                idReceta = keys.getInt(1);
            }

            String sqlDetalle = """
                    INSERT INTO receta_detalle
                        (id_receta, id_medicamento, dosis, duracion)
                    VALUES (?, ?, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalle)) {
                for (RecetaDetalle d : detalles) {
                    stmt.setInt(1, idReceta);
                    stmt.setString(2, d.getIdMedicamento());
                    stmt.setString(3, d.getDosis());
                    stmt.setString(4, d.getDuracion());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar receta con detalles: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

    // Marca receta como entregada y descuenta stock — atómico
    public boolean entregarYDescontarStock(int idReceta) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            // 1. Descontar stock de cada medicamento de la receta (sintaxis SQL Server)
            String sqlDescontar = """
                    UPDATE m SET m.stock = m.stock - 1
                    FROM medicamentos m
                    INNER JOIN receta_detalle rd ON m.id = rd.id_medicamento
                    WHERE rd.id_receta = ? AND m.stock > 0
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlDescontar)) {
                stmt.setInt(1, idReceta);
                stmt.executeUpdate();
            }

            // 2. Marcar receta como entregada
            String sqlEstado = "UPDATE recetas SET estado = 'ENTREGADA' WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEstado)) {
                stmt.setInt(1, idReceta);
                if (stmt.executeUpdate() == 0) throw new SQLException("Receta no encontrada");
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al entregar receta: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

}
