package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.CieDTO;
import edu.universidad.centromedico.service.CieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Búsqueda de códigos CIE-10 para la atención. Solo DOCTOR. */
@RestController
@RequestMapping("/api/cie")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
public class CieController {

    private final CieService cieService;

    @GetMapping
    public List<CieDTO> buscar(@RequestParam("q") String q) {
        if (q == null || q.trim().length() < 2) {
            return List.of();
        }
        return cieService.buscar(q.trim());
    }
}
