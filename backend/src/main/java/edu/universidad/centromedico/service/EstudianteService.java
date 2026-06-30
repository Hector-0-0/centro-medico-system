package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.EstudianteDTO;
import edu.universidad.centromedico.dto.NuevoEstudianteRequest;
import edu.universidad.centromedico.model.Estudiante;
import edu.universidad.centromedico.model.Rol;
import edu.universidad.centromedico.model.Usuario;
import edu.universidad.centromedico.repository.EstudianteRepository;
import edu.universidad.centromedico.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Gestión de pacientes (tabla `estudiantes`). Replica el EstudianteDAO del
 * desktop: listar no eliminados, registrar (usuario + estudiante en una
 * transacción) y eliminar con borrado lógico sobre `usuarios`.
 */
@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;

    public List<EstudianteDTO> listar() {
        return estudianteRepository.findAllActivos().stream()
            .map(EstudianteDTO::de)
            .toList();
    }

    @Transactional
    public EstudianteDTO registrar(NuevoEstudianteRequest req) {
        if (usuarioRepository.existsById(req.getId())) {
            throw new RuntimeException("El código ya existe");
        }
        if (estudianteRepository.existsByDni(req.getDni())) {
            throw new RuntimeException("El DNI ya está registrado para otro paciente");
        }
        edu.universidad.centromedico.model.Catalogos.validarCarrera(req.getCarrera());

        int edad = Period.between(req.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 14 || edad > 100) {
            throw new RuntimeException("El paciente debe tener entre 14 y 100 años");
        }

        Usuario usuario = new Usuario();
        usuario.setId(req.getId());
        usuario.setPassword(req.getPassword());
        usuario.setRol(Rol.ESTUDIANTE);
        usuario.setEliminado(false);
        usuarioRepository.save(usuario);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(req.getId());
        estudiante.setNombre(req.getNombre());
        estudiante.setDni(req.getDni());
        estudiante.setFechaNacimiento(req.getFechaNacimiento());
        estudiante.setEdad(edad); // se guarda calculada por compatibilidad con el desktop
        estudiante.setCarrera(req.getCarrera());
        estudiante.setEmail(req.getEmail());
        estudianteRepository.save(estudiante);

        return EstudianteDTO.de(estudiante);
    }

    /** Borrado lógico: marca el usuario como eliminado (preserva historial). */
    @Transactional
    public void eliminar(String id) {
        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(id)
            .orElseThrow(() -> new RuntimeException("El paciente no existe"));
        usuario.setEliminado(true);
        usuarioRepository.save(usuario);
    }
}
