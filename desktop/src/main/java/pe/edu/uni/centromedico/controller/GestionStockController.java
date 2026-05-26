package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Medicamento;
import pe.edu.uni.centromedico.service.MedicamentoService;
import pe.edu.uni.centromedico.ui.dialogs.NuevoMedicamentoDialog;
import pe.edu.uni.centromedico.ui.panels.GestionStockPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class GestionStockController {

    private final GestionStockPanel    vista;
    private final MedicamentoService   medicamentoService;

    public GestionStockController(GestionStockPanel vista) {
        this.vista              = vista;
        this.medicamentoService = new MedicamentoService();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        cargarTabla(medicamentoService.obtenerTodos());
    }

    private void cargarTabla(List<Medicamento> lista) {
        String[][] datos = new String[lista.size()][4];
        for (int i = 0; i < lista.size(); i++) {
            Medicamento m = lista.get(i);
            String estado = m.getStock() == 0  ? "AGOTADO"
                          : m.getStock() < 20  ? "BAJO" : "NORMAL";
            datos[i] = new String[]{
                m.getId(), m.getNombre(),
                String.valueOf(m.getStock()), estado
            };
        }
        vista.getTblStock().setModel(new DefaultTableModel(
            datos, new String[]{"Código", "Nombre", "Stock", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscarStock().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getTxtBuscarStock().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::buscar));

        vista.getTblStock().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    ErrorHandler.ejecutarSeguro(vista, GestionStockController.this::ajustarStock);
            }
        });

        vista.getBtnAgregarStock().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::abrirDialogoNuevo));
    }

    private void abrirDialogoNuevo() {
        java.awt.Frame ventana = (java.awt.Frame)
            javax.swing.SwingUtilities.getWindowAncestor(vista);
        new NuevoMedicamentoDialog(ventana, true).setVisible(true);
        cargarDatos();
    }

    private void buscar() {
        cargarTabla(medicamentoService.buscar(vista.getTxtBuscarStock().getText()));
    }

    private void ajustarStock() {
        int fila = vista.getTblStock().getSelectedRow();
        if (fila < 0) return;

        String id     = (String) vista.getTblStock().getValueAt(fila, 0);
        String nombre = (String) vista.getTblStock().getValueAt(fila, 1);
        String input  = javax.swing.JOptionPane.showInputDialog(vista,
            "Nuevo stock para \"" + nombre + "\":",
            "Ajustar Stock", javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isBlank()) return;

        try {
            int nuevoStock = Integer.parseInt(input.trim());
            if (medicamentoService.actualizarStock(id, nuevoStock)) {
                cargarDatos();
            } else {
                ErrorHandler.mostrarError(vista, "Stock inválido o error al actualizar.");
            }
        } catch (NumberFormatException ex) {
            ErrorHandler.mostrarError(vista, "Ingresa un número entero ≥ 0.");
        }
    }
}
