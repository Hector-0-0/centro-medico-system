package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.SlotDAO;
import pe.edu.uni.centromedico.models.Slot;
import pe.edu.uni.centromedico.ui.dialogs.ErrorDialog;
import pe.edu.uni.centromedico.ui.panels.DisponibilidadPanel;
import pe.edu.uni.centromedico.util.SesionManager;

import java.awt.Component;

public class DisponibilidadController {

    private final DisponibilidadPanel vista;
    private final SlotDAO slotDAO;

    public DisponibilidadController(DisponibilidadPanel vista) {
        this.vista = vista;
        this.slotDAO = new SlotDAO();
        conectarEventos();
    }

    private void conectarEventos() {
        // Reemplazar el listener de stub puesto en el constructor del panel
        // removemos los listeners existentes y ponemos el nuestro
        for (java.awt.event.ActionListener al : vista.getBtnGuardar().getActionListeners()) {
            vista.getBtnGuardar().removeActionListener(al);
        }
        vista.getBtnGuardar().addActionListener(e -> guardarDisponibilidad());
    }

    private void guardarDisponibilidad() {
        String idDoctor = SesionManager.getId();
        if (idDoctor == null || idDoctor.isBlank()) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                    "No hay sesión activa.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Iterar los componentes de pnl_dias buscando JCheckBox y JComboBox pares
        // La estructura es: JLabel(encabezado)×5, luego por cada día:
        // JCheckBox, JLabel("→"), JComboBox(inicio), JLabel("—"), JComboBox(fin)
        Component[] comps = vista.getPnlDias().getComponents();

        int guardados = 0;
        int idx = 0;

        // Saltar los 5 encabezados (JLabel)
        while (idx < comps.length && comps[idx] instanceof javax.swing.JLabel) {
            idx++;
        }

        while (idx < comps.length) {
            // Buscar el siguiente JCheckBox
            if (!(comps[idx] instanceof javax.swing.JCheckBox chk)) {
                idx++;
                continue;
            }
            // idx+1 = JLabel "→"
            // idx+2 = JComboBox inicio
            // idx+3 = JLabel "—"
            // idx+4 = JComboBox fin
            if (idx + 4 >= comps.length)
                break;

            if (comps[idx + 2] instanceof javax.swing.JComboBox<?> cmbIni
                    && comps[idx + 4] instanceof javax.swing.JComboBox<?> cmbFin) {

                if (chk.isSelected()) {
                    String horaIni = cmbIni.getSelectedItem() != null
                            ? cmbIni.getSelectedItem().toString()
                            : "08:00";
                    String horaFin = cmbFin.getSelectedItem() != null
                            ? cmbFin.getSelectedItem().toString()
                            : "09:00";

                    Slot s = new Slot();
                    s.setIdDoctor(idDoctor);
                    s.setDiaSemana(chk.getText());
                    s.setHoraInicio(horaIni);
                    s.setHoraFin(horaFin);
                    s.setDisponible(true);
                    slotDAO.guardar(s);
         
                    if (slotDAO.eliminarSlotsSinCitas(idDoctor)) {
                        ErrorDialog errorDialog = new ErrorDialog(null, true, "Error al eliminar slots anteriores: ");
                        errorDialog.setVisible(true);           guardados++;
                    }
                }
            }
            idx += 5; // avanzar al siguiente bloque de día
        }

        if (guardados > 0) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                    "Disponibilidad guardada: " + guardados + " día(s) registrado(s).",
                    "Disponibilidad", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(vista,
                    "Selecciona al menos un día para guardar.",
                    "Sin selección", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }
}
