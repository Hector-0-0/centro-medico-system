package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.DoctorDAO;
import pe.edu.uni.centromedico.models.Doctor;
import pe.edu.uni.centromedico.ui.dialogs.NuevoMedicoDialog;
import pe.edu.uni.centromedico.ui.panels.MedicoPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MedicoController {

    private final MedicoPanel vista;
    private final DoctorDAO   doctorDAO;

    public MedicoController(MedicoPanel vista) {
        this.vista     = vista;
        this.doctorDAO = new DoctorDAO();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        cargarTabla(doctorDAO.obtenerTodos());
    }

    private void cargarTabla(List<Doctor> doctores) {
        String[][] datos = new String[doctores.size()][4];
        for (int i = 0; i < doctores.size(); i++) {
            Doctor d = doctores.get(i);
            datos[i] = new String[]{
                d.getId(), d.getNombre(),
                d.getEspecialidad(), d.isActivo() ? "Activo" : "Inactivo"
            };
        }
        vista.getTblMedicos().setModel(new DefaultTableModel(
            datos, new String[]{"Código", "Nombre", "Especialidad", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getTxtBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getBtnNuevo().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::nuevo));

        vista.getTblMedicos().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    ErrorHandler.ejecutarSeguro(vista, MedicoController.this::eliminarSeleccionado);
            }
        });
    }

    private void buscar() {
        String texto = vista.getTxtBuscar().getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarDatos(); return; }
        List<Doctor> filtrados = doctorDAO.obtenerTodos().stream()
            .filter(d -> d.getNombre().toLowerCase().contains(texto)
                      || d.getEspecialidad().toLowerCase().contains(texto)
                      || d.getId().toLowerCase().contains(texto))
            .toList();
        cargarTabla(filtrados);
    }

    private void nuevo() {
        java.awt.Frame ventana = (java.awt.Frame)
            javax.swing.SwingUtilities.getWindowAncestor(vista);
        new NuevoMedicoDialog(ventana, true).setVisible(true);
        cargarDatos();
    }

    private void eliminarSeleccionado() {
        int fila = vista.getFilaSeleccionada();
        if (fila < 0) return;
        String nombre = (String) vista.getTblMedicos().getValueAt(fila, 1);
        if (!ErrorHandler.confirmar(vista, "Confirmar eliminación",
                "¿Eliminar al médico " + nombre + "?")) return;

        String id = (String) vista.getTblMedicos().getValueAt(fila, 0);
        if (doctorDAO.eliminar(id)) {
            ErrorHandler.mostrarInfo(vista, "Eliminado", "Médico eliminado correctamente.");
        } else {
            ErrorHandler.mostrarError(vista, "No se pudo eliminar el médico.");
        }
        cargarDatos();
    }
}
