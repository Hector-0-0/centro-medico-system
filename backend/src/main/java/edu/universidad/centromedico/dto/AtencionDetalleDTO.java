package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/** Detalle de la atención de una cita: diagnósticos CIE + comentarios + receta. */
@Data
@AllArgsConstructor
public class AtencionDetalleDTO {
    private boolean atendida;
    private String comentarios;
    private List<Diagnostico> diagnosticos;
    private Integer idReceta;

    @Data
    @AllArgsConstructor
    public static class Diagnostico {
        private String codigo;
        private String descripcion;
        private String observacion;
    }
}
