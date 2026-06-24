package edu.universidad.centromedico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Médico — mapea la tabla `doctores` de la BD del desktop. Su id (id_usuario)
 * es a la vez la PK y la FK hacia `usuarios`.
 */
@Entity
@Table(name = "doctores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @Column(name = "id_usuario", length = 10)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(length = 50)
    private String consultorio;

    @Column(nullable = false)
    private boolean activo = true;
}
