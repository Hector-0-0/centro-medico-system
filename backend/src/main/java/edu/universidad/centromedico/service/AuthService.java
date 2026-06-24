package edu.universidad.centromedico.service;

import edu.universidad.centromedico.config.JwtService;
import edu.universidad.centromedico.dto.LoginRequest;
import edu.universidad.centromedico.dto.LoginResponse;
import edu.universidad.centromedico.model.Usuario;
import edu.universidad.centromedico.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servicio de autenticación contra la tabla `usuarios` de la BD del desktop
 * (SQL Server). El login compara la contraseña en TEXTO PLANO, exactamente
 * igual que el UsuarioDAO de la app de escritorio.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final JdbcTemplate jdbc;

    /** Valida las credenciales (texto plano) y devuelve un token JWT. */
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(usuario.getId(), usuario.getRol().name());
        return new LoginResponse(token, usuario.getId(), usuario.getRol().name(), resolverNombre(usuario));
    }

    /** Perfil del usuario autenticado (para GET /api/auth/me). */
    public Map<String, Object> perfil(String id) {
        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> perfil = new LinkedHashMap<>();
        perfil.put("id",       usuario.getId());
        perfil.put("username", usuario.getId());
        perfil.put("rol",      usuario.getRol().name());
        perfil.put("nombre",   resolverNombre(usuario));
        return perfil;
    }

    /** Busca el nombre legible en la tabla de perfil según el rol. */
    private String resolverNombre(Usuario usuario) {
        String tabla = switch (usuario.getRol()) {
            case ESTUDIANTE -> "estudiantes";
            case DOCTOR     -> "doctores";
            case ADMIN      -> "administradores";
            case FARMACIA   -> "farmacia_usuarios";
        };
        try {
            return jdbc.queryForObject(
                "SELECT nombre FROM " + tabla + " WHERE id_usuario = ?",
                String.class, usuario.getId());
        } catch (Exception e) {
            return usuario.getId();
        }
    }
}
