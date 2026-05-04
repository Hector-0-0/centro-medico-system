package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** DTO para registrar el historial médico de una cita. */
@Data
public class HistorialDTO {

    @NotNull(message = "La cita es obligatoria")
    private Long citaId;

    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnostico;

    private String tratamiento;
    private String receta;
    private String observaciones;
}
