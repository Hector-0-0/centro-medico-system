package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** Datos para registrar un médico nuevo (crea usuario + doctor). */
@Data
public class NuevoDoctorRequest {

    // El código del médico es su DNI: 8 dígitos numéricos.
    @NotBlank(message = "El código (DNI) es obligatorio")
    @Pattern(regexp = "^\\d{8}$",
        message = "El código del médico debe ser un DNI de 8 dígitos")
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
