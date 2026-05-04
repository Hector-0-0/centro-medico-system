package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.CitaDTO;
import edu.universidad.centromedico.model.Cita;
import edu.universidad.centromedico.model.EstadoCita;
import edu.universidad.centromedico.model.Medico;
import edu.universidad.centromedico.model.Paciente;
import edu.universidad.centromedico.repository.CitaRepository;
import edu.universidad.centromedico.repository.MedicoRepository;
import edu.universidad.centromedico.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio para la agenda de citas médicas.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public List<Cita> listarDeHoy() {
        return citaRepository.findCitasDeHoy();
    }

    public List<Cita> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> listarPorMedico(Long medicoId) {
        return citaRepository.findByMedicoId(medicoId);
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
    }

    /**
     * Crea una nueva cita validando que el médico esté disponible en ese horario.
     */
    public Cita crear(CitaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Validar disponibilidad del médico (ventana de ±29 minutos)
        boolean ocupado = citaRepository.findByMedicoIdAndFechaHoraBetween(
            dto.getMedicoId(),
            dto.getFechaHora().minusMinutes(29),
            dto.getFechaHora().plusMinutes(29)
        ).stream().anyMatch(c -> c.getEstado() != EstadoCita.CANCELADA);

        if (ocupado) {
            throw new RuntimeException("El médico ya tiene una cita en ese horario");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setConsultorio(dto.getConsultorio());
        cita.setEstado(EstadoCita.PENDIENTE);

        return citaRepository.save(cita);
    }

    public Cita cancelar(Long id) {
        Cita cita = buscarPorId(id);
        if (cita.getEstado() == EstadoCita.ATENDIDA) {
            throw new RuntimeException("No se puede cancelar una cita ya atendida");
        }
        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

    public Cita marcarAtendida(Long id) {
        Cita cita = buscarPorId(id);
        cita.setEstado(EstadoCita.ATENDIDA);
        return citaRepository.save(cita);
    }

    public Cita reprogramar(Long id, CitaDTO dto) {
        Cita cita = buscarPorId(id);
        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado(EstadoCita.REPROGRAMADA);
        return citaRepository.save(cita);
    }
}
