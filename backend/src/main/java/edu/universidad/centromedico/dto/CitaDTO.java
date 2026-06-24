package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Una cita con los datos de paciente, médico y horario (para listar). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {
    private int id;
    private String idEstudiante;
    private String nombreEstudiante;
    private String idDoctor;
    private String nombreDoctor;
    private String especialidad;
    private String motivo;
    private String estado;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
}
