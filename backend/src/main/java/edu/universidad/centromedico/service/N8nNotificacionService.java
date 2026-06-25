package edu.universidad.centromedico.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Envía notificaciones a n8n vía webhook HTTP. n8n se encarga del resto de la
 * automatización (p.ej. enviar la receta al estudiante por Gmail).
 *
 * <p>Es tolerante a fallos a propósito: si la URL no está configurada o n8n no
 * responde, se registra el error pero NUNCA se propaga, para no afectar el
 * guardado de la receta. Además corre @Async para no bloquear la respuesta HTTP.
 */
@Service
@RequiredArgsConstructor
public class N8nNotificacionService {

    private static final Logger log = LoggerFactory.getLogger(N8nNotificacionService.class);

    private final ObjectMapper objectMapper;

    @Value("${n8n.webhook.receta.url:}")
    private String recetaWebhookUrl;

    // HTTP/1.1 explícito: el HttpClient de Java usa HTTP/2 por defecto e intenta
    // el upgrade h2c, que n8n (Node/Express) no maneja bien y provoca timeouts.
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /** POST del payload de una receta al webhook de n8n. */
    @Async
    public void notificarReceta(Map<String, Object> payload) {
        if (recetaWebhookUrl == null || recetaWebhookUrl.isBlank()) {
            log.debug("n8n.webhook.receta.url no configurada; se omite la notificación de receta.");
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(payload);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(recetaWebhookUrl))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Receta notificada a n8n (cita {}). HTTP {}",
                        payload.get("idCita"), response.statusCode());
            } else {
                log.warn("n8n respondió HTTP {} al notificar la receta (cita {}): {}",
                        response.statusCode(), payload.get("idCita"), response.body());
            }
        } catch (Exception e) {
            // Nunca propagamos: la receta ya se guardó; el email es best-effort.
            log.error("No se pudo notificar la receta a n8n (cita {}): {}",
                    payload.get("idCita"), e.getMessage());
        }
    }
}
