package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class CitaPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JLabel lbl_fecha_hoy;
    private final javax.swing.JScrollPane scrl_citas_medico;
    private final javax.swing.JTable tbl_citas_medico;

    public CitaPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Mis Citas del Día");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);
        lbl_titulo.setForeground(UIConstants.TEXTO_TITULO);

        String hoy = java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern(
                "EEEE dd 'de' MMMM, yyyy", new java.util.Locale("es")));
        lbl_fecha_hoy = new javax.swing.JLabel(
            Character.toUpperCase(hoy.charAt(0)) + hoy.substring(1));

        tbl_citas_medico = new javax.swing.JTable();
        scrl_citas_medico = new javax.swing.JScrollPane(tbl_citas_medico);
        UIConstants.configurarTabla(tbl_citas_medico, scrl_citas_medico);

        tbl_citas_medico.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Hora", "Paciente", "Especialidad", "Estado", "Acción"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]4[]16[grow]"));
        add(lbl_titulo, "wrap");
        add(lbl_fecha_hoy, "wrap");
        add(scrl_citas_medico, "grow");
    }

    public javax.swing.JTable getTblCitas()          { return tbl_citas_medico; }
    public void setFecha(String fecha)               { lbl_fecha_hoy.setText(fecha); }
    public int getFilaSeleccionada()                 { return tbl_citas_medico.getSelectedRow(); }
    public Object getValueAt(int row, int col)       { return tbl_citas_medico.getValueAt(row, col); }
}
