package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Una receta pendiente de entrega (vista de farmacia). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaPendienteDTO {
    private int id;
    private String nombrePaciente;
    private int idCita;
    private String diagnostico;
    private String estado;
}
