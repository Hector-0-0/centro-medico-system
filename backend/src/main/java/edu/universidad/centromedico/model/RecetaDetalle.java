package edu.universidad.centromedico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Línea de una receta: un medicamento con su dosis, duración y cantidad. */
@Entity
@Table(name = "receta_detalles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    @JsonIgnore
    private Receta receta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    @Column(length = 100)
    private String dosis;

    @Column(length = 100)
    private String duracion;

    @Column(nullable = false)
    private Integer cantidad = 1;
}
