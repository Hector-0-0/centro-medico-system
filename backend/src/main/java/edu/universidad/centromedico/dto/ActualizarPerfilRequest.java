package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/** Campos editables de "Mi Perfil" del estudiante (email, carrera, nacimiento, foto). */
@Data
public class ActualizarPerfilRequest {

    @NotBlank(message = "El email es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@(uni\\.pe|uni\\.edu\\.pe)$",
        message = "El email debe terminar en @uni.pe o @uni.edu.pe")
    private String email;

    @NotBlank(message = "La carrera es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
        message = "La carrera solo puede contener letras y espacios")
    private String carrera;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    /** Data URL base64 de la foto (opcional). */
    private String foto;
}
