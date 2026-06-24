package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.SlotDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/** Turnos (slots) de los médicos. Replica SlotDAO.obtenerTodos del desktop. */
@Service
@RequiredArgsConstructor
public class SlotService {

    private final JdbcTemplate jdbc;

    public List<SlotDTO> listar() {
        return jdbc.query("""
            SELECT s.id, s.id_doctor, s.dia_semana, s.hora_inicio, s.hora_fin, s.disponible,
                   d.nombre AS nombre_doctor, d.especialidad, d.consultorio
            FROM slots_disponibilidad s
            JOIN doctores d ON s.id_doctor = d.id_usuario
            WHERE s.eliminado = 0
            ORDER BY d.especialidad, s.dia_semana, s.hora_inicio
            """,
            (rs, n) -> new SlotDTO(
                rs.getInt("id"), rs.getString("id_doctor"), rs.getString("nombre_doctor"),
                rs.getString("especialidad"), rs.getString("consultorio"),
                rs.getString("dia_semana"), rs.getString("hora_inicio"), rs.getString("hora_fin"),
                rs.getBoolean("disponible")));
    }
}
