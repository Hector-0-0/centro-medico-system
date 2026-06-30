package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.RecetaDetalleDTO;
import edu.universidad.centromedico.dto.RecetaPendienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Recetas de farmacia. Replica RecetaDAO del desktop: listar pendientes (con
 * paciente y diagnóstico) y entregar (descuenta stock + marca ENTREGADA).
 */
@Service
@RequiredArgsConstructor
public class RecetaService {

    private final JdbcTemplate jdbc;
    private final MedicamentoService medicamentoService;

    public List<RecetaPendienteDTO> pendientes() {
        return jdbc.query("""
            SELECT r.id AS id_receta,
                   e.nombre AS nombre_estudiante,
                   c.id AS id_cita,
                   COALESCE(
                       (SELECT STRING_AGG(cc.codigo + ' - ' + cc.descripcion, ' | ')
                        FROM atencion_diagnostico ad
                        JOIN codigos_cie cc ON ad.id_cie = cc.id
                        WHERE ad.id_atencion = a.id),
                       a.diagnostico
                   ) AS diagnostico,
                   r.estado
            FROM recetas r
            JOIN atencion_cita a ON r.id_atencion = a.id
            JOIN citas         c ON a.id_cita      = c.id
            JOIN estudiantes   e ON c.id_estudiante = e.id_usuario
            WHERE r.estado = 'PENDIENTE'
            ORDER BY r.id DESC
            """,
            (rs, n) -> new RecetaPendienteDTO(
                rs.getInt("id_receta"), rs.getString("nombre_estudiante"),
                rs.getInt("id_cita"), rs.getString("diagnostico"), rs.getString("estado")));
    }

    public RecetaDetalleDTO detalle(int idReceta) {
        List<Map<String, Object>> filas = jdbc.queryForList("""
            SELECT r.id AS id_receta,
                   e.nombre AS nombre_estudiante,
                   c.id AS id_cita,
                   COALESCE(
                       (SELECT STRING_AGG(cc.codigo + ' - ' + cc.descripcion, ' | ')
                        FROM atencion_diagnostico ad
                        JOIN codigos_cie cc ON ad.id_cie = cc.id
                        WHERE ad.id_atencion = a.id),
                       a.diagnostico
                   ) AS diagnostico,
                   r.estado
            FROM recetas r
            JOIN atencion_cita a ON r.id_atencion = a.id
            JOIN citas         c ON a.id_cita      = c.id
            JOIN estudiantes   e ON c.id_estudiante = e.id_usuario
            WHERE r.id = ?
            """, idReceta);
        if (filas.isEmpty()) {
            throw new RuntimeException("Receta no encontrada");
        }
        Map<String, Object> f = filas.get(0);

        List<RecetaDetalleDTO.Item> items = jdbc.query("""
            SELECT rd.id_medicamento, m.nombre, rd.dosis, rd.duracion, m.stock AS stock_actual
            FROM receta_detalle rd
            JOIN medicamentos m ON rd.id_medicamento = m.id
            WHERE rd.id_receta = ?
            """,
            (rs, n) -> new RecetaDetalleDTO.Item(
                rs.getString("id_medicamento"),
                rs.getString("nombre"),
                rs.getString("dosis"),
                rs.getString("duracion"),
                rs.getInt("stock_actual"),
                Math.max(0, rs.getInt("stock_actual") - 1)),
            idReceta);

        return new RecetaDetalleDTO(
            (int) f.get("id_receta"),
            (String) f.get("nombre_estudiante"),
            (int) f.get("id_cita"),
            (String) f.get("diagnostico"),
            (String) f.get("estado"),
            items);
    }

    /**
     * Entrega la receta: descuenta 1 de stock por medicamento (registrando la
     * salida en la auditoría) y marca ENTREGADA.
     */
    @Transactional
    public void entregar(int idReceta, String usuario) {
        List<Map<String, Object>> filas = jdbc.queryForList(
            "SELECT estado FROM recetas WHERE id = ?", idReceta);
        if (filas.isEmpty()) {
            throw new RuntimeException("La receta no existe");
        }
        if (!"PENDIENTE".equals(filas.get(0).get("estado"))) {
            throw new RuntimeException("La receta ya fue entregada");
        }

        // Un descuento de 1 por medicamento distinto de la receta (con stock).
        List<Map<String, Object>> items = jdbc.queryForList("""
            SELECT DISTINCT m.id AS id_med, m.stock AS stock
            FROM medicamentos m
            INNER JOIN receta_detalle rd ON m.id = rd.id_medicamento
            WHERE rd.id_receta = ?
            """, idReceta);

        for (Map<String, Object> item : items) {
            int stock = ((Number) item.get("stock")).intValue();
            if (stock <= 0) continue; // sin existencias: no se descuenta ni se audita
            String idMed = (String) item.get("id_med");
            int nuevo = stock - 1;
            jdbc.update("UPDATE medicamentos SET stock = ? WHERE id = ?", nuevo, idMed);
            medicamentoService.registrarMovimiento(
                idMed, "SALIDA", 1, nuevo, "Entrega de receta #" + idReceta, usuario);
        }

        jdbc.update("UPDATE recetas SET estado = 'ENTREGADA' WHERE id = ?", idReceta);
    }
}
