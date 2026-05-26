package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Receta;
import pe.edu.uni.centromedico.service.RecetaService;
import pe.edu.uni.centromedico.ui.panels.FarmaciaRecetasPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class FarmaciaController {

    private final FarmaciaRecetasPanel vista;
    private final RecetaService        recetaService;
    private List<Receta>               recetasCargadas;

    public FarmaciaController(FarmaciaRecetasPanel vista) {
        this.vista         = vista;
        this.recetaService = new RecetaService();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        recetasCargadas = recetaService.obtenerRecetasPendientes();
        mostrarEnTabla(recetasCargadas);
    }

    private void mostrarEnTabla(List<Receta> lista) {
        String[][] datos = new String[lista.size()][5];
        for (int i = 0; i < lista.size(); i++) {
            Receta r = lista.get(i);
            datos[i] = new String[]{
                String.valueOf(r.getId()),
                r.getNombrePaciente() != null ? r.getNombrePaciente() : "—",
                String.valueOf(r.getIdCita()),
                r.getDiagnostico() != null ? r.getDiagnostico() : "—",
                r.getEstado()
            };
        }
        vista.getTblRecetas().setModel(new DefaultTableModel(
            datos, new String[]{"ID Receta", "Paciente", "Cita", "Diagnóstico", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscarReceta().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getTxtBuscarReceta().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getBtnConfirmarEntrega().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::confirmarEntrega));
    }

    private void buscar() {
        String texto = vista.getTxtBuscarReceta().getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            mostrarEnTabla(recetasCargadas);
            return;
        }
        List<Receta> filtradas = recetasCargadas.stream()
            .filter(r -> {
                String paciente = r.getNombrePaciente() != null ? r.getNombrePaciente() : "";
                return paciente.toLowerCase().contains(texto)
                    || String.valueOf(r.getId()).contains(texto);
            })
            .toList();
        mostrarEnTabla(filtradas);
    }

    private void confirmarEntrega() {
        int fila = vista.getTblRecetas().getSelectedRow();
        if (fila < 0) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Selecciona una receta para confirmar la entrega.");
            return;
        }

        Object valor = vista.getTblRecetas().getValueAt(fila, 0);
        if (valor == null) {
            ErrorHandler.mostrarError(vista, "La fila seleccionada no tiene ID válido.");
            return;
        }
        int idReceta;
        try {
            idReceta = Integer.parseInt(valor.toString());
        } catch (NumberFormatException ex) {
            ErrorHandler.mostrarError(vista, "ID de receta inválido.");
            return;
        }

        if (!ErrorHandler.confirmar(vista, "Confirmar entrega",
                "¿Confirmar entrega de la receta #" + idReceta + "?")) return;

        if (recetaService.confirmarEntrega(idReceta)) {
            ErrorHandler.mostrarInfo(vista, "Éxito", "Entrega confirmada correctamente.");
        } else {
            ErrorHandler.mostrarError(vista,
                "Error al confirmar la entrega. Verifica el stock disponible.");
        }
        cargarDatos();
    }
}
