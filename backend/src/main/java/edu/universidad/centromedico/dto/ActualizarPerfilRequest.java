package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Campos editables de "Mi Perfil" del estudiante (contacto, datos médicos y
 * credenciales). La carrera y la fecha de nacimiento NO se editan aquí: las fija
 * el administrador al registrar al paciente (igual que en un sistema real).
 */
@Data
public class ActualizarPerfilRequest {

    @NotBlank(message = "El email es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@(uni\\.pe|uni\\.edu\\.pe)$",
        message = "El email debe terminar en @uni.pe o @uni.edu.pe")
    private String email;

    // Datos de contacto / médicos (opcionales). El teléfono admite 6-15 dígitos o vacío.
    @Pattern(regexp = "^$|^\\d{6,15}$", message = "El teléfono debe tener entre 6 y 15 dígitos")
    private String telefono;

    @Size(max = 255, message = "Las alergias no pueden superar 255 caracteres")
    private String alergias;

    @Pattern(regexp = "^$|^(O|A|B|AB)[+-]$", message = "El tipo de sangre no es válido")
    private String tipoSangre;

    @Size(max = 150, message = "El contacto de emergencia no puede superar 150 caracteres")
    private String contactoEmergencia;

    /** Verificación obligatoria: contraseña actual para confirmar cualquier cambio. */
    @NotBlank(message = "Debes ingresar tu contraseña actual para guardar los cambios")
    private String passwordActual;

    /** Nueva contraseña (opcional). Si viene, debe tener al menos 4 caracteres. */
    @Size(min = 4, message = "La nueva contraseña debe tener al menos 4 caracteres")
    private String passwordNueva;

    /** Data URL base64 de la foto (opcional). null = sin cambios; "" = quitar. */
    private String foto;
}
