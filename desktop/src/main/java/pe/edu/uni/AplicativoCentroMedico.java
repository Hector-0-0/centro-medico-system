package pe.edu.uni;

import pe.edu.uni.centromedico.ui.frames.LoginFrame;

public class AplicativoCentroMedico {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatLightLaf.setup();  // FlatLaf aquí
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
