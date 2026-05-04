package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Datos de entrada para el endpoint de login. */
@Data
public class LoginRequest {
    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
