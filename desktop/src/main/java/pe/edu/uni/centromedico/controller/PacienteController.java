package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.EstudianteDAO;
import pe.edu.uni.centromedico.models.Estudiante;
import pe.edu.uni.centromedico.ui.dialogs.NuevoPacienteDialog;
import pe.edu.uni.centromedico.ui.panels.PacientePanel;
import pe.edu.uni.centromedico.util.ErrorHandler;

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
        vista.getBtnBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getTxtBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getBtnEliminar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::eliminar));
        vista.getBtnNuevo().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::nuevo));
    }

    private void eliminar() {
        int fila = vista.getFilaSeleccionada();
        if (fila < 0) {
            ErrorHandler.mostrarAdvertencia(vista, "Selecciona un paciente para eliminar.");
            return;
        }
        String nombre = (String) vista.getTblPacientes().getValueAt(fila, 1);
        if (!ErrorHandler.confirmar(vista, "Confirmar eliminación",
                "¿Eliminar al paciente " + nombre + "?")) return;

        String id = (String) vista.getTblPacientes().getValueAt(fila, 0);
        if (estudianteDAO.eliminar(id)) {
            ErrorHandler.mostrarInfo(vista, "Eliminado", "Paciente eliminado correctamente.");
        } else {
            ErrorHandler.mostrarError(vista, "No se pudo eliminar el paciente.");
        }
        cargarDatos();
    }

    private void nuevo() {
        java.awt.Frame ventana = (java.awt.Frame)
            javax.swing.SwingUtilities.getWindowAncestor(vista);
        new NuevoPacienteDialog(ventana, true).setVisible(true);
        cargarDatos();
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
