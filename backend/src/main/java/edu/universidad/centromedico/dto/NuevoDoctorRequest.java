package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** Datos para registrar un médico nuevo (crea usuario + doctor). */
@Data
public class NuevoDoctorRequest {

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9]{3,10}$",
        message = "El código debe tener entre 3 y 10 caracteres, solo letras y números")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ. ]+$",
        message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "La especialidad es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
        message = "La especialidad solo puede contener letras y espacios")
    private String especialidad;

    @NotBlank(message = "El consultorio es obligatorio")
    private String consultorio;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
