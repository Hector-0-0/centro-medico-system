package edu.universidad.centromedico.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** DTO para que un médico emita una receta con sus líneas de medicamentos. */
@Data
public class RecetaDTO {

    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El médico es obligatorio")
    private Long medicoId;

    private Long citaId;

    private String diagnostico;

    @NotNull(message = "La receta debe tener al menos un medicamento")
    private List<DetalleDTO> detalles;

    @Data
    public static class DetalleDTO {
        @NotNull(message = "El medicamento es obligatorio")
        private Long medicamentoId;
        private String dosis;
        private String duracion;
        private Integer cantidad = 1;
    }
}
