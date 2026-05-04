package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.LoginRequest;
import edu.universidad.centromedico.dto.LoginResponse;
import edu.universidad.centromedico.model.Paciente;
import edu.universidad.centromedico.model.Usuario;
import edu.universidad.centromedico.repository.MedicoRepository;
import edu.universidad.centromedico.repository.PacienteRepository;
import edu.universidad.centromedico.repository.UsuarioRepository;
import edu.universidad.centromedico.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Endpoints de autenticación. Rutas públicas — no requieren token.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService       authService;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository  medicoRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Devuelve el perfil del usuario autenticado, incluyendo su pacienteId o medicoId
     * para que el frontend pueda filtrar datos propios.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication auth) {
        String username = auth.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("id",       usuario.getId());
        perfil.put("username", usuario.getUsername());
        perfil.put("email",    usuario.getEmail());
        perfil.put("rol",      usuario.getRol().name());

        // Si es paciente, adjuntar su pacienteId
        pacienteRepository.findByUsuarioId(usuario.getId())
            .ifPresent(p -> {
                perfil.put("pacienteId",     p.getId());
                perfil.put("nombre",         p.getNombre());
                perfil.put("apellido",       p.getApellido());
                perfil.put("grupoSanguineo", p.getGrupoSanguineo());
                perfil.put("alergias",       p.getAlergias());
            });

        // Si es médico, adjuntar su medicoId
        medicoRepository.findAll().stream()
            .filter(m -> m.getUsuario() != null && m.getUsuario().getId().equals(usuario.getId()))
            .findFirst()
            .ifPresent(m -> {
                perfil.put("medicoId",    m.getId());
                perfil.put("nombre",      m.getNombre());
                perfil.put("apellido",    m.getApellido());
                perfil.put("especialidad",m.getEspecialidad() != null ? m.getEspecialidad().getNombre() : null);
            });

        return ResponseEntity.ok(perfil);
    }
}