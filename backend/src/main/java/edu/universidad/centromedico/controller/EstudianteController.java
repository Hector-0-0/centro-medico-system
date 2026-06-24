package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.EstudianteDTO;
import edu.universidad.centromedico.dto.NuevoEstudianteRequest;
import edu.universidad.centromedico.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestión de pacientes (estudiantes). Solo ADMIN, igual que en el desktop.
 */
@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public List<EstudianteDTO> listar() {
        return estudianteService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstudianteDTO registrar(@Valid @RequestBody NuevoEstudianteRequest req) {
        return estudianteService.registrar(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
