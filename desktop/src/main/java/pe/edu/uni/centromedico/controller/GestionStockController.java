package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.models.Medicamento;
import pe.edu.uni.centromedico.ui.dialogs.NuevoMedicamentoDialog;
import pe.edu.uni.centromedico.ui.panels.GestionStockPanel;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class GestionStockController {

    private final GestionStockPanel vista;
    private final MedicamentoDAO    medicamentoDAO;

    public GestionStockController(GestionStockPanel vista) {
        this.vista          = vista;
        this.medicamentoDAO = new MedicamentoDAO();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        cargarTabla(medicamentoDAO.obtenerTodos());
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
        vista.getBtnBuscarStock().addActionListener(e -> buscar());
        vista.getTxtBuscarStock().addActionListener(e -> buscar());

        // Doble-click en fila → ajustar stock del medicamento seleccionado
        vista.getTblStock().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) ajustarStock();
            }
        });

        // Botón "+ Agregar" → nuevo medicamento
        vista.getBtnAgregarStock().addActionListener(e -> {
            java.awt.Frame ventana = (java.awt.Frame)
                javax.swing.SwingUtilities.getWindowAncestor(vista);
            new NuevoMedicamentoDialog(ventana, true).setVisible(true);
            cargarDatos();
        });
    }

    private void buscar() {
        String texto = vista.getTxtBuscarStock().getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarDatos(); return; }
        List<Medicamento> filtrados = medicamentoDAO.obtenerTodos().stream()
            .filter(m -> m.getNombre().toLowerCase().contains(texto)
                      || m.getId().toLowerCase().contains(texto))
            .toList();
        cargarTabla(filtrados);
    }

    private void ajustarStock() {
        int fila = vista.getTblStock().getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                "Haz doble-click en un medicamento para ajustar su stock.",
                "Sin selección", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id     = (String) vista.getTblStock().getValueAt(fila, 0);
        String nombre = (String) vista.getTblStock().getValueAt(fila, 1);
        String input  = javax.swing.JOptionPane.showInputDialog(vista,
            "Nuevo stock para \"" + nombre + "\":",
            "Ajustar Stock", javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.isBlank()) {
            try {
                int nuevoStock = Integer.parseInt(input.trim());
                if (nuevoStock < 0) throw new NumberFormatException();
                medicamentoDAO.actualizarStock(id, nuevoStock);
                cargarDatos();
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "Ingresa un número entero ≥ 0.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
