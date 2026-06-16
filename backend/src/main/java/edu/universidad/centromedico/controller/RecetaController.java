package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.RecetaDTO;
import edu.universidad.centromedico.model.Receta;
import edu.universidad.centromedico.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService service;

    @GetMapping
    public ResponseEntity<List<Receta>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Receta>> pendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Receta>> porPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Receta>> porMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(service.listarPorMedico(medicoId));
    }

    /** El médico emite la receta. */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Receta> emitir(@Valid @RequestBody RecetaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.emitir(dto));
    }

    /** La farmacia (recepción/admin) entrega la receta y descuenta stock. */
    @PutMapping("/{id}/entregar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Receta> entregar(@PathVariable Long id) {
        return ResponseEntity.ok(service.entregar(id));
    }
}
