package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Receta;
import pe.edu.uni.centromedico.service.RecetaService;
import pe.edu.uni.centromedico.ui.panels.FarmaciaRecetasPanel;

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
                r.getNombrePaciente(),
                String.valueOf(r.getIdCita()),
                r.getDiagnostico(),
                r.getEstado()
            };
        }
        vista.getTblRecetas().setModel(new DefaultTableModel(
            datos, new String[]{"ID Receta", "Paciente", "Cita", "Diagnóstico", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscarReceta().addActionListener(e -> buscar());
        vista.getTxtBuscarReceta().addActionListener(e -> buscar());
        vista.getBtnConfirmarEntrega().addActionListener(e -> confirmarEntrega());
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
            javax.swing.JOptionPane.showMessageDialog(vista,
                "Selecciona una receta para confirmar la entrega.",
                "Sin selección", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idReceta = Integer.parseInt(
            vista.getTblRecetas().getValueAt(fila, 0).toString());

        int confirmar = javax.swing.JOptionPane.showConfirmDialog(vista,
            "¿Confirmar entrega de la receta #" + idReceta + "?",
            "Confirmar entrega", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirmar == javax.swing.JOptionPane.YES_OPTION) {
            boolean ok = recetaService.confirmarEntrega(idReceta);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "Entrega confirmada correctamente.",
                    "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "Error al confirmar la entrega. Intenta de nuevo.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
