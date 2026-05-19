package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Receta;
import pe.edu.uni.centromedico.models.RecetaDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO {

    // Recetas pendientes con datos de estudiante y cita para mostrar en tabla
    public List<Receta> obtenerPendientes() {
        List<Receta> lista = new ArrayList<>();
        String sql = """
                SELECT r.id AS id_receta,
                       e.nombre AS nombre_estudiante,
                       c.id     AS id_cita,
                       a.diagnostico,
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

    // Marcar receta como entregada
    public boolean confirmarEntrega(int idReceta) {
        String sql = "UPDATE recetas SET estado = 'ENTREGADA' WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceta);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al confirmar entrega: " + e.getMessage());
            return false;
        }
    }

    // Insertar nueva receta; devuelve el id generado (o -1 si falla)
    public int registrar(int idAtencion) {
        String sql = "INSERT INTO recetas (id_atencion, estado) VALUES (?, 'PENDIENTE')";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idAtencion);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al registrar receta: " + e.getMessage());
        }
        return -1;
    }

    // Insertar un detalle de receta
    public boolean registrarDetalle(int idReceta, RecetaDetalle detalle) {
        String sql = """
                INSERT INTO receta_detalle
                    (id_receta, id_medicamento, dosis, duracion)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceta);
            stmt.setString(2, detalle.getIdMedicamento());
            stmt.setString(3, detalle.getDosis());
            stmt.setString(4, detalle.getDuracion());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar detalle de receta: " + e.getMessage());
            return false;
        }
    }

    // Detalles de una receta, con JOIN a medicamentos para obtener nombre
    public List<RecetaDetalle> obtenerDetallesPorReceta(int idReceta) {
        List<RecetaDetalle> lista = new ArrayList<>();
        String sql = """
                SELECT rd.id, rd.id_receta, rd.id_medicamento,
                       m.nombre AS nombre_medicamento,
                       rd.dosis, rd.duracion
                FROM receta_detalle rd
                JOIN medicamentos m ON rd.id_medicamento = m.id
                WHERE rd.id_receta = ?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceta);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RecetaDetalle det = new RecetaDetalle();
                det.setId(rs.getInt("id"));
                det.setIdReceta(rs.getInt("id_receta"));
                det.setIdMedicamento(rs.getString("id_medicamento"));
                det.setNombreMedicamento(rs.getString("nombre_medicamento"));
                det.setDosis(rs.getString("dosis"));
                det.setDuracion(rs.getString("duracion"));
                lista.add(det);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de receta: " + e.getMessage());
        }
        return lista;
    }
}
