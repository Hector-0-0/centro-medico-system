package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Medicamento;
import pe.edu.uni.centromedico.service.MedicamentoService;
import pe.edu.uni.centromedico.ui.panels.MedicamentoPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MedicamentoController {

    private final MedicamentoPanel    vista;
    private final MedicamentoService  medicamentoService;

    public MedicamentoController(MedicamentoPanel vista) {
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
            datos[i] = new String[]{
                m.getId(), m.getNombre(),
                String.valueOf(m.getStock()), m.getTipo()
            };
        }
        vista.getTblMedicamentos().setModel(new DefaultTableModel(
            datos, new String[]{"Código", "Nombre", "Stock", "Tipo"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getTxtBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
    }

    private void buscar() {
        cargarTabla(medicamentoService.buscar(vista.getTxtBuscar().getText()));
    }
}
