package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.ActualizarPerfilRequest;
import edu.universidad.centromedico.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** "Mi Perfil" del usuario autenticado (cualquier rol). */
@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public Map<String, Object> perfil(Authentication auth) {
        return perfilService.perfil(auth.getName());
    }

    /** Editar "Mi Perfil" (solo estudiante: email, carrera, nacimiento, foto). */
    @PutMapping
    public ResponseEntity<Map<String, Object>> actualizar(
            @Valid @RequestBody ActualizarPerfilRequest req, Authentication auth) {
        perfilService.actualizar(auth.getName(), req);
        return ResponseEntity.ok(perfilService.perfil(auth.getName()));
    }
}
