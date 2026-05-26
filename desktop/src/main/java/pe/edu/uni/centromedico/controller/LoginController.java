package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Persona;
import pe.edu.uni.centromedico.service.AuthService;
import pe.edu.uni.centromedico.ui.dialogs.ErrorDialog;
import pe.edu.uni.centromedico.ui.frames.LoginFrame;
import pe.edu.uni.centromedico.ui.frames.MainFrame;
import pe.edu.uni.centromedico.util.ErrorHandler;
import pe.edu.uni.centromedico.util.SesionManager;

public class LoginController {

    private final LoginFrame  vista;
    private final AuthService authService;

    public LoginController(LoginFrame vista) {
        this.vista       = vista;
        this.authService = new AuthService();
        conectarEventos();
    }

    private void conectarEventos() {
        vista.getBtnIngresar().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::autenticar));
    }

    private void autenticar() {
        String codigo   = vista.getTxtUsuario().getText().trim();
        String password = new String(vista.getTxtPassword().getPassword()).trim();

        if (codigo.isEmpty() || password.isEmpty()) {
            new ErrorDialog(vista, true, "Ingresa tu código y contraseña.").setVisible(true);
            return;
        }

        Persona persona = authService.autenticar(codigo, password);
        if (persona == null) {
            new ErrorDialog(vista, true, "Código o contraseña incorrectos.").setVisible(true);
            return;
        }

        SesionManager.iniciar(persona);
        vista.dispose();
        new MainFrame(persona).setVisible(true);
    }
}
