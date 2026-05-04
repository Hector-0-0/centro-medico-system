package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.model.Medicamento;
import edu.universidad.centromedico.service.MedicamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @GetMapping
    public ResponseEntity<List<Medicamento>> listar() {
        return ResponseEntity.ok(medicamentoService.listarActivos());
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Medicamento>> conStockBajo() {
        return ResponseEntity.ok(medicamentoService.conStockBajo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicamentoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Medicamento> registrar(@RequestBody Medicamento medicamento) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(medicamentoService.registrar(medicamento));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Medicamento> actualizar(@PathVariable Long id,
                                                  @RequestBody Medicamento datos) {
        return ResponseEntity.ok(medicamentoService.actualizar(id, datos));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Medicamento> actualizarStock(@PathVariable Long id,
                                                       @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(medicamentoService.actualizarStock(id, body.get("cantidad")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        medicamentoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
