package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.AgendarRequest;
import edu.universidad.centromedico.dto.CitaDTO;
import edu.universidad.centromedico.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Citas: listado global (ADMIN) y citas propias del médico (DOCTOR).
 */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    /** Todas las citas — panel "Todas las Citas" del ADMIN. */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CitaDTO> listar() {
        return citaService.obtenerTodas();
    }

    /** Citas del médico autenticado — panel "Mis Citas" del DOCTOR. */
    @GetMapping("/mias")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<CitaDTO> mias(Authentication auth) {
        return citaService.obtenerPorDoctor(auth.getName());
    }

    /** Citas del estudiante autenticado — panel "Mis Citas" del ESTUDIANTE. */
    @GetMapping("/estudiante")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public List<CitaDTO> deEstudiante(Authentication auth) {
        return citaService.obtenerPorEstudiante(auth.getName());
    }

    /** Agendar una cita en un slot disponible — ESTUDIANTE. */
    @PostMapping("/agendar")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<Void> agendar(@Valid @RequestBody AgendarRequest req, Authentication auth) {
        citaService.agendar(auth.getName(), req.getIdSlot(), req.getMotivo());
        return ResponseEntity.ok().build();
    }
}
