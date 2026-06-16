package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** DTO para agendar una cita a partir de un horario de disponibilidad. */
@Data
public class AgendarDTO {

    @NotNull(message = "El horario es obligatorio")
    private Long disponibilidadId;

    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String motivo;
}
