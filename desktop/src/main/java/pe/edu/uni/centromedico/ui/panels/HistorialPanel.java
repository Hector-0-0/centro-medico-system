package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class HistorialPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JPanel pnl_busqueda;
    private final javax.swing.JTextField txt_buscar;
    private final javax.swing.JButton btn_buscar;
    private final javax.swing.JScrollPane scrl_citas;
    private final javax.swing.JTable tbl_citas;

    public HistorialPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Mis Citas");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);

        pnl_busqueda = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[36!]"));
        pnl_busqueda.setOpaque(false);

        txt_buscar = new javax.swing.JTextField();
        txt_buscar.putClientProperty("JTextField.placeholderText", "Buscar...");
        btn_buscar = new javax.swing.JButton("Buscar");

        pnl_busqueda.add(txt_buscar, "growx, h 36!");
        pnl_busqueda.add(btn_buscar, "h 36!, gapleft 8");

        tbl_citas = new javax.swing.JTable();
        scrl_citas = new javax.swing.JScrollPane(tbl_citas);
        UIConstants.configurarTabla(tbl_citas, scrl_citas);
        tbl_citas.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Especialidad", "Médico", "Día", "Hora", "Consultorio", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        javax.swing.JComboBox<String> cmb_fecha = new javax.swing.JComboBox<>(
            new String[]{"Todas", "Hoy", "Esta semana", "Este mes"});

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]12[]12[]12[grow]"));
        add(lbl_titulo, "wrap");
        add(pnl_busqueda, "growx, wrap");
        add(cmb_fecha, "wrap");
        add(scrl_citas, "grow");
    }

    public javax.swing.JTable getTblCitas()    { return tbl_citas; }
    public javax.swing.JButton getBtnBuscar()  { return btn_buscar; }
    public javax.swing.JTextField getTxtBuscar() { return txt_buscar; }
}
