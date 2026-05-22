package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.models.Medicamento;
import pe.edu.uni.centromedico.ui.panels.MedicamentoPanel;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MedicamentoController {

    private final MedicamentoPanel vista;
    private final MedicamentoDAO   medicamentoDAO;

    public MedicamentoController(MedicamentoPanel vista) {
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
            datos[i] = new String[]{
                m.getId(), m.getNombre(),
                String.valueOf(m.getStock()), m.getTipo()
            };
        }
        vista.getTblMedicamentos().setModel(new DefaultTableModel(
            datos, new String[]{"Código","Nombre","Stock","Tipo"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getTxtBuscar().addActionListener(e -> buscar());
        // btn_agregar no se incluye en el layout de MedicamentoPanel (vista solo lectura)
    }

    private void buscar() {
        String texto = vista.getTxtBuscar().getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarDatos(); return; }
        List<Medicamento> filtrados = medicamentoDAO.obtenerTodos().stream()
            .filter(m -> m.getNombre().toLowerCase().contains(texto)
                      || m.getId().toLowerCase().contains(texto))
            .toList();
        cargarTabla(filtrados);
    }
}
