package edu.universidad.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Una entrada del log de auditoría de movimientos de stock. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoStockDTO {
    private int id;
    private String idMedicamento;
    private String tipoMovimiento;   // ENTRADA | SALIDA
    private int cantidad;
    private int stockResultante;
    private String motivo;
    private String usuario;
    private String fecha;            // ISO
}
