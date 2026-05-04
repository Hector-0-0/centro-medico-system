package edu.universidad.centromedico.service;

import edu.universidad.centromedico.model.Especialidad;
import edu.universidad.centromedico.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio para especialidades médicas.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    public Especialidad buscarPorId(Long id) {
        return especialidadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con id: " + id));
    }

    public Especialidad registrar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    public Especialidad actualizar(Long id, Especialidad datos) {
        Especialidad especialidad = buscarPorId(id);
        especialidad.setNombre(datos.getNombre());
        especialidad.setDescripcion(datos.getDescripcion());
        return especialidadRepository.save(especialidad);
    }

    public void eliminar(Long id) {
        especialidadRepository.deleteById(id);
    }
}
