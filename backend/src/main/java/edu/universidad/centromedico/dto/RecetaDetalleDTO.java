package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecetaDetalleDTO {
    private int id;
    private String nombrePaciente;
    private int idCita;
    private String diagnostico;
    private String estado;
    private List<Item> items;

    @Data
    @AllArgsConstructor
    public static class Item {
        private String idMedicamento;
        private String nombre;
        private String dosis;
        private String duracion;
        private int stockActual;
        private int stockDespues;
    }
}
