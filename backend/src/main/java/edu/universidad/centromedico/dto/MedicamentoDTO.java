package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Medicamento del inventario. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoDTO {
    private String id;
    private String nombre;
    private int stock;
    private String tipo;
    private Integer dosisMg;
}
