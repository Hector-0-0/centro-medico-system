package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.AgendarDTO;
import edu.universidad.centromedico.dto.DisponibilidadDTO;
import edu.universidad.centromedico.model.Cita;
import edu.universidad.centromedico.model.Disponibilidad;
import edu.universidad.centromedico.service.DisponibilidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService service;

    /** Horarios disponibles (para que el paciente agende). */
    @GetMapping
    public ResponseEntity<List<Disponibilidad>> listar() {
        return ResponseEntity.ok(service.listarDisponibles());
    }

    /** Horarios de un médico (su agenda de disponibilidad). */
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Disponibilidad>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(service.listarPorMedico(medicoId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Disponibilidad> crear(@Valid @RequestBody DisponibilidadDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Disponibilidad> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody DisponibilidadDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /** Agenda una cita a partir de un horario disponible. */
    @PostMapping("/agendar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'PACIENTE')")
    public ResponseEntity<Cita> agendar(@Valid @RequestBody AgendarDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agendar(dto));
    }
}
