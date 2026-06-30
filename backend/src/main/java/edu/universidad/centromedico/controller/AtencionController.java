package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.AtencionDetalleDTO;
import edu.universidad.centromedico.dto.AtenderRequest;
import edu.universidad.centromedico.service.AtencionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Registro de la atención de una cita. Solo DOCTOR (la cita debe ser suya). */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class AtencionController {

    private final AtencionService atencionService;

    @PostMapping("/{idCita}/atender")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> atender(@PathVariable int idCita,
                                        @Valid @RequestBody AtenderRequest req,
                                        Authentication auth) {
        atencionService.atender(idCita, auth.getName(), req);
        return ResponseEntity.ok().build();
    }

    /** Detalle de la atención de una cita propia (estudiante). */
    @GetMapping("/{idCita}/atencion")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public AtencionDetalleDTO detalle(@PathVariable int idCita, Authentication auth) {
        return atencionService.detalle(idCita, auth.getName());
    }

    /** Descargar PDF de la receta de una cita propia (estudiante). */
    @GetMapping("/{idCita}/receta-pdf")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<byte[]> recetaPdf(@PathVariable int idCita, Authentication auth) {
        byte[] pdf = atencionService.generarPdfReceta(idCita, auth.getName());
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"receta-cita-" + idCita + ".pdf\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}
