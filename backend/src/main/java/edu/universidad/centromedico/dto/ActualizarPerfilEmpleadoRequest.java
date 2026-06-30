package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Campos editables de "Mi Perfil" de los empleados (ADMIN y FARMACIA): contacto, foto y credenciales. */
@Data
public class ActualizarPerfilEmpleadoRequest {

    @Pattern(regexp = "^$|^\\d{6,15}$", message = "El teléfono debe tener entre 6 y 15 dígitos")
    private String telefono;

    @Pattern(regexp = "^$|^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "El email no es válido")
    private String email;

    /** Verificación obligatoria: contraseña actual para confirmar cualquier cambio. */
    @NotBlank(message = "Debes ingresar tu contraseña actual para guardar los cambios")
    private String passwordActual;

    /** Nueva contraseña (opcional). Si viene, debe tener al menos 4 caracteres. */
    @Size(min = 4, message = "La nueva contraseña debe tener al menos 4 caracteres")
    private String passwordNueva;

    /** Data URL base64 de la foto (opcional). null = sin cambios; "" = quitar. */
    private String foto;
}
