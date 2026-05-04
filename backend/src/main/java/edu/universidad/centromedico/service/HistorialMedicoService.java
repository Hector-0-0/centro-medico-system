package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.HistorialDTO;
import edu.universidad.centromedico.model.Cita;
import edu.universidad.centromedico.model.EstadoCita;
import edu.universidad.centromedico.model.HistorialMedico;
import edu.universidad.centromedico.repository.CitaRepository;
import edu.universidad.centromedico.repository.HistorialMedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio para el historial médico.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HistorialMedicoService {

    private final HistorialMedicoRepository historialRepository;
    private final CitaRepository citaRepository;

    public List<HistorialMedico> listarPorPaciente(Long pacienteId) {
        return historialRepository.findByCitaPacienteId(pacienteId);
    }

    public List<HistorialMedico> listarPorMedico(Long medicoId) {
        return historialRepository.findByCitaMedicoId(medicoId);
    }

    public HistorialMedico buscarPorId(Long id) {
        return historialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Historial no encontrado con id: " + id));
    }

    public HistorialMedico buscarPorCita(Long citaId) {
        return historialRepository.findByCitaId(citaId)
            .orElseThrow(() -> new RuntimeException("No hay historial para la cita id: " + citaId));
    }

    /**
     * Registra el historial de una cita y la marca como ATENDIDA.
     */
    public HistorialMedico registrar(HistorialDTO dto) {
        Cita cita = citaRepository.findById(dto.getCitaId())
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (historialRepository.findByCitaId(cita.getId()).isPresent()) {
            throw new RuntimeException("La cita ya tiene un historial registrado");
        }

        // Marcar la cita como atendida
        cita.setEstado(EstadoCita.ATENDIDA);
        citaRepository.save(cita);

        HistorialMedico historial = new HistorialMedico();
        historial.setCita(cita);
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setReceta(dto.getReceta());
        historial.setObservaciones(dto.getObservaciones());

        return historialRepository.save(historial);
    }

    public HistorialMedico actualizar(Long id, HistorialDTO dto) {
        HistorialMedico historial = buscarPorId(id);
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setReceta(dto.getReceta());
        historial.setObservaciones(dto.getObservaciones());
        return historialRepository.save(historial);
    }
}
