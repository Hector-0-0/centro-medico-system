package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Código CIE-10 (catálogo de diagnósticos). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CieDTO {
    private int id;
    private String codigo;
    private String descripcion;
}
