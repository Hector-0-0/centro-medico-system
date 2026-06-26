package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.AtenderRequest;
import edu.universidad.centromedico.dto.AtencionDetalleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registro de la atención de una cita (consulta médica). Replica
 * AtencionDAO.registrar + RecetaDAO.registrarConDetalles del desktop, todo
 * en una transacción: atención + diagnósticos CIE + marca la cita ATENDIDA +
 * receta opcional.
 */
@Service
@RequiredArgsConstructor
public class AtencionService {

    private final JdbcTemplate jdbc;
    private final N8nNotificacionService n8n;
    private final RecetaPdfService recetaPdf;

    /** Detalle de la atención de una cita del estudiante (diagnósticos + comentarios). */
    public AtencionDetalleDTO detalle(int idCita, String idEstudiante) {
        List<Map<String, Object>> citas = jdbc.queryForList(
            "SELECT id_estudiante FROM citas WHERE id = ? AND eliminado = 0", idCita);
        if (citas.isEmpty() || !idEstudiante.equals(citas.get(0).get("id_estudiante"))) {
            throw new RuntimeException("Cita no encontrada");
        }

        List<Map<String, Object>> at = jdbc.queryForList(
            "SELECT id, comentarios FROM atencion_cita WHERE id_cita = ?", idCita);
        if (at.isEmpty()) {
            return new AtencionDetalleDTO(false, null, List.of());
        }
        int idAtencion = ((Number) at.get(0).get("id")).intValue();
        String comentarios = (String) at.get(0).get("comentarios");

        List<AtencionDetalleDTO.Diagnostico> diagnosticos = jdbc.query("""
            SELECT cc.codigo, cc.descripcion, ad.observacion
            FROM atencion_diagnostico ad
            JOIN codigos_cie cc ON ad.id_cie = cc.id
            WHERE ad.id_atencion = ?
            ORDER BY cc.codigo
            """,
            (rs, n) -> new AtencionDetalleDTO.Diagnostico(
                rs.getString("codigo"), rs.getString("descripcion"), rs.getString("observacion")),
            idAtencion);

        return new AtencionDetalleDTO(true, comentarios, diagnosticos);
    }

    @Transactional
    public void atender(int idCita, String idDoctor, AtenderRequest req) {
        // 1. Validar la cita: existe, es del médico y está PENDIENTE.
        List<Map<String, Object>> filas = jdbc.queryForList(
            "SELECT id_doctor, estado FROM citas WHERE id = ? AND eliminado = 0", idCita);
        if (filas.isEmpty()) {
            throw new RuntimeException("La cita no existe");
        }
        Map<String, Object> cita = filas.get(0);
        if (!idDoctor.equals(cita.get("id_doctor"))) {
            throw new RuntimeException("Esta cita no te pertenece");
        }
        if (!"PENDIENTE".equals(cita.get("estado"))) {
            throw new RuntimeException("Solo se pueden atender citas PENDIENTES");
        }

        // 2. Insertar la atención y recuperar su id.
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO atencion_cita (id_cita, diagnostico, comentarios) VALUES (?, '', ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idCita);
            ps.setString(2, req.getComentarios());
            return ps;
        }, kh);
        int idAtencion = kh.getKey().intValue();

        // 3. Diagnósticos CIE (al menos uno; lo garantiza la validación del DTO).
        jdbc.batchUpdate(
            "INSERT INTO atencion_diagnostico (id_atencion, id_cie, observacion) VALUES (?, ?, ?)",
            req.getDiagnosticos(), req.getDiagnosticos().size(),
            (ps, d) -> {
                ps.setInt(1, idAtencion);
                ps.setInt(2, d.getIdCie());
                ps.setString(3, d.getObservacion());
            });

        // 4. Marcar la cita como ATENDIDA.
        jdbc.update("UPDATE citas SET estado = 'ATENDIDA' WHERE id = ?", idCita);

        // 5. Receta opcional.
        List<AtenderRequest.RecetaItem> receta = req.getReceta();
        if (receta != null && !receta.isEmpty()) {
            KeyHolder rh = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO recetas (id_atencion, estado) VALUES (?, 'PENDIENTE')",
                    Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, idAtencion);
                return ps;
            }, rh);
            int idReceta = rh.getKey().intValue();

            jdbc.batchUpdate(
                "INSERT INTO receta_detalle (id_receta, id_medicamento, dosis, duracion) VALUES (?, ?, ?, ?)",
                receta, receta.size(),
                (ps, d) -> {
                    ps.setInt(1, idReceta);
                    ps.setString(2, d.getIdMedicamento());
                    ps.setString(3, d.getDosis());
                    ps.setString(4, d.getDuracion());
                });

            // 6. Notificar la receta a n8n (envía el email al estudiante).
            //    Se arma el payload ahora (dentro de la tx, lecturas consistentes)
            //    pero el POST se dispara SOLO si la transacción se confirma, para
            //    no enviar correos de recetas que terminen revertidas.
            Map<String, Object> payload =
                construirPayloadReceta(idReceta, idCita, idDoctor, req);
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            n8n.notificarReceta(payload);
                        }
                    });
            } else {
                n8n.notificarReceta(payload);
            }
        }
    }

    /** Construye el payload JSON que recibirá n8n para enviar la receta por correo. */
    private Map<String, Object> construirPayloadReceta(
            int idReceta, int idCita, String idDoctor, AtenderRequest req) {

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("idReceta", idReceta);
        payload.put("idCita", idCita);
        payload.put("fecha", LocalDateTime.now().toString());
        payload.put("comentarios", req.getComentarios());

        // Estudiante (destinatario del correo).
        Map<String, Object> estudiante = jdbc.queryForMap("""
            SELECT e.id_usuario AS id, e.nombre, e.email, e.edad
            FROM citas c
            JOIN estudiantes e ON c.id_estudiante = e.id_usuario
            WHERE c.id = ?
            """, idCita);
        payload.put("estudiante", estudiante);

        // Doctor que emite la receta.
        Map<String, Object> doctor = jdbc.queryForMap(
            "SELECT id_usuario AS id, nombre, especialidad FROM doctores WHERE id_usuario = ?",
            idDoctor);
        payload.put("doctor", doctor);

        // Diagnósticos CIE (código + descripción + observación).
        List<Map<String, Object>> diagnosticos = new ArrayList<>();
        for (AtenderRequest.Diagnostico d : req.getDiagnosticos()) {
            Map<String, Object> cie = jdbc.queryForMap(
                "SELECT codigo, descripcion FROM codigos_cie WHERE id = ?", d.getIdCie());
            Map<String, Object> diag = new LinkedHashMap<>();
            diag.put("codigo", cie.get("codigo"));
            diag.put("descripcion", cie.get("descripcion"));
            diag.put("observacion", d.getObservacion());
            diagnosticos.add(diag);
        }
        payload.put("diagnosticos", diagnosticos);

        // Medicamentos recetados (nombre + dosis + duración).
        List<Map<String, Object>> medicamentos = new ArrayList<>();
        for (AtenderRequest.RecetaItem item : req.getReceta()) {
            Map<String, Object> med = new LinkedHashMap<>();
            Map<String, Object> info = jdbc.queryForMap(
                "SELECT nombre, tipo, dosis_mg FROM medicamentos WHERE id = ?", item.getIdMedicamento());
            med.put("id", item.getIdMedicamento());
            med.put("nombre", info.get("nombre"));
            med.put("tipo", info.get("tipo"));
            med.put("dosisMg", info.get("dosis_mg"));
            med.put("dosis", item.getDosis());
            med.put("duracion", item.getDuracion());
            medicamentos.add(med);
        }
        payload.put("medicamentos", medicamentos);

        // PDF de la receta (Base64) para que n8n lo adjunte al correo.
        payload.put("pdfNombre", "receta-" + String.format("%04d", idReceta) + ".pdf");
        payload.put("pdfBase64", recetaPdf.generarBase64(payload));

        return payload;
    }
}
