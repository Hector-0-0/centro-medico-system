package edu.universidad.centromedico.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cuenta de acceso al sistema — mapea la tabla `usuarios` de la BD del desktop
 * (SQL Server). El id (ej. ADM001, D001, U001, FAR001) es la clave primaria y
 * también el "usuario" con el que se inicia sesión. La contraseña se guarda en
 * texto plano, igual que en el desktop.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @Column(length = 10)
    private String id;

    /** Texto plano (igual que el desktop). Nunca se expone en respuestas JSON. */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(nullable = false)
    private boolean eliminado = false;
}
