package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.DisponibilidadDTO;
import edu.universidad.centromedico.dto.GuardarDisponibilidadRequest;
import edu.universidad.centromedico.dto.GuardarDisponibilidadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Disponibilidad del médico. Replica el DisponibilidadDAO del desktop: listar
 * rangos, y guardar por día regenerando los slots de 30 minutos (bloqueando si
 * el día tiene citas pendientes).
 */
@Service
@RequiredArgsConstructor
public class DisponibilidadService {

    private final JdbcTemplate jdbc;

    public List<DisponibilidadDTO> listar(String idDoctor) {
        return jdbc.query("""
            SELECT id, dia_semana, hora_inicio, hora_fin
            FROM disponibilidad_doctor
            WHERE id_doctor = ? AND eliminado = 0
            ORDER BY dia_semana, hora_inicio
            """,
            (rs, n) -> new DisponibilidadDTO(rs.getInt("id"), rs.getString("dia_semana"),
                rs.getString("hora_inicio"), rs.getString("hora_fin")),
            idDoctor);
    }

    /** Guarda varios días; cada uno se acepta o rechaza por su cuenta. */
    @Transactional
    public GuardarDisponibilidadResponse guardar(String idDoctor, GuardarDisponibilidadRequest req) {
        int guardados = 0, rechazados = 0;
        if (req.getDias() != null) {
            for (GuardarDisponibilidadRequest.Dia d : req.getDias()) {
                if (guardarDia(idDoctor, d.getDiaSemana(), d.getHoraInicio(), d.getHoraFin())) {
                    guardados++;
                } else {
                    rechazados++;
                }
            }
        }
        return new GuardarDisponibilidadResponse(guardados, rechazados);
    }

    /** Procesa un día dentro de la transacción. Devuelve false si se rechaza. */
    private boolean guardarDia(String idDoctor, String dia, String ini, String fin) {
        if (dia == null || dia.isBlank() || ini == null || fin == null) return false;
        if (ini.compareTo(fin) >= 0) return false;                 // rango inválido
        if (tieneCitasPendientes(idDoctor, dia, ini, fin)) return false;

        // 1. Soft-delete de slots disponibles y disponibilidad anterior de ese día.
        jdbc.update("""
            UPDATE s SET s.eliminado = 1
            FROM slots_disponibilidad s
            INNER JOIN disponibilidad_doctor d ON s.id_disponibilidad = d.id
            WHERE d.id_doctor = ? AND d.dia_semana = ? AND s.disponible = 1 AND s.eliminado = 0
            """, idDoctor, dia);
        jdbc.update("""
            UPDATE disponibilidad_doctor SET eliminado = 1
            WHERE id_doctor = ? AND dia_semana = ? AND eliminado = 0
            """, idDoctor, dia);

        // 2. Insertar la nueva disponibilidad.
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO disponibilidad_doctor (id_doctor, dia_semana, hora_inicio, hora_fin)
                VALUES (?, ?, ?, ?)
                """, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, idDoctor);
            ps.setString(2, dia);
            ps.setString(3, ini);
            ps.setString(4, fin);
            return ps;
        }, kh);
        int idDisp = kh.getKey().intValue();

        // 3. Generar e insertar los slots de 30 minutos.
        List<String[]> slots = generarSlots(ini, fin);
        jdbc.batchUpdate("""
            INSERT INTO slots_disponibilidad (id_disponibilidad, id_doctor, dia_semana, hora_inicio, hora_fin)
            VALUES (?, ?, ?, ?, ?)
            """, slots, slots.size(), (ps, slot) -> {
                ps.setInt(1, idDisp);
                ps.setString(2, idDoctor);
                ps.setString(3, dia);
                ps.setString(4, slot[0]);
                ps.setString(5, slot[1]);
            });
        return true;
    }

    private boolean tieneCitasPendientes(String idDoctor, String dia, String ini, String fin) {
        Integer n = jdbc.queryForObject("""
            SELECT COUNT(*) FROM citas c
            JOIN slots_disponibilidad s   ON c.id_slot = s.id
            JOIN disponibilidad_doctor d  ON s.id_disponibilidad = d.id
            WHERE d.id_doctor = ? AND d.dia_semana = ?
              AND s.hora_inicio >= ? AND s.hora_fin <= ?
              AND c.estado = 'PENDIENTE' AND d.eliminado = 0
            """, Integer.class, idDoctor, dia, ini, fin);
        return n != null && n > 0;
    }

    /** Genera pares [inicio, fin] de 30 minutos dentro del rango. */
    private List<String[]> generarSlots(String horaInicio, String horaFin) {
        List<String[]> slots = new ArrayList<>();
        int inicio = toMinutos(horaInicio);
        int fin = toMinutos(horaFin);
        while (inicio + 30 <= fin) {
            slots.add(new String[]{ toHora(inicio), toHora(inicio + 30) });
            inicio += 30;
        }
        return slots;
    }

    private int toMinutos(String hora) {
        String[] p = hora.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    private String toHora(int minutos) {
        return String.format("%02d:%02d", minutos / 60, minutos % 60);
    }
}
