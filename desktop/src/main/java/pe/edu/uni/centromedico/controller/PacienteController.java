package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.EstudianteDAO;
import pe.edu.uni.centromedico.models.Estudiante;
import pe.edu.uni.centromedico.ui.dialogs.NuevoPacienteDialog;
import pe.edu.uni.centromedico.ui.panels.PacientePanel;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PacienteController {

    private final PacientePanel vista;
    private final EstudianteDAO estudianteDAO;

    public PacienteController(PacientePanel vista) {
        this.vista         = vista;
        this.estudianteDAO = new EstudianteDAO();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        cargarTabla(estudianteDAO.obtenerTodos());
    }

    private void cargarTabla(List<Estudiante> lista) {
        String[][] datos = new String[lista.size()][5];
        for (int i = 0; i < lista.size(); i++) {
            Estudiante e = lista.get(i);
            datos[i] = new String[]{ e.getId(), e.getNombre(),
                String.valueOf(e.getEdad()), e.getCarrera(), e.getEmail() };
        }
        vista.getTblPacientes().setModel(new DefaultTableModel(
            datos, new String[]{"Código","Nombre","Edad","Carrera","Email"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getTxtBuscar().addActionListener(e -> buscar());

        vista.getBtnEliminar().addActionListener(e -> {
            int fila = vista.getFilaSeleccionada();
            if (fila < 0) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "Selecciona un paciente para eliminar.",
                    "Sin selección", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nombre = (String) vista.getTblPacientes().getValueAt(fila, 1);
            int confirmar = javax.swing.JOptionPane.showConfirmDialog(vista,
                "¿Eliminar al paciente " + nombre + "?",
                "Confirmar eliminación", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirmar == javax.swing.JOptionPane.YES_OPTION) {
                String id = (String) vista.getTblPacientes().getValueAt(fila, 0);
                boolean ok = estudianteDAO.eliminar(id);
                if (ok) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "Paciente eliminado correctamente.",
                        "Eliminado", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "No se pudo eliminar. Puede tener citas registradas.",
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cargarDatos();
            }
        });

        vista.getBtnNuevo().addActionListener(e -> {
            java.awt.Frame ventana = (java.awt.Frame)
                javax.swing.SwingUtilities.getWindowAncestor(vista);
            new NuevoPacienteDialog(ventana, true).setVisible(true);
            cargarDatos();
        });
    }

    private void buscar() {
        String texto = vista.getTxtBuscar().getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarDatos(); return; }
        List<Estudiante> filtrados = estudianteDAO.obtenerTodos().stream()
            .filter(e -> e.getNombre().toLowerCase().contains(texto)
                      || e.getId().toLowerCase().contains(texto))
            .toList();
        cargarTabla(filtrados);
    }
}
