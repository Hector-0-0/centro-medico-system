package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.DoctorDTO;
import edu.universidad.centromedico.dto.NuevoDoctorRequest;
import edu.universidad.centromedico.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestión de médicos (doctores). Solo ADMIN, igual que en el desktop.
 */
@RestController
@RequestMapping("/api/doctores")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDTO> listar() {
        return doctorService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDTO registrar(@Valid @RequestBody NuevoDoctorRequest req) {
        return doctorService.registrar(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        doctorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
