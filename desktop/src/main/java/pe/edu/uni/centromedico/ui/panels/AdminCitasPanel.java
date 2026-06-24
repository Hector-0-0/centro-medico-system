package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class AdminCitasPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JPanel pnl_filtros;
    private final javax.swing.JComboBox<String> cmb_especialidad;
    private final javax.swing.JComboBox<String> cmb_estado;
    private final javax.swing.JButton btn_filtrar;
    private final javax.swing.JScrollPane scrl_citas_admin;
    private final javax.swing.JTable tbl_citas_admin;

    public AdminCitasPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Todas las Citas");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);

        pnl_filtros = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][grow][]", "[36!]"));
        pnl_filtros.setOpaque(false);

        cmb_especialidad = new javax.swing.JComboBox<>();
        cmb_especialidad.addItem("Todas");
        cmb_estado = new javax.swing.JComboBox<>(new String[]{"Todos", "PENDIENTE", "ATENDIDA", "CANCELADA"});
        btn_filtrar = new javax.swing.JButton("Filtrar");
        btn_filtrar.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_filtrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        pnl_filtros.add(cmb_especialidad, "growx, h 36!");
        pnl_filtros.add(cmb_estado, "growx, h 36!, gapleft 8");
        pnl_filtros.add(btn_filtrar, "h 36!, gapleft 8");

        tbl_citas_admin = new javax.swing.JTable();
        scrl_citas_admin = new javax.swing.JScrollPane(tbl_citas_admin);
        UIConstants.configurarTabla(tbl_citas_admin, scrl_citas_admin);
        tbl_citas_admin.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Paciente", "Especialidad", "Médico", "Fecha", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]12[]12[grow]"));
        add(lbl_titulo, "wrap");
        add(pnl_filtros, "growx, wrap");
        add(scrl_citas_admin, "grow");
    }

    public javax.swing.JTable            getTblCitasAdmin()    { return tbl_citas_admin; }
    public javax.swing.JButton           getBtnFiltrar()       { return btn_filtrar; }
    public javax.swing.JComboBox<String> getCmbEspecialidad()  { return cmb_especialidad; }
    public javax.swing.JComboBox<String> getCmbEstado()        { return cmb_estado; }

    public void setEspecialidades(java.util.List<String> especialidades) {
        javax.swing.DefaultComboBoxModel<String> model = new javax.swing.DefaultComboBoxModel<>();
        model.addElement("Todas");
        especialidades.forEach(model::addElement);
        cmb_especialidad.setModel(model);
    }
}
