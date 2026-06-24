package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.LoginRequest;
import edu.universidad.centromedico.dto.LoginResponse;
import edu.universidad.centromedico.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoints de autenticación. /login es público; /me requiere token.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /** Devuelve el perfil (id, rol, nombre) del usuario autenticado. */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication auth) {
        return ResponseEntity.ok(authService.perfil(auth.getName()));
    }
}
