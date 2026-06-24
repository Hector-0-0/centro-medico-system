package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/** Datos para registrar un paciente nuevo (crea usuario + estudiante). */
@Data
public class NuevoEstudianteRequest {

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9]{3,10}$",
        message = "El código debe tener entre 3 y 10 caracteres, solo letras y números")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
        message = "El nombre solo puede contener letras y espacios")
    private String nombre;

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
