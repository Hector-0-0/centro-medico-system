package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.AgendarDTO;
import edu.universidad.centromedico.dto.DisponibilidadDTO;
import edu.universidad.centromedico.model.*;
import edu.universidad.centromedico.repository.CitaRepository;
import edu.universidad.centromedico.repository.DisponibilidadRepository;
import edu.universidad.centromedico.repository.MedicoRepository;
import edu.universidad.centromedico.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Lógica de negocio para los horarios de disponibilidad de los médicos
 * y el agendamiento de citas a partir de ellos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;

    private static final Map<String, DayOfWeek> DIAS = Map.of(
        "LUNES", DayOfWeek.MONDAY, "MARTES", DayOfWeek.TUESDAY,
        "MIERCOLES", DayOfWeek.WEDNESDAY, "JUEVES", DayOfWeek.THURSDAY,
        "VIERNES", DayOfWeek.FRIDAY, "SABADO", DayOfWeek.SATURDAY,
        "DOMINGO", DayOfWeek.SUNDAY
    );

    public List<Disponibilidad> listarDisponibles() {
        return disponibilidadRepository.findByActivoTrue();
    }

    public List<Disponibilidad> listarPorMedico(Long medicoId) {
        return disponibilidadRepository.findByMedicoIdAndActivoTrue(medicoId);
    }

    public Disponibilidad crear(DisponibilidadDTO dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Disponibilidad d = new Disponibilidad();
        d.setMedico(medico);
        d.setDiaSemana(dto.getDiaSemana().toUpperCase());
        d.setHoraInicio(dto.getHoraInicio());
        d.setHoraFin(dto.getHoraFin());
        d.setConsultorio(dto.getConsultorio());
        d.setActivo(true);
        return disponibilidadRepository.save(d);
    }

    public Disponibilidad actualizar(Long id, DisponibilidadDTO dto) {
        Disponibilidad d = buscarPorId(id);
        d.setDiaSemana(dto.getDiaSemana().toUpperCase());
        d.setHoraInicio(dto.getHoraInicio());
        d.setHoraFin(dto.getHoraFin());
        d.setConsultorio(dto.getConsultorio());
        return disponibilidadRepository.save(d);
    }

    public void eliminar(Long id) {
        Disponibilidad d = buscarPorId(id);
        d.setActivo(false); // borrado lógico
        disponibilidadRepository.save(d);
    }

    public Disponibilidad buscarPorId(Long id) {
        return disponibilidadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Horario no encontrado con id: " + id));
    }

    /**
     * Agenda una cita a partir de un horario de disponibilidad.
     * Combina la fecha elegida con la hora de inicio del horario.
     */
    public Cita agendar(AgendarDTO dto) {
        Disponibilidad d = buscarPorId(dto.getDisponibilidadId());
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        DayOfWeek esperado = DIAS.get(d.getDiaSemana().toUpperCase());
        if (esperado != null && dto.getFecha().getDayOfWeek() != esperado) {
            throw new RuntimeException("La fecha elegida no corresponde al día " + d.getDiaSemana());
        }

        LocalDateTime fechaHora = dto.getFecha().atTime(d.getHoraInicio());

        boolean ocupado = citaRepository.findByMedicoIdAndFechaHoraBetween(
            d.getMedico().getId(),
            fechaHora.minusMinutes(29),
            fechaHora.plusMinutes(29)
        ).stream().anyMatch(c -> c.getEstado() != EstadoCita.CANCELADA);

        if (ocupado) {
            throw new RuntimeException("El médico ya tiene una cita en ese horario");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(d.getMedico());
        cita.setFechaHora(fechaHora);
        cita.setMotivo(dto.getMotivo());
        cita.setConsultorio(d.getConsultorio());
        cita.setEstado(EstadoCita.PENDIENTE);
        return citaRepository.save(cita);
    }
}
