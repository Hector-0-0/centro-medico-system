package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/** Datos para registrar un paciente nuevo (crea usuario + estudiante). */
@Data
public class NuevoEstudianteRequest {

    // Código universitario UNI: 4 dígitos del año de ingreso + serie de dígitos + 1 letra. Ej: 202500154C
    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^\\d{4}\\d{3,}[A-Za-z]$",
        message = "El código debe ser un código UNI: año (4 dígitos) + serie de números + 1 letra. Ej: 202500154C")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
        message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    // DNI del paciente (8 dígitos), independiente del código UNI.
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La carrera es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
        message = "La carrera solo puede contener letras y espacios")
    private String carrera;

    @NotBlank(message = "El email es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@(uni\\.pe|uni\\.edu\\.pe)$",
        message = "El email debe terminar en @uni.pe o @uni.edu.pe")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
