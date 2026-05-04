package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.PacienteDTO;
import edu.universidad.centromedico.model.Paciente;
import edu.universidad.centromedico.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio para la gestión de pacientes.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));
    }

    public Paciente buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + dni));
    }

    public List<Paciente> buscar(String termino) {
        return pacienteRepository
            .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(termino, termino);
    }

    public Paciente registrar(PacienteDTO dto) {
        if (pacienteRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("Ya existe un paciente con el DNI: " + dto.getDni());
        }

        Paciente paciente = new Paciente();
        mapearDesdeDTO(dto, paciente);
        return pacienteRepository.save(paciente);
    }

    public Paciente actualizar(Long id, PacienteDTO dto) {
        Paciente paciente = buscarPorId(id);

        // Verificar DNI duplicado solo si cambió
        if (!paciente.getDni().equals(dto.getDni()) && pacienteRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("Ya existe un paciente con el DNI: " + dto.getDni());
        }

        mapearDesdeDTO(dto, paciente);
        return pacienteRepository.save(paciente);
    }

    public void eliminar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente no encontrado con id: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    private void mapearDesdeDTO(PacienteDTO dto, Paciente paciente) {
        paciente.setDni(dto.getDni());
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setGrupoSanguineo(dto.getGrupoSanguineo());
        paciente.setAlergias(dto.getAlergias());
    }
}
