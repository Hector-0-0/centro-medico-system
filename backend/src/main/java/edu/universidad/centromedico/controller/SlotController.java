package edu.universidad.centromedico.controller;

import edu.universidad.centromedico.dto.SlotDTO;
import edu.universidad.centromedico.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Horarios (slots) disponibles para que el estudiante agende. Solo ESTUDIANTE. */
@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ESTUDIANTE')")
public class SlotController {

    private final SlotService slotService;

    @GetMapping
    public List<SlotDTO> listar() {
        return slotService.listar();
    }
}
