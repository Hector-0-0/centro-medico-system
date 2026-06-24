package edu.universidad.centromedico.dto;

import edu.universidad.centromedico.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Datos de un médico para listar en la tabla. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private String id;
    private String nombre;
    private String especialidad;
    private String consultorio;
    private boolean activo;

    public static DoctorDTO de(Doctor d) {
        return new DoctorDTO(d.getId(), d.getNombre(), d.getEspecialidad(),
            d.getConsultorio(), d.isActivo());
    }
}
