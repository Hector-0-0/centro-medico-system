package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Resultado de guardar la disponibilidad: cuántos días se aplicaron, cuántos se
 * rechazaron y el detalle (día + motivo) de cada rechazo para mostrarlo al médico.
 */
@Data
@AllArgsConstructor
public class GuardarDisponibilidadResponse {
    private int guardados;
    private int rechazados;
    private List<DiaRechazado> detalleRechazados;

    @Data
    @AllArgsConstructor
    public static class DiaRechazado {
        private String diaSemana;
        private String motivo;
    }
}
