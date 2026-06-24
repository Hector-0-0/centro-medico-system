package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** Datos para registrar la atención de una cita (consulta médica). */
@Data
public class AtenderRequest {

    private String comentarios;

    @NotEmpty(message = "Debe agregar al menos un diagnóstico CIE")
    private List<Diagnostico> diagnosticos;

    /** Receta opcional. */
    private List<RecetaItem> receta;

    @Data
    public static class Diagnostico {
        @NotNull(message = "El código CIE es obligatorio")
        private Integer idCie;
        private String observacion;
    }

    @Data
    public static class RecetaItem {
        private String idMedicamento;
        private String dosis;
        private String duracion;
    }
}
