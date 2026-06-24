package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Un turno de 30 min de un médico (para ver horarios y agendar). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotDTO {
    private int id;
    private String idDoctor;
    private String nombreDoctor;
    private String especialidad;
    private String consultorio;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private boolean disponible;
}
