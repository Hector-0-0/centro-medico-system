package edu.universidad.centromedico.dto;

import lombok.Data;

import java.util.List;

/** Días a guardar en la disponibilidad del médico (con su rango horario). */
@Data
public class GuardarDisponibilidadRequest {

    private List<Dia> dias;

    @Data
    public static class Dia {
        private String diaSemana;
        private String horaInicio;
        private String horaFin;
        /** true = atiende ese día; false = desactivar (limpiar) ese día. */
        private boolean activo;
    }
}
