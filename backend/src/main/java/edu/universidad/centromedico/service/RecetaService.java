package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.RecetaDTO;
import edu.universidad.centromedico.model.*;
import edu.universidad.centromedico.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio de recetas: emisión por el médico y entrega por la
 * farmacia (que descuenta el stock de los medicamentos recetados).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final CitaRepository citaRepository;
    private final MedicamentoRepository medicamentoRepository;

    public List<Receta> listar() {
        return recetaRepository.findAllByOrderByFechaDesc();
    }

    public List<Receta> listarPendientes() {
        return recetaRepository.findByEstadoOrderByFechaDesc(EstadoReceta.PENDIENTE);
    }

    public List<Receta> listarPorPaciente(Long pacienteId) {
        return recetaRepository.findByPacienteIdOrderByFechaDesc(pacienteId);
    }

    public List<Receta> listarPorMedico(Long medicoId) {
        return recetaRepository.findByMedicoIdOrderByFechaDesc(medicoId);
    }

    public Receta buscarPorId(Long id) {
        return recetaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
    }

    public Receta emitir(RecetaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Medico medico = medicoRepository.findById(dto.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new RuntimeException("La receta debe tener al menos un medicamento");
        }

        Receta receta = new Receta();
        receta.setPaciente(paciente);
        receta.setMedico(medico);
        receta.setDiagnostico(dto.getDiagnostico());
        receta.setEstado(EstadoReceta.PENDIENTE);
        if (dto.getCitaId() != null) {
            citaRepository.findById(dto.getCitaId()).ifPresent(receta::setCita);
        }

        for (RecetaDTO.DetalleDTO d : dto.getDetalles()) {
            Medicamento m = medicamentoRepository.findById(d.getMedicamentoId())
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado: " + d.getMedicamentoId()));
            RecetaDetalle det = new RecetaDetalle();
            det.setMedicamento(m);
            det.setDosis(d.getDosis());
            det.setDuracion(d.getDuracion());
            det.setCantidad(d.getCantidad() != null && d.getCantidad() > 0 ? d.getCantidad() : 1);
            receta.agregarDetalle(det);
        }

        return recetaRepository.save(receta);
    }

    /**
     * La farmacia entrega la receta: descuenta el stock de cada medicamento
     * y marca la receta como ENTREGADA.
     */
    public Receta entregar(Long id) {
        Receta receta = buscarPorId(id);
        if (receta.getEstado() == EstadoReceta.ENTREGADA) {
            throw new RuntimeException("La receta ya fue entregada");
        }

        for (RecetaDetalle det : receta.getDetalles()) {
            Medicamento m = det.getMedicamento();
            int restante = m.getStockActual() - det.getCantidad();
            if (restante < 0) {
                throw new RuntimeException("Stock insuficiente de " + m.getNombre()
                    + " (disponible: " + m.getStockActual() + ", requerido: " + det.getCantidad() + ")");
            }
            m.setStockActual(restante);
            medicamentoRepository.save(m);
        }

        receta.setEstado(EstadoReceta.ENTREGADA);
        return recetaRepository.save(receta);
    }
}
