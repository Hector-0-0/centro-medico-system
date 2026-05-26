package pe.edu.uni.centromedico.util;

import java.awt.Component;
import javax.swing.JOptionPane;

public final class ErrorHandler {

    private ErrorHandler() { /* util — no instanciable */ }

    public static void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje,
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarAdvertencia(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje,
            "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public static void mostrarInfo(Component padre, String titulo, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje,
            titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component padre, String titulo, String mensaje) {
        return JOptionPane.showConfirmDialog(padre, mensaje, titulo,
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // Ejecuta una acción y muestra cualquier excepción como diálogo de error.
    // Garantiza que el crash de un listener nunca rompa la app.
    public static void ejecutarSeguro(Component padre, Runnable accion) {
        try {
            accion.run();
        } catch (Exception ex) {
            mostrarError(padre,
                "Ocurrió un error inesperado: " + ex.getMessage());
        }
    }
}
