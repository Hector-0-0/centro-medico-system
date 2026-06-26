package edu.universidad.centromedico.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Genera el PDF de la receta médica a partir del payload de la atención.
 * Usa openhtmltopdf (HTML/CSS -> PDF) reproduciendo el formato oficial de la
 * receta de la UNI: cabecera azul con el escudo institucional, bloque de datos
 * del paciente/médico en dos columnas y la tabla de prescripción.
 */
@Service
public class RecetaPdfService {

    private static final DateTimeFormatter FECHA_FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Escudo de la UNI embebido como data URI (se carga una sola vez del classpath). */
    private static final String LOGO_DATA_URI = cargarLogo();

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

        String fecha = fecha(payload.get("fecha"));

        // "Diag. Asociado": no tenemos diagnóstico ligado a cada medicamento, así que
        // se muestra el resumen de los diagnósticos CIE de la atención en cada fila.
        String diagAsociado;
        if (diags.isEmpty()) {
            diagAsociado = "No hay Diagnóstico asociado";
        } else {
            diagAsociado = diags.stream()
                .map(d -> str(orDash(d.get("descripcion"))))
                .collect(Collectors.joining(", "));
        }
        String diagAsociadoEsc = esc(diagAsociado);

        StringBuilder filas = new StringBuilder();
        if (meds.isEmpty()) {
            filas.append("<tr><td colspan=\"8\" class=\"empty\">Sin medicamentos prescritos</td></tr>");
        } else {
            int i = 1;
            for (Map<String, Object> m : meds) {
                String codigo = str(m.get("id")).isBlank()
                    ? esc(m.get("nombre"))
                    : "(" + esc(m.get("id")) + ") " + esc(m.get("nombre"));
                String concentracion = str(m.get("dosisMg")).isBlank() ? "—" : esc(m.get("dosisMg")) + "MG";
                filas.append("<tr>")
                    .append("<td class=\"num\">").append(i++).append("</td>")
                    .append("<td>").append(codigo).append("</td>")
                    .append("<td>").append(diagAsociadoEsc).append("</td>")
                    .append("<td class=\"ctr\">").append(concentracion).append("</td>")
                    .append("<td class=\"ctr\">").append(esc(orDash(m.get("tipo")))).append("</td>")
                    .append("<td class=\"ctr\">").append(esc(orDash(m.get("duracion")))).append("</td>")
                    .append("<td class=\"ctr\">—</td>")
                    .append("<td>").append(esc(orDash(m.get("dosis")))).append("</td>")
                    .append("</tr>");
            }
        }

        String logoTag = LOGO_DATA_URI.isBlank()
            ? ""
            : "<img class=\"logo\" src=\"" + LOGO_DATA_URI + "\" />";

        String html = PLANTILLA
            .replace("@@LOGO@@", logoTag)
            .replace("@@NOMBRE@@", esc(orDash(est.get("nombre"))))
            .replace("@@EDAD@@", esc(orDash(est.get("edad"))))
            .replace("@@NDOC@@", esc(orDash(est.get("id"))))
            .replace("@@ESPECIALIDAD@@", esc(orDash(doc.get("especialidad"))))
            .replace("@@DOCTOR@@", esc(orDash(doc.get("nombre"))))
            .replace("@@FECHA@@", esc(fecha))
            .replace("@@FILAS@@", filas.toString());

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

    /** Esqueleto HTML/CSS de la receta (placeholders @@...@@ se rellenan en generar()). */
    private static final String PLANTILLA = """
        <?xml version="1.0" encoding="UTF-8"?>
        <html>
        <head>
        <style>
          @page { size: A4; margin: 28px 36px; }
          * { box-sizing: border-box; }
          body { margin: 0; font-family: Helvetica, Arial, sans-serif; color: #1a1a1a; font-size: 10pt; }
          .head { width: 100%; border-collapse: collapse; margin-bottom: 6px; }
          .head td { vertical-align: middle; }
          .head .logo { height: 70px; }
          .head .cell-l { width: 110px; }
          .head .cell-r { width: 110px; text-align: right; }
          .titles { text-align: center; }
          .titles .uni { color: #2456a6; font-style: italic; font-weight: bold;
                         font-family: Georgia, 'Times New Roman', serif; font-size: 16pt; }
          .titles .doc { color: #2456a6; font-style: italic; font-weight: bold;
                         font-family: Georgia, 'Times New Roman', serif; font-size: 18pt; margin-top: 4px; }
          .info { width: 100%; border-collapse: collapse; margin-top: 8px; margin-bottom: 14px; }
          .info td { padding: 3px 0; font-size: 9.5pt; vertical-align: top; }
          .info .lbl { color: #1a1a1a; width: 150px; }
          .info .sep { width: 12px; color: #1a1a1a; }
          .info .val { font-weight: normal; }
          .info .lbl-r { color: #1a1a1a; width: 120px; padding-left: 24px; }
          table.rx { width: 100%; border-collapse: collapse; font-size: 8.5pt; margin-top: 4px; }
          table.rx th { background-color: #1f3c88; color: #ffffff; text-align: center;
                        padding: 5px 4px; border: 1px solid #1f3c88; font-size: 8pt; font-weight: bold; }
          table.rx td { padding: 4px 6px; border: 1px solid #c9d3e6; vertical-align: top; }
          table.rx td.num { text-align: center; width: 22px; }
          table.rx td.ctr { text-align: center; }
          table.rx .empty { color: #94A3B8; font-style: italic; text-align: center; }
          .firma { text-align: center; margin-top: 70px; }
          .firma .line { display: inline-block; border-top: 1px solid #555; padding-top: 5px;
                         min-width: 240px; }
          .firma .name { font-weight: bold; color: #1a1a1a; font-size: 10pt; }
          .firma .esp { color: #444; font-size: 9pt; margin-top: 2px; }
          .firma .cmp { color: #444; font-size: 9pt; }
          .pag { position: fixed; bottom: 6px; right: 0; font-size: 8.5pt; color: #1a1a1a; }
        </style>
        </head>
        <body>
          <table class="head">
            <tr>
              <td class="cell-l">@@LOGO@@</td>
              <td class="titles">
                <div class="uni">Universidad Nacional de Ingeniería</div>
                <div class="doc">RECETA MEDICA</div>
              </td>
              <td class="cell-r"></td>
            </tr>
          </table>

          <table class="info">
            <tr>
              <td class="lbl">Nombres y Apellidos</td><td class="sep">:</td><td class="val">@@NOMBRE@@</td>
              <td class="lbl-r">Edad (Años)</td><td class="sep">:</td><td class="val">@@EDAD@@</td>
            </tr>
            <tr>
              <td class="lbl">Tipo Documento Identidad</td><td class="sep">:</td><td class="val">DNI</td>
              <td class="lbl-r">N° Doc. Identidad</td><td class="sep">:</td><td class="val">@@NDOC@@</td>
            </tr>
            <tr>
              <td class="lbl">Especialidad</td><td class="sep">:</td><td class="val">@@ESPECIALIDAD@@</td>
              <td class="lbl-r">Fecha de Atención</td><td class="sep">:</td><td class="val">@@FECHA@@</td>
            </tr>
            <tr>
              <td class="lbl">Especialista de la Salud</td><td class="sep">:</td><td class="val">@@DOCTOR@@</td>
              <td class="lbl-r"></td><td class="sep"></td><td class="val"></td>
            </tr>
          </table>

          <table class="rx">
            <thead>
              <tr>
                <th>N°</th>
                <th>Codigo ATC</th>
                <th>Diag. Asociado</th>
                <th>Concentración</th>
                <th>Forma Farmaceutica</th>
                <th>Días Tratami.</th>
                <th>Cant.</th>
                <th>Indicaciones</th>
              </tr>
            </thead>
            <tbody>@@FILAS@@</tbody>
          </table>

          <div class="firma">
            <div class="line">
              <div class="name">@@DOCTOR@@</div>
              <div class="esp">@@ESPECIALIDAD@@</div>
              <div class="cmp">C.M.P.</div>
            </div>
          </div>

          <div class="pag">Pag :1</div>
        </body>
        </html>
        """;

    // ----- helpers -----

    private static String cargarLogo() {
        try (InputStream in = RecetaPdfService.class.getResourceAsStream("/images/logo-uni.png")) {
            if (in == null) return "";
            byte[] bytes = in.readAllBytes();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return "";
        }
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
