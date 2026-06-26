package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Campos editables de "Mi Perfil" del médico (consultorio y foto). */
@Data
public class ActualizarPerfilDoctorRequest {

    @NotBlank(message = "El consultorio es obligatorio")
    private String consultorio;

    /** Data URL base64 de la foto (opcional). null = sin cambios; "" = quitar. */
    private String foto;
}
