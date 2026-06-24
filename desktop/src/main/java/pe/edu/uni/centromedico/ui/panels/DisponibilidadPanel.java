package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class DisponibilidadPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JLabel lbl_instruccion;
    private final javax.swing.JPanel pnl_dias;
    private final javax.swing.JButton btn_guardar_disp;

    public DisponibilidadPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Mi Disponibilidad — Próximo Mes");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);
        lbl_instruccion = new javax.swing.JLabel("Marca los días y horarios en los que estarás disponible");

        String[] horas = {
            "08:00","08:30","09:00","09:30","10:00","10:30",
            "11:00","11:30","12:00","14:00","14:30","15:00",
            "15:30","16:00","16:30","17:00","17:30","18:00"
        };
        String[][] dias = {{"Lunes"},{"Martes"},{"Miércoles"},{"Jueves"},{"Viernes"},{"Sábado"}};

        pnl_dias = new javax.swing.JPanel(new net.miginfocom.swing.MigLayout(
            "fillx, insets 20, gapy 10",
            "[160!][80!][grow][80!][grow]", "[]12[]12[]12[]12[]12[]12[]"));
        pnl_dias.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel hDia = new javax.swing.JLabel("Día");
        javax.swing.JLabel hIni = new javax.swing.JLabel("Hora inicio");
        javax.swing.JLabel hFin = new javax.swing.JLabel("Hora fin");
        for (javax.swing.JLabel h : new javax.swing.JLabel[]{hDia, hIni, hFin}) {
            h.setFont(new java.awt.Font("Liberation Sans", java.awt.Font.BOLD, 11));
            h.setForeground(new java.awt.Color(100, 116, 139));
        }
        pnl_dias.add(hDia, "");
        pnl_dias.add(new javax.swing.JLabel(""), "");
        pnl_dias.add(hIni, "");
        pnl_dias.add(new javax.swing.JLabel(""), "");
        pnl_dias.add(hFin, "wrap");

        for (String[] dia : dias) {
            javax.swing.JCheckBox chk = new javax.swing.JCheckBox(dia[0]);
            chk.setFont(new java.awt.Font("Liberation Sans", java.awt.Font.BOLD, 13));
            chk.setFocusPainted(false);
            chk.setBackground(java.awt.Color.WHITE);

            javax.swing.JComboBox<String> cmbIni = new javax.swing.JComboBox<>(horas);
            javax.swing.JComboBox<String> cmbFin = new javax.swing.JComboBox<>(horas);
            cmbIni.setEnabled(false);
            cmbFin.setEnabled(false);

            chk.addItemListener(e -> {
                boolean sel = chk.isSelected();
                cmbIni.setEnabled(sel);
                cmbFin.setEnabled(sel);
            });

            pnl_dias.add(chk, "");
            pnl_dias.add(new javax.swing.JLabel("→"), "center");
            pnl_dias.add(cmbIni, "growx");
            pnl_dias.add(new javax.swing.JLabel("—"), "center");
            pnl_dias.add(cmbFin, "growx, wrap");
        }

        btn_guardar_disp = new javax.swing.JButton("Guardar Disponibilidad");
        btn_guardar_disp.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_guardar_disp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_guardar_disp.addActionListener(e ->
            javax.swing.JOptionPane.showMessageDialog(this,
                "Disponibilidad guardada correctamente.",
                "Disponibilidad", javax.swing.JOptionPane.INFORMATION_MESSAGE));

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]6[]16[grow]16[]"));
        add(lbl_titulo, "wrap");
        add(lbl_instruccion, "wrap");
        add(pnl_dias, "grow, wrap");
        add(btn_guardar_disp, "right, h 44!, w 220!");
    }

    public javax.swing.JPanel getPnlDias()          { return pnl_dias; }
    public javax.swing.JButton getBtnGuardar()      { return btn_guardar_disp; }
}
