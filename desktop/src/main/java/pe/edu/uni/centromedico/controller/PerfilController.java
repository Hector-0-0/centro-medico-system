package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Persona;
import pe.edu.uni.centromedico.ui.dialogs.EditarPerfilDialog;
import pe.edu.uni.centromedico.ui.panels.PerfilPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;

public class PerfilController {

    private final PerfilPanel vista;
    private final Persona persona;

    public PerfilController(PerfilPanel vista, Persona persona) {
        this.vista = vista;
        this.persona = persona;
        conectarEventos();
    }

    private void conectarEventos() {
        vista.getBtnEditarPerfil().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::editarPerfil));
    }

    private void editarPerfil() {
        EditarPerfilDialog dlg = new EditarPerfilDialog(
            javax.swing.SwingUtilities.getWindowAncestor(vista) instanceof java.awt.Frame f
                ? f : null, true, persona);
        dlg.setVisible(true);
        if (dlg.isGuardado()) {
            vista.actualizarDatos(persona);
        }
    }
}
