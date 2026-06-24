package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.DisponibilidadDTO;
import edu.universidad.centromedico.dto.GuardarDisponibilidadRequest;
import edu.universidad.centromedico.dto.GuardarDisponibilidadResponse;
import edu.universidad.centromedico.service.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Disponibilidad del médico autenticado. Solo DOCTOR. */
@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @GetMapping
    public List<DisponibilidadDTO> listar(Authentication auth) {
        return disponibilidadService.listar(auth.getName());
    }

    @PostMapping
    public GuardarDisponibilidadResponse guardar(@RequestBody GuardarDisponibilidadRequest req,
                                                 Authentication auth) {
        return disponibilidadService.guardar(auth.getName(), req);
    }
}
