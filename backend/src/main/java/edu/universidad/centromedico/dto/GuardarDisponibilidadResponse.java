package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Resultado de guardar la disponibilidad: cuántos días se guardaron y cuántos se rechazaron. */
@Data
@AllArgsConstructor
public class GuardarDisponibilidadResponse {
    private int guardados;
    private int rechazados;
}
