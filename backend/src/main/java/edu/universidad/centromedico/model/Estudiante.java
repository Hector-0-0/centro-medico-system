package edu.universidad.centromedico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Paciente del centro médico — mapea la tabla `estudiantes` de la BD del
 * desktop. Su id (id_usuario) es a la vez la PK y la FK hacia `usuarios`.
 */
@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {

    @Id
    @Column(name = "id_usuario", length = 10)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    /** DNI del paciente (8 dígitos). Distinto del código UNI, que es la PK. */
    @Column(length = 8)
    private String dni;

    private Integer edad;

    /** Fecha de nacimiento; la edad se calcula a partir de ella (la columna `edad` se conserva por compatibilidad con el desktop). */
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(length = 100)
    private String carrera;

    @Column(length = 100)
    private String email;

    /** Foto de perfil como data URL base64 (puede ser grande). */
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String foto;
}
