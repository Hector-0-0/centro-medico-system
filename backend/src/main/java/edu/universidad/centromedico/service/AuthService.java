package edu.universidad.centromedico.service;

import edu.universidad.centromedico.config.JwtService;
import edu.universidad.centromedico.dto.LoginRequest;
import edu.universidad.centromedico.dto.LoginResponse;
import edu.universidad.centromedico.model.Rol;
import edu.universidad.centromedico.model.Usuario;
import edu.universidad.centromedico.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación: login y registro de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Valida las credenciales y devuelve un token JWT si son correctas.
     */
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        if (!usuario.getActivo()) {
            throw new RuntimeException("La cuenta está desactivada");
        }

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRol().name());
        return new LoginResponse(token, usuario.getUsername(), usuario.getRol().name(), usuario.getId());
    }

    /**
     * Registra un nuevo usuario en el sistema (uso interno / admin).
     */
    public Usuario registrar(String username, String rawPassword, String email, Rol rol) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setEmail(email);
        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }
}
