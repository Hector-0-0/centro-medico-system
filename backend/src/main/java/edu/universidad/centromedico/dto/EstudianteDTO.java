package edu.universidad.centromedico.dto;

import edu.universidad.centromedico.model.Estudiante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

/** Datos de un paciente para listar en la tabla. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteDTO {
    private String id;
    private String nombre;
    private String dni;
    private Integer edad;
    private LocalDate fechaNacimiento;
    private String carrera;
    private String email;

    public static EstudianteDTO de(Estudiante e) {
        return new EstudianteDTO(
            e.getId(), e.getNombre(), e.getDni(), edadDe(e), e.getFechaNacimiento(), e.getCarrera(), e.getEmail());
    }

    /** La edad se deriva de la fecha de nacimiento; cae a la columna `edad` si no hay fecha (filas del desktop). */
    public static Integer edadDe(Estudiante e) {
        if (e.getFechaNacimiento() != null) {
            return Period.between(e.getFechaNacimiento(), LocalDate.now()).getYears();
        }
        return e.getEdad();
    }
}
