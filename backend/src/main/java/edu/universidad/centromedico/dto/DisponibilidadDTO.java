package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Un rango de disponibilidad del médico. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadDTO {
    private int id;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
}
