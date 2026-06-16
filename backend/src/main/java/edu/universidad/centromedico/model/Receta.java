package edu.universidad.centromedico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Receta médica emitida por un médico al atender a un paciente.
 * La farmacia la entrega y al hacerlo descuenta el stock de los medicamentos.
 */
@Entity
@Table(name = "recetas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(length = 400)
    private String diagnostico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReceta estado = EstadoReceta.PENDIENTE;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecetaDetalle> detalles = new ArrayList<>();

    public void agregarDetalle(RecetaDetalle detalle) {
        detalle.setReceta(this);
        this.detalles.add(detalle);
    }
}
