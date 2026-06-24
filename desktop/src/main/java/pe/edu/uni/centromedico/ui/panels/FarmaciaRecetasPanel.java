package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class FarmaciaRecetasPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JPanel pnl_busqueda;
    private final javax.swing.JTextField txt_buscar_receta;
    private final javax.swing.JButton btn_buscar_receta;
    private final javax.swing.JScrollPane scrl_recetas;
    private final javax.swing.JTable tbl_recetas;
    private final javax.swing.JButton btn_confirmar_entrega;

    public FarmaciaRecetasPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Recetas Pendientes");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);

        pnl_busqueda = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[36!]"));
        pnl_busqueda.setOpaque(false);

        txt_buscar_receta = new javax.swing.JTextField();
        txt_buscar_receta.putClientProperty("JTextField.placeholderText", "Buscar receta...");
        btn_buscar_receta = new javax.swing.JButton("Buscar");

        pnl_busqueda.add(txt_buscar_receta, "growx, h 36!");
        pnl_busqueda.add(btn_buscar_receta, "h 36!, gapleft 8");

        tbl_recetas = new javax.swing.JTable();
        scrl_recetas = new javax.swing.JScrollPane(tbl_recetas);
        UIConstants.configurarTabla(tbl_recetas, scrl_recetas);
        tbl_recetas.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Paciente", "Medicamento", "Dosis", "Indicacion", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        btn_confirmar_entrega = new javax.swing.JButton("Confirmar Entrega");
        btn_confirmar_entrega.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_confirmar_entrega.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]12[]12[grow]12[]"));
        add(lbl_titulo, "wrap");
        add(pnl_busqueda, "growx, wrap");
        add(scrl_recetas, "grow, wrap");
        add(btn_confirmar_entrega, "right, h 44!, w 200!");
    }

    public javax.swing.JTable getTblRecetas()          { return tbl_recetas; }
    public javax.swing.JButton getBtnBuscarReceta()    { return btn_buscar_receta; }
    public javax.swing.JButton getBtnConfirmarEntrega(){ return btn_confirmar_entrega; }
    public javax.swing.JTextField getTxtBuscarReceta() { return txt_buscar_receta; }
}
