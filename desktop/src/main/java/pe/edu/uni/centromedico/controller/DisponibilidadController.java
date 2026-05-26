package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.DisponibilidadDAO;
import pe.edu.uni.centromedico.ui.panels.DisponibilidadPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;
import pe.edu.uni.centromedico.util.SesionManager;

import java.awt.Component;

public class DisponibilidadController {

    private final DisponibilidadPanel vista;
    private final DisponibilidadDAO   disponibilidadDAO;

    public DisponibilidadController(DisponibilidadPanel vista) {
        this.vista              = vista;
        this.disponibilidadDAO  = new DisponibilidadDAO();
        conectarEventos();
    }

    private void conectarEventos() {
        // Limpiar stub generado por NetBeans y agregar el real
        for (java.awt.event.ActionListener al : vista.getBtnGuardar().getActionListeners()) {
            vista.getBtnGuardar().removeActionListener(al);
        }
        vista.getBtnGuardar().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::guardarDisponibilidad));
    }

    private void guardarDisponibilidad() {
        String idDoctor = SesionManager.getId();
        if (idDoctor == null || idDoctor.isBlank()) {
            ErrorHandler.mostrarError(vista, "No hay sesión activa.");
            return;
        }

        // pnl_dias contiene: 5 JLabel encabezados + por día (JCheckBox, JLabel"→", JComboBox ini, JLabel"—", JComboBox fin)
        Component[] comps = vista.getPnlDias().getComponents();
        int guardados = 0;
        int rechazados = 0;
        int idx = 0;

        while (idx < comps.length && comps[idx] instanceof javax.swing.JLabel) {
            idx++;
        }

        while (idx < comps.length) {
            if (!(comps[idx] instanceof javax.swing.JCheckBox chk)) { idx++; continue; }
            if (idx + 4 >= comps.length) break;

            if (comps[idx + 2] instanceof javax.swing.JComboBox<?> cmbIni
             && comps[idx + 4] instanceof javax.swing.JComboBox<?> cmbFin
             && chk.isSelected()) {

                String horaIni = cmbIni.getSelectedItem() != null
                    ? cmbIni.getSelectedItem().toString() : "08:00";
                String horaFin = cmbFin.getSelectedItem() != null
                    ? cmbFin.getSelectedItem().toString() : "09:00";

                if (horaIni.compareTo(horaFin) >= 0) {
                    rechazados++;
                } else {
                    boolean ok = disponibilidadDAO.guardar(idDoctor, chk.getText(), horaIni, horaFin);
                    if (ok) guardados++; else rechazados++;
                }
            }
            idx += 5;
        }

        if (guardados > 0 && rechazados == 0) {
            ErrorHandler.mostrarInfo(vista, "Disponibilidad",
                "Disponibilidad guardada: " + guardados + " día(s) registrado(s).");
        } else if (guardados > 0) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Guardados: " + guardados + ". Rechazados: " + rechazados +
                " (rango inválido o citas pendientes en ese día).");
        } else if (rechazados > 0) {
            ErrorHandler.mostrarError(vista,
                "No se pudo guardar ningún día. Verifica el rango horario " +
                "o que no haya citas pendientes en esos días.");
        } else {
            ErrorHandler.mostrarAdvertencia(vista,
                "Selecciona al menos un día para guardar.");
        }
    }
}
