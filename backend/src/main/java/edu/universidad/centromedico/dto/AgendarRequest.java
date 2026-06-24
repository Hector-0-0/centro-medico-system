package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Datos para agendar una cita en un slot disponible. */
@Data
public class AgendarRequest {

    @NotNull(message = "El horario es obligatorio")
    private Integer idSlot;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
}
