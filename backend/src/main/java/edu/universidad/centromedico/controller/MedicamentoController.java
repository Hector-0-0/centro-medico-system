package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.ActualizarStockRequest;
import edu.universidad.centromedico.dto.MedicamentoDTO;
import edu.universidad.centromedico.dto.MovimientoStockDTO;
import edu.universidad.centromedico.dto.NuevoMedicamentoRequest;
import edu.universidad.centromedico.service.MedicamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Medicamentos. Lectura: DOCTOR (Ver Stock) y FARMACIA (Gestión de Stock).
 * Alta y ajuste de stock: solo FARMACIA.
 */
@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    /** Inventario completo (Ver Stock / Gestión de Stock). */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'FARMACIA')")
    public List<MedicamentoDTO> listar() {
        return medicamentoService.listar();
    }

    /** Solo medicamentos con stock (para recetar en la atención). */
    @GetMapping("/con-stock")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<MedicamentoDTO> conStock() {
        return medicamentoService.conStock();
    }

    /** Movimientos de stock de un medicamento (auditoría). */
    @GetMapping("/{id}/movimientos")
    @PreAuthorize("hasAnyRole('DOCTOR', 'FARMACIA')")
    public List<MovimientoStockDTO> movimientos(@PathVariable String id) {
        return medicamentoService.movimientos(id);
    }

    /** Registrar un medicamento nuevo. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('FARMACIA')")
    public void crear(@Valid @RequestBody NuevoMedicamentoRequest req, Authentication auth) {
        medicamentoService.crear(req, auth.getName());
    }

    /** Ajustar el stock de un medicamento. */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('FARMACIA')")
    public ResponseEntity<Void> actualizarStock(@PathVariable String id,
                                                @Valid @RequestBody ActualizarStockRequest req,
                                                Authentication auth) {
        medicamentoService.actualizarStock(id, req.getStock(), auth.getName());
        return ResponseEntity.ok().build();
    }
}
