package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class GestionStockPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JPanel pnl_acciones;
    private final javax.swing.JTextField txt_buscar_stock;
    private final javax.swing.JButton btn_buscar_stock;
    private final javax.swing.JButton btn_agregar_stock;
    private final javax.swing.JScrollPane scrl_stock;
    private final javax.swing.JTable tbl_stock;

    public GestionStockPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Gestión de Medicamentos");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);

        pnl_acciones = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][][]", "[36!]"));
        pnl_acciones.setOpaque(false);

        txt_buscar_stock = new javax.swing.JTextField();
        txt_buscar_stock.putClientProperty("JTextField.placeholderText", "Buscar medicamento...");

        btn_buscar_stock  = new javax.swing.JButton("Buscar");
        btn_agregar_stock = new javax.swing.JButton("+ Agregar");
        btn_agregar_stock.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_agregar_stock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        pnl_acciones.add(txt_buscar_stock, "growx, h 36!");
        pnl_acciones.add(btn_buscar_stock, "h 36!, gapleft 8");
        pnl_acciones.add(btn_agregar_stock, "h 36!, gapleft 4");

        tbl_stock = new javax.swing.JTable();
        scrl_stock = new javax.swing.JScrollPane(tbl_stock);
        UIConstants.configurarTabla(tbl_stock, scrl_stock);

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]12[]12[grow]"));
        add(lbl_titulo, "wrap");
        add(pnl_acciones, "growx, wrap");
        add(scrl_stock, "grow");
    }

    public javax.swing.JTable     getTblStock()         { return tbl_stock; }
    public javax.swing.JButton    getBtnBuscarStock()   { return btn_buscar_stock; }
    public javax.swing.JButton    getBtnAgregarStock()  { return btn_agregar_stock; }
    public javax.swing.JTextField getTxtBuscarStock()   { return txt_buscar_stock; }
}
