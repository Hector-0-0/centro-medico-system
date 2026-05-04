package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.HistorialDTO;
import edu.universidad.centromedico.model.HistorialMedico;
import edu.universidad.centromedico.service.HistorialMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historiales")
@RequiredArgsConstructor
public class HistorialMedicoController {

    private final HistorialMedicoService historialService;

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HistorialMedico>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historialService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<HistorialMedico> buscarPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(historialService.buscarPorCita(citaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialMedico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<HistorialMedico> registrar(@Valid @RequestBody HistorialDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historialService.registrar(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<HistorialMedico> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody HistorialDTO dto) {
        return ResponseEntity.ok(historialService.actualizar(id, dto));
    }
}
