package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** DTO para crear o actualizar un médico. */
@Data
public class MedicoDTO {

    @NotBlank(message = "El CMP es obligatorio")
    private String cmp;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    private String telefono;

    @NotNull(message = "La especialidad es obligatoria")
    private Long especialidadId;
}
