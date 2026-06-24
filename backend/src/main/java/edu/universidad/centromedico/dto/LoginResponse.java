package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Respuesta del endpoint de login con el token JWT. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    /** Id del usuario que también funciona como nombre de usuario (ej. ADM001). */
    private String username;
    private String rol;
    /** Nombre legible de la persona (estudiante, doctor, admin o farmacéutico). */
    private String nombre;
}
