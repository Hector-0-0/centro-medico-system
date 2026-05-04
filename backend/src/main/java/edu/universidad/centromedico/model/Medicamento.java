package edu.universidad.centromedico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Medicamento disponible en la farmacia del centro médico.
 */
@Entity
@Table(name = "medicamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "principio_activo", length = 150)
    private String principioActivo;

    @Column(length = 100)
    private String presentacion;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 10;

    @Column(length = 50)
    private String unidad;

    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Indica si el stock está por debajo del mínimo.
     */
    @Transient
    public boolean isStockBajo() {
        return stockActual <= stockMinimo;
    }
}
