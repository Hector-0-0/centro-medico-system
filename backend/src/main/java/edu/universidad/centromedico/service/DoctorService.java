package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.DoctorDTO;
import edu.universidad.centromedico.dto.NuevoDoctorRequest;
import edu.universidad.centromedico.model.Doctor;
import edu.universidad.centromedico.model.Rol;
import edu.universidad.centromedico.model.Usuario;
import edu.universidad.centromedico.repository.DoctorRepository;
import edu.universidad.centromedico.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Gestión de médicos (tabla `doctores`). Replica el DoctorDAO del desktop:
 * listar no eliminados, registrar (usuario + doctor en una transacción, activo
 * por defecto) y eliminar con borrado lógico sobre `usuarios`.
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UsuarioRepository usuarioRepository;

    public List<DoctorDTO> listar() {
        return doctorRepository.findAllActivos().stream()
            .map(DoctorDTO::de)
            .toList();
    }

    @Transactional
    public DoctorDTO registrar(NuevoDoctorRequest req) {
        if (usuarioRepository.existsById(req.getId())) {
            throw new RuntimeException("El código ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setId(req.getId());
        usuario.setPassword(req.getPassword());
        usuario.setRol(Rol.DOCTOR);
        usuario.setEliminado(false);
        usuarioRepository.save(usuario);

        Doctor doctor = new Doctor();
        doctor.setId(req.getId());
        doctor.setNombre(req.getNombre());
        doctor.setEspecialidad(req.getEspecialidad());
        doctor.setConsultorio(req.getConsultorio());
        doctor.setActivo(true);
        doctorRepository.save(doctor);

        return DoctorDTO.de(doctor);
    }

    /** Borrado lógico: marca el usuario como eliminado (preserva historial). */
    @Transactional
    public void eliminar(String id) {
        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(id)
            .orElseThrow(() -> new RuntimeException("El médico no existe"));
        usuario.setEliminado(true);
        usuarioRepository.save(usuario);
    }
}
