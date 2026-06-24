package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.RecetaPendienteDTO;
import edu.universidad.centromedico.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Recetas pendientes y entrega. Solo FARMACIA. */
@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('FARMACIA')")
public class RecetaController {

    private final RecetaService recetaService;

    @GetMapping("/pendientes")
    public List<RecetaPendienteDTO> pendientes() {
        return recetaService.pendientes();
    }

    @PostMapping("/{id}/entregar")
    public ResponseEntity<Void> entregar(@PathVariable int id, Authentication auth) {
        recetaService.entregar(id, auth.getName());
        return ResponseEntity.ok().build();
    }
}
