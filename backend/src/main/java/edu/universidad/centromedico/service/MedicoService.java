package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.MedicoDTO;
import edu.universidad.centromedico.model.Especialidad;
import edu.universidad.centromedico.model.Medico;
import edu.universidad.centromedico.repository.EspecialidadRepository;
import edu.universidad.centromedico.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio para la gestión de médicos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));
    }

    public List<Medico> buscarPorEspecialidad(Long especialidadId) {
        return medicoRepository.findByEspecialidadId(especialidadId);
    }

    public List<Medico> buscar(String termino) {
        return medicoRepository
            .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(termino, termino);
    }

    public Medico registrar(MedicoDTO dto) {
        if (medicoRepository.existsByCmp(dto.getCmp())) {
            throw new RuntimeException("Ya existe un médico con CMP: " + dto.getCmp());
        }

        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        Medico medico = new Medico();
        mapearDesdeDTO(dto, medico, especialidad);
        return medicoRepository.save(medico);
    }

    public Medico actualizar(Long id, MedicoDTO dto) {
        Medico medico = buscarPorId(id);
        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        mapearDesdeDTO(dto, medico, especialidad);
        return medicoRepository.save(medico);
    }

    public void eliminar(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new RuntimeException("Médico no encontrado con id: " + id);
        }
        medicoRepository.deleteById(id);
    }

    private void mapearDesdeDTO(MedicoDTO dto, Medico medico, Especialidad especialidad) {
        medico.setCmp(dto.getCmp());
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setTelefono(dto.getTelefono());
        medico.setEspecialidad(especialidad);
    }
}
