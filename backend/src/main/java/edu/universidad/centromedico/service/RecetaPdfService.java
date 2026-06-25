package edu.universidad.centromedico.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Genera el PDF de la receta médica a partir del payload de la atención.
 * Usa openhtmltopdf (HTML/CSS -> PDF) para un documento con la identidad
 * visual de la UNI (guinda + crema), listo para adjuntar al correo.
 */
@Service
public class RecetaPdfService {

    private static final DateTimeFormatter FECHA_FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Devuelve el PDF de la receta codificado en Base64 (para enviarlo en JSON). */
    public String generarBase64(Map<String, Object> payload) {
        byte[] pdf = generar(payload);
        return Base64.getEncoder().encodeToString(pdf);
    }

    @SuppressWarnings("unchecked")
    public byte[] generar(Map<String, Object> payload) {
        Map<String, Object> est = (Map<String, Object>) payload.getOrDefault("estudiante", Map.of());
        Map<String, Object> doc = (Map<String, Object>) payload.getOrDefault("doctor", Map.of());
        List<Map<String, Object>> diags =
            (List<Map<String, Object>>) payload.getOrDefault("diagnosticos", List.of());
        List<Map<String, Object>> meds =
            (List<Map<String, Object>>) payload.getOrDefault("medicamentos", List.of());

        String nReceta = String.format("%04d", asInt(payload.get("idReceta")));
        String nCita = String.format("%04d", asInt(payload.get("idCita")));
        String fecha = fecha(payload.get("fecha"));
        String comentarios = str(payload.get("comentarios"));

        StringBuilder filasDiag = new StringBuilder();
        if (diags.isEmpty()) {
            filasDiag.append("<tr><td colspan=\"3\" class=\"empty\">Sin diagnósticos registrados</td></tr>");
        } else {
            for (Map<String, Object> d : diags) {
                filasDiag.append("<tr>")
                    .append("<td class=\"code\">").append(esc(d.get("codigo"))).append("</td>")
                    .append("<td>").append(esc(d.get("descripcion"))).append("</td>")
                    .append("<td>").append(esc(orDash(d.get("observacion")))).append("</td>")
                    .append("</tr>");
            }
        }

        StringBuilder filasMed = new StringBuilder();
        if (meds.isEmpty()) {
            filasMed.append("<tr><td colspan=\"4\" class=\"empty\">Sin medicamentos</td></tr>");
        } else {
            int i = 1;
            for (Map<String, Object> m : meds) {
                filasMed.append("<tr>")
                    .append("<td class=\"num\">").append(i++).append("</td>")
                    .append("<td>").append(esc(m.get("nombre"))).append("</td>")
                    .append("<td>").append(esc(orDash(m.get("dosis")))).append("</td>")
                    .append("<td>").append(esc(orDash(m.get("duracion")))).append("</td>")
                    .append("</tr>");
            }
        }

        String comentariosBloque = comentarios.isBlank() ? "" : """
            <div class="section">
              <div class="section-title">Indicaciones del médico</div>
              <div class="box">%s</div>
            </div>
            """.formatted(esc(comentarios));

        String html = """
            <?xml version="1.0" encoding="UTF-8"?>
            <html>
            <head>
            <style>
              @page { size: A4; margin: 0; }
              * { box-sizing: border-box; }
              body { margin: 0; font-family: Helvetica, Arial, sans-serif; color: #1E293B; font-size: 11pt; }
              .page { padding: 0 0 90px 0; position: relative; min-height: 100%%; }
              .header { background-color: #8B1414; color: #F9F5F0; padding: 22px 40px; }
              .header .uni { font-size: 16pt; font-weight: bold; letter-spacing: .5px; }
              .header .sub { font-size: 9.5pt; opacity: .92; margin-top: 2px; }
              .titlebar { background-color: #711610; color: #F9F5F0; padding: 8px 40px;
                          font-size: 12pt; font-weight: bold; letter-spacing: 3px; }
              .content { padding: 24px 40px; }
              .meta { width: 100%%; border-collapse: collapse; margin-bottom: 6px; }
              .meta td { padding: 4px 0; vertical-align: top; font-size: 10.5pt; }
              .meta .label { color: #64748B; width: 95px; }
              .meta .val { font-weight: bold; }
              .receta-no { float: right; text-align: right; }
              .receta-no .big { color: #711610; font-size: 15pt; font-weight: bold; }
              .receta-no .lbl { color: #64748B; font-size: 8.5pt; letter-spacing: 1px; }
              .section { margin-top: 18px; }
              .section-title { color: #711610; font-size: 11pt; font-weight: bold;
                               border-bottom: 2px solid #E8DDD8; padding-bottom: 4px; margin-bottom: 8px; }
              table.data { width: 100%%; border-collapse: collapse; font-size: 10pt; }
              table.data th { background-color: #F4ECE4; color: #1E293B; text-align: left;
                              padding: 7px 10px; border: 1px solid #E8DDD8; font-size: 9.5pt; }
              table.data td { padding: 7px 10px; border: 1px solid #E8DDD8; }
              table.data tr:nth-child(even) td { background-color: #F9F5F0; }
              .code { font-weight: bold; color: #711610; white-space: nowrap; }
              .num { text-align: center; color: #64748B; width: 28px; }
              .empty { color: #94A3B8; font-style: italic; text-align: center; }
              .box { background-color: #F9F5F0; border: 1px solid #E8DDD8; border-radius: 4px;
                     padding: 10px 12px; font-size: 10.5pt; }
              .firma { margin-top: 48px; width: 100%%; }
              .firma td { width: 50%%; vertical-align: bottom; padding-top: 28px; font-size: 9.5pt; color: #64748B; }
              .firma .line { border-top: 1px solid #94A3B8; padding-top: 5px; text-align: center; }
              .footer { position: absolute; bottom: 0; left: 0; right: 0;
                        border-top: 3px solid #8B1414; background-color: #F4ECE4;
                        padding: 12px 40px; font-size: 8pt; color: #64748B; }
            </style>
            </head>
            <body>
              <div class="page">
                <div class="header">
                  <div class="uni">UNIVERSIDAD NACIONAL DE INGENIERÍA</div>
                  <div class="sub">Centro Médico Universitario &#8226; Sistema de Gestión de Salud</div>
                </div>
                <div class="titlebar">RECETA MÉDICA</div>

                <div class="content">
                  <div class="receta-no">
                    <div class="lbl">RECETA N°</div>
                    <div class="big">%s</div>
                  </div>
                  <table class="meta">
                    <tr><td class="label">Fecha:</td><td class="val">%s</td></tr>
                    <tr><td class="label">Paciente:</td><td class="val">%s</td></tr>
                    <tr><td class="label">Código:</td><td class="val">%s</td></tr>
                    <tr><td class="label">Médico:</td><td class="val">%s</td></tr>
                    <tr><td class="label">Especialidad:</td><td class="val">%s</td></tr>
                    <tr><td class="label">N° de cita:</td><td class="val">%s</td></tr>
                  </table>

                  <div class="section">
                    <div class="section-title">Diagnóstico (CIE-10)</div>
                    <table class="data">
                      <thead><tr><th style="width:90px;">Código</th><th>Descripción</th><th>Observación</th></tr></thead>
                      <tbody>%s</tbody>
                    </table>
                  </div>

                  <div class="section">
                    <div class="section-title">Prescripción</div>
                    <table class="data">
                      <thead><tr><th>#</th><th>Medicamento</th><th>Dosis</th><th>Duración</th></tr></thead>
                      <tbody>%s</tbody>
                    </table>
                  </div>

                  %s

                  <table class="firma">
                    <tr>
                      <td></td>
                      <td><div class="line">%s<br/>%s</div></td>
                    </tr>
                  </table>
                </div>

                <div class="footer">
                  Documento generado automáticamente por el Sistema de Gestión del Centro Médico UNI.
                  Este documento no requiere firma manuscrita. Para recoger los medicamentos, acérquese a la
                  farmacia del Centro Médico presentando esta receta.
                </div>
              </div>
            </body>
            </html>
            """.formatted(
                nReceta,
                esc(fecha),
                esc(orDash(est.get("nombre"))),
                esc(orDash(est.get("id"))),
                esc(orDash(doc.get("nombre"))),
                esc(orDash(doc.get("especialidad"))),
                nCita,
                filasDiag,
                filasMed,
                comentariosBloque,
                esc(orDash(doc.get("nombre"))),
                esc(orDash(doc.get("especialidad")))
            );

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el PDF de la receta: " + e.getMessage(), e);
        }
    }

    // ----- helpers -----

    private static int asInt(Object o) {
        if (o instanceof Number n) return n.intValue();
        try { return Integer.parseInt(String.valueOf(o)); } catch (Exception e) { return 0; }
    }

    private static String str(Object o) {
        return o == null ? "" : o.toString();
    }

    private static String orDash(Object o) {
        String s = str(o);
        return s.isBlank() ? "—" : s;
    }

    private static String fecha(Object o) {
        if (o == null) return LocalDateTime.now().format(FECHA_FMT);
        try {
            return LocalDateTime.parse(o.toString()).format(FECHA_FMT);
        } catch (Exception e) {
            return o.toString();
        }
    }

    /** Escapa para XHTML (openhtmltopdf exige contenido bien formado). */
    private static String esc(Object o) {
        String s = str(o);
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
