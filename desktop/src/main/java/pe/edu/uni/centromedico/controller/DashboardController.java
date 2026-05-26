package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Slot;
import pe.edu.uni.centromedico.service.CitaService;
import pe.edu.uni.centromedico.ui.dialogs.NuevaCitaDialog;
import pe.edu.uni.centromedico.ui.panels.DashboardPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;
import pe.edu.uni.centromedico.util.SesionManager;

import java.util.List;

public class DashboardController {

    private final DashboardPanel vista;
    private final CitaService    citaService;
    private List<Slot> todosLosSlots;

    public DashboardController(DashboardPanel vista) {
        this.vista       = vista;
        this.citaService = new CitaService();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        todosLosSlots = citaService.obtenerTodosLosSlots();
        vista.cargarDatos(todosLosSlots);
    }

    private void conectarEventos() {
        vista.getBtnTodos().addActionListener(e       ->
            ErrorHandler.ejecutarSeguro(vista, () -> vista.filtrarPublico("Todos")));
        vista.getBtnDisponibles().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, () -> vista.filtrarPublico("1")));
        vista.getBtnOcupados().addActionListener(e    ->
            ErrorHandler.ejecutarSeguro(vista, () -> vista.filtrarPublico("0")));
        vista.getCbxEspecialidad().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, () -> vista.filtrarPublico("Todos")));

        vista.getBtnAgendar().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::abrirNuevaCita));
    }

    private void abrirNuevaCita() {
        if (!SesionManager.haySesion()) {
            ErrorHandler.mostrarError(vista, "No hay sesión activa.");
            return;
        }
        java.awt.Frame ventana = (java.awt.Frame)
            javax.swing.SwingUtilities.getWindowAncestor(vista);
        new NuevaCitaDialog(ventana, true, SesionManager.getUsuario()).setVisible(true);
        cargarDatos();
    }
}
