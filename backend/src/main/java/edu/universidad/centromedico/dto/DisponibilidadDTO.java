package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

/** DTO para crear o actualizar un horario de disponibilidad. */
@Data
public class DisponibilidadDTO {

    @NotNull(message = "El médico es obligatorio")
    private Long medicoId;

    @NotBlank(message = "El día de la semana es obligatorio")
    private String diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private String consultorio;
}
