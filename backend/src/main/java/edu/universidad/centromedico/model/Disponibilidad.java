package edu.universidad.centromedico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Horario de disponibilidad de un médico (bloque semanal recurrente).
 * Sirve tanto para que el médico gestione su agenda como para que el
 * paciente vea los "Horarios Disponibles" y agende una cita.
 */
@Entity
@Table(name = "disponibilidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    /** LUNES, MARTES, ... DOMINGO */
    @Column(name = "dia_semana", nullable = false, length = 12)
    private String diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(length = 50)
    private String consultorio;

    @Column(nullable = false)
    private Boolean activo = true;
}
