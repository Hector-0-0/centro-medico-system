package edu.universidad.centromedico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Especialidad médica disponible en el centro.
 */
@Entity
@Table(name = "especialidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 300)
    private String descripcion;

    /** Evita la recursión infinita Medico → Especialidad → medicos → ... al serializar JSON. */
    @OneToMany(mappedBy = "especialidad", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Medico> medicos;
}
