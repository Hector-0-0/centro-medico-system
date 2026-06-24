package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** Datos para registrar un medicamento nuevo. */
@Data
public class NuevoMedicamentoRequest {

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9-]{3,15}$",
        message = "El código debe tener 3-15 caracteres: letras, números o guion")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ .]+$",
        message = "El nombre solo admite letras, números y espacios")
    private String nombre;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 10000, message = "El stock supera el máximo permitido (10000)")
    private Integer stock;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotNull(message = "La dosis (mg) es obligatoria")
    @Min(value = 1, message = "La dosis debe ser de al menos 1 mg")
    @Max(value = 10000, message = "La dosis supera el máximo permitido (10000 mg)")
    private Integer dosisMg;
}
