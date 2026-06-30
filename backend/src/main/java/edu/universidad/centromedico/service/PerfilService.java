package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.ActualizarPerfilDoctorRequest;
import edu.universidad.centromedico.dto.ActualizarPerfilEmpleadoRequest;
import edu.universidad.centromedico.dto.ActualizarPerfilRequest;
import edu.universidad.centromedico.model.Catalogos;
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
 * citas. Replica el PerfilPanel del desktop. Cada rol puede editar los campos
 * coherentes con su función; todo cambio se confirma con la contraseña actual.
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
                    "SELECT nombre, dni, carrera, email, edad, fecha_nacimiento, telefono, "
                    + "alergias, tipo_sangre, contacto_emergencia, foto "
                    + "FROM estudiantes WHERE id_usuario = ?", id);
                p.putAll(e);
                // Reexpone columnas snake_case como camelCase para el frontend.
                p.remove("fecha_nacimiento");
                p.remove("tipo_sangre");
                p.remove("contacto_emergencia");
                p.put("tipoSangre", e.get("tipo_sangre"));
                p.put("contactoEmergencia", e.get("contacto_emergencia"));
                // La edad mostrada se deriva de la fecha de nacimiento cuando existe.
                Object fn = e.get("fecha_nacimiento");
                if (fn instanceof java.sql.Date d) {
                    p.put("fechaNacimiento", d.toLocalDate().toString());
                    p.put("edad", Period.between(d.toLocalDate(), LocalDate.now()).getYears());
                }
                ponerEstadisticas(p, "id_estudiante", id);
            }
            case "DOCTOR" -> {
                p.putAll(jdbc.queryForMap(
                    "SELECT nombre, especialidad, consultorio, activo, telefono, email, foto "
                    + "FROM doctores WHERE id_usuario = ?", id));
                ponerEstadisticas(p, "id_doctor", id);
            }
            case "ADMIN" -> p.putAll(jdbc.queryForMap(
                "SELECT nombre, telefono, email, foto FROM administradores WHERE id_usuario = ?", id));
            case "FARMACIA" -> p.putAll(jdbc.queryForMap(
                "SELECT nombre, telefono, email, foto FROM farmacia_usuarios WHERE id_usuario = ?", id));
            default -> { /* sin datos extra */ }
        }
        return p;
    }

    /** Límite defensivo del tamaño de la foto (data URL base64 ≈ 1.3 MB de imagen). */
    private static final int MAX_FOTO_CHARS = 1_800_000;

    /** Edita el perfil del estudiante autenticado (datos personales, médicos y credenciales). */
    @Transactional
    public void actualizar(String id, ActualizarPerfilRequest req) {
        exigirRol(id, "ESTUDIANTE", "Solo los estudiantes pueden editar este perfil");
        verificarPassword(id, req.getPasswordActual());

        Catalogos.validarTipoSangre(req.getTipoSangre());
        validarFoto(req.getFoto());

        // Carrera y fecha de nacimiento NO se tocan aquí (las gestiona el admin).
        // La foto solo se actualiza si viene en la petición (null = sin cambios).
        if (req.getFoto() == null) {
            jdbc.update(
                "UPDATE estudiantes SET email = ?, telefono = ?, alergias = ?, "
                + "tipo_sangre = ?, contacto_emergencia = ? WHERE id_usuario = ?",
                req.getEmail(), blankToNull(req.getTelefono()), blankToNull(req.getAlergias()),
                blankToNull(req.getTipoSangre()), blankToNull(req.getContactoEmergencia()), id);
        } else {
            jdbc.update(
                "UPDATE estudiantes SET email = ?, telefono = ?, alergias = ?, "
                + "tipo_sangre = ?, contacto_emergencia = ?, foto = ? WHERE id_usuario = ?",
                req.getEmail(), blankToNull(req.getTelefono()), blankToNull(req.getAlergias()),
                blankToNull(req.getTipoSangre()), blankToNull(req.getContactoEmergencia()),
                blankToNull(req.getFoto()), id);
        }
        cambiarPasswordSiCorresponde(id, req.getPasswordActual(), req.getPasswordNueva());
    }

    /** Edita el perfil del médico autenticado (consultorio, contacto, foto y credenciales). */
    @Transactional
    public void actualizarDoctor(String id, ActualizarPerfilDoctorRequest req) {
        exigirRol(id, "DOCTOR", "Solo los médicos pueden editar este perfil");
        verificarPassword(id, req.getPasswordActual());
        validarFoto(req.getFoto());

        if (req.getFoto() == null) {
            jdbc.update("UPDATE doctores SET consultorio = ?, telefono = ?, email = ? WHERE id_usuario = ?",
                req.getConsultorio(), blankToNull(req.getTelefono()), blankToNull(req.getEmail()), id);
        } else {
            jdbc.update("UPDATE doctores SET consultorio = ?, telefono = ?, email = ?, foto = ? WHERE id_usuario = ?",
                req.getConsultorio(), blankToNull(req.getTelefono()), blankToNull(req.getEmail()),
                blankToNull(req.getFoto()), id);
        }
        cambiarPasswordSiCorresponde(id, req.getPasswordActual(), req.getPasswordNueva());
    }

    /** Edita el perfil de un empleado autenticado (ADMIN o FARMACIA): contacto, foto y credenciales. */
    @Transactional
    public void actualizarEmpleado(String id, ActualizarPerfilEmpleadoRequest req) {
        String rol = jdbc.queryForObject(
            "SELECT rol FROM usuarios WHERE id = ? AND eliminado = 0", String.class, id);
        String tabla = switch (rol == null ? "" : rol) {
            case "ADMIN" -> "administradores";
            case "FARMACIA" -> "farmacia_usuarios";
            default -> throw new RuntimeException("Este perfil no se puede editar");
        };
        verificarPassword(id, req.getPasswordActual());
        validarFoto(req.getFoto());

        if (req.getFoto() == null) {
            jdbc.update("UPDATE " + tabla + " SET telefono = ?, email = ? WHERE id_usuario = ?",
                blankToNull(req.getTelefono()), blankToNull(req.getEmail()), id);
        } else {
            jdbc.update("UPDATE " + tabla + " SET telefono = ?, email = ?, foto = ? WHERE id_usuario = ?",
                blankToNull(req.getTelefono()), blankToNull(req.getEmail()), blankToNull(req.getFoto()), id);
        }
        cambiarPasswordSiCorresponde(id, req.getPasswordActual(), req.getPasswordNueva());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void exigirRol(String id, String rolEsperado, String mensaje) {
        String rol = jdbc.queryForObject(
            "SELECT rol FROM usuarios WHERE id = ? AND eliminado = 0", String.class, id);
        if (!rolEsperado.equals(rol)) {
            throw new RuntimeException(mensaje);
        }
    }

    /** Confirma la identidad antes de cualquier cambio (contraseñas en texto plano, ver CLAUDE.md). */
    private void verificarPassword(String id, String passwordActual) {
        String actual = jdbc.queryForObject(
            "SELECT password FROM usuarios WHERE id = ? AND eliminado = 0", String.class, id);
        if (actual == null || !actual.equals(passwordActual)) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
    }

    /** Cambia la contraseña solo si se proporcionó una nueva y es distinta de la actual. */
    private void cambiarPasswordSiCorresponde(String id, String passwordActual, String passwordNueva) {
        if (passwordNueva != null && !passwordNueva.isBlank()) {
            String nueva = passwordNueva.trim();
            if (nueva.length() < 4) {
                throw new RuntimeException("La nueva contraseña debe tener al menos 4 caracteres");
            }
            if (nueva.equals(passwordActual)) {
                throw new RuntimeException("La nueva contraseña debe ser distinta de la actual");
            }
            jdbc.update("UPDATE usuarios SET password = ? WHERE id = ?", nueva, id);
        }
    }

    private void validarFoto(String foto) {
        if (foto != null && foto.length() > MAX_FOTO_CHARS) {
            throw new RuntimeException("La foto es demasiado grande (máx. ~1.3 MB)");
        }
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    /** col es una constante interna ("id_estudiante"/"id_doctor"), no entrada del usuario. */
    private void ponerEstadisticas(Map<String, Object> p, String col, String id) {
        String base = "SELECT COUNT(*) FROM citas WHERE " + col + " = ? AND eliminado = 0";
        p.put("totalCitas",      jdbc.queryForObject(base, Integer.class, id));
        p.put("citasPendientes", jdbc.queryForObject(base + " AND estado = 'PENDIENTE'", Integer.class, id));
        p.put("citasAtendidas",  jdbc.queryForObject(base + " AND estado = 'ATENDIDA'", Integer.class, id));
    }
}
