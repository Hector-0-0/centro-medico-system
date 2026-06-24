package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.CitaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Consulta de citas. Replica el CitaDAO del desktop: trae todas las citas no
 * eliminadas con el nombre del paciente, del médico, especialidad y horario.
 */
@Service
@RequiredArgsConstructor
public class CitaService {

    private final JdbcTemplate jdbc;

    private static final RowMapper<CitaDTO> MAPPER = (rs, n) -> new CitaDTO(
        rs.getInt("id"),
        rs.getString("id_estudiante"),
        rs.getString("nombre_estudiante"),
        rs.getString("id_doctor"),
        rs.getString("nombre_doctor"),
        rs.getString("especialidad"),
        rs.getString("motivo"),
        rs.getString("estado"),
        rs.getString("dia_semana"),
        rs.getString("hora_inicio"),
        rs.getString("hora_fin")
    );

    private static final String SELECT_CITA = """
        SELECT c.id, c.id_estudiante, c.id_doctor, c.motivo, c.estado,
               e.nombre AS nombre_estudiante,
               d.nombre AS nombre_doctor,
               d.especialidad,
               s.dia_semana, s.hora_inicio, s.hora_fin
        FROM citas c
        JOIN estudiantes          e ON c.id_estudiante = e.id_usuario
        JOIN doctores             d ON c.id_doctor     = d.id_usuario
        JOIN slots_disponibilidad s ON c.id_slot       = s.id
        """;

    /** Todas las citas (cualquier especialidad/estado), ordenadas por id desc. */
    public List<CitaDTO> obtenerTodas() {
        return jdbc.query(SELECT_CITA + " WHERE c.eliminado = 0 ORDER BY c.id DESC", MAPPER);
    }

    /** Citas de un médico (cualquier estado), ordenadas por id desc. */
    public List<CitaDTO> obtenerPorDoctor(String idDoctor) {
        return jdbc.query(
            SELECT_CITA + " WHERE c.id_doctor = ? AND c.eliminado = 0 ORDER BY c.id DESC",
            MAPPER, idDoctor);
    }

    /** Citas de un estudiante (cualquier estado), ordenadas por id desc. */
    public List<CitaDTO> obtenerPorEstudiante(String idEstudiante) {
        return jdbc.query(
            SELECT_CITA + " WHERE c.id_estudiante = ? AND c.eliminado = 0 ORDER BY c.id DESC",
            MAPPER, idEstudiante);
    }

    /** Agenda una cita en un slot disponible: crea la cita y ocupa el slot. */
    @Transactional
    public void agendar(String idEstudiante, int idSlot, String motivo) {
        List<Map<String, Object>> filas = jdbc.queryForList(
            "SELECT disponible, id_doctor FROM slots_disponibilidad WHERE id = ? AND eliminado = 0", idSlot);
        if (filas.isEmpty()) {
            throw new RuntimeException("El horario no existe");
        }
        Map<String, Object> slot = filas.get(0);
        Object disp = slot.get("disponible");
        boolean disponible = (disp instanceof Boolean b) ? b : ((Number) disp).intValue() == 1;
        if (!disponible) {
            throw new RuntimeException("El horario ya no está disponible");
        }

        jdbc.update(
            "INSERT INTO citas (id_estudiante, id_doctor, id_slot, motivo, estado) VALUES (?, ?, ?, ?, 'PENDIENTE')",
            idEstudiante, slot.get("id_doctor"), idSlot, motivo);
        jdbc.update("UPDATE slots_disponibilidad SET disponible = 0 WHERE id = ?", idSlot);
    }
}
