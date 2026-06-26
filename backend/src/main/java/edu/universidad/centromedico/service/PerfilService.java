package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.ActualizarPerfilRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Datos de "Mi Perfil": información personal según el rol + estadísticas de
 * citas. Replica el PerfilPanel del desktop.
 */
@Service
@RequiredArgsConstructor
public class PerfilService {

    private final JdbcTemplate jdbc;

    public Map<String, Object> perfil(String id) {
        String rol = jdbc.queryForObject(
            "SELECT rol FROM usuarios WHERE id = ? AND eliminado = 0", String.class, id);

        Map<String, Object> p = new LinkedHashMap<>();
        p.put("id", id);
        p.put("rol", rol);

        switch (rol == null ? "" : rol) {
            case "ESTUDIANTE" -> {
                Map<String, Object> e = jdbc.queryForMap(
                    "SELECT nombre, carrera, email, edad, fecha_nacimiento, foto FROM estudiantes WHERE id_usuario = ?", id);
                p.putAll(e);
                p.remove("fecha_nacimiento"); // se reexpone como `fechaNacimiento` (ISO)
                // La edad mostrada se deriva de la fecha de nacimiento cuando existe.
                Object fn = e.get("fecha_nacimiento");
                if (fn instanceof java.sql.Date d) {
                    p.put("fechaNacimiento", d.toLocalDate().toString());
                    p.put("edad", java.time.Period.between(d.toLocalDate(), java.time.LocalDate.now()).getYears());
                }
                ponerEstadisticas(p, "id_estudiante", id);
            }
            case "DOCTOR" -> {
                p.putAll(jdbc.queryForMap(
                    "SELECT nombre, especialidad, consultorio, activo, foto FROM doctores WHERE id_usuario = ?", id));
                ponerEstadisticas(p, "id_doctor", id);
            }
            case "ADMIN" -> p.put("nombre", jdbc.queryForObject(
                "SELECT nombre FROM administradores WHERE id_usuario = ?", String.class, id));
            case "FARMACIA" -> p.put("nombre", jdbc.queryForObject(
                "SELECT nombre FROM farmacia_usuarios WHERE id_usuario = ?", String.class, id));
            default -> { /* sin datos extra */ }
        }
        return p;
    }

    /** Límite defensivo del tamaño de la foto (data URL base64 ≈ 1.3 MB de imagen). */
    private static final int MAX_FOTO_CHARS = 1_800_000;

    /** Edita el perfil del estudiante autenticado (email, carrera, nacimiento, foto). */
    @Transactional
    public void actualizar(String id, ActualizarPerfilRequest req) {
        String rol = jdbc.queryForObject(
            "SELECT rol FROM usuarios WHERE id = ? AND eliminado = 0", String.class, id);
        if (!"ESTUDIANTE".equals(rol)) {
            throw new RuntimeException("Solo los estudiantes pueden editar su perfil");
        }

        edu.universidad.centromedico.model.Catalogos.validarCarrera(req.getCarrera());

        int edad = Period.between(req.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18 || edad > 100) {
            throw new RuntimeException("El paciente debe tener entre 18 y 100 años");
        }
        if (req.getFoto() != null && req.getFoto().length() > MAX_FOTO_CHARS) {
            throw new RuntimeException("La foto es demasiado grande (máx. ~1.3 MB)");
        }

        // La foto solo se actualiza si viene en la petición (null = sin cambios).
        if (req.getFoto() == null) {
            jdbc.update(
                "UPDATE estudiantes SET email = ?, carrera = ?, fecha_nacimiento = ?, edad = ? WHERE id_usuario = ?",
                req.getEmail(), req.getCarrera(), java.sql.Date.valueOf(req.getFechaNacimiento()), edad, id);
        } else {
            jdbc.update(
                "UPDATE estudiantes SET email = ?, carrera = ?, fecha_nacimiento = ?, edad = ?, foto = ? WHERE id_usuario = ?",
                req.getEmail(), req.getCarrera(), java.sql.Date.valueOf(req.getFechaNacimiento()), edad,
                req.getFoto().isBlank() ? null : req.getFoto(), id);
        }
    }

    /** Edita el perfil del médico autenticado (consultorio y foto). */
    @Transactional
    public void actualizarDoctor(String id, edu.universidad.centromedico.dto.ActualizarPerfilDoctorRequest req) {
        String rol = jdbc.queryForObject(
            "SELECT rol FROM usuarios WHERE id = ? AND eliminado = 0", String.class, id);
        if (!"DOCTOR".equals(rol)) {
            throw new RuntimeException("Solo los médicos pueden editar este perfil");
        }
        if (req.getFoto() != null && req.getFoto().length() > MAX_FOTO_CHARS) {
            throw new RuntimeException("La foto es demasiado grande (máx. ~1.3 MB)");
        }

        // La foto solo se actualiza si viene en la petición (null = sin cambios).
        if (req.getFoto() == null) {
            jdbc.update("UPDATE doctores SET consultorio = ? WHERE id_usuario = ?",
                req.getConsultorio(), id);
        } else {
            jdbc.update("UPDATE doctores SET consultorio = ?, foto = ? WHERE id_usuario = ?",
                req.getConsultorio(), req.getFoto().isBlank() ? null : req.getFoto(), id);
        }
    }

    /** col es una constante interna ("id_estudiante"/"id_doctor"), no entrada del usuario. */
    private void ponerEstadisticas(Map<String, Object> p, String col, String id) {
        String base = "SELECT COUNT(*) FROM citas WHERE " + col + " = ? AND eliminado = 0";
        p.put("totalCitas",      jdbc.queryForObject(base, Integer.class, id));
        p.put("citasPendientes", jdbc.queryForObject(base + " AND estado = 'PENDIENTE'", Integer.class, id));
        p.put("citasAtendidas",  jdbc.queryForObject(base + " AND estado = 'ATENDIDA'", Integer.class, id));
    }
}
