package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class MedicamentoPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JPanel pnl_acciones;
    private final javax.swing.JTextField txt_buscar_med;
    private final javax.swing.JButton btn_buscar_med;
    private final javax.swing.JButton btn_agregar_med;
    private final javax.swing.JScrollPane scrl_medicamentos;
    private final javax.swing.JTable tbl_medicamentos;

    public MedicamentoPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Inventario de Medicamentos");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);
        lbl_titulo.setForeground(UIConstants.TEXTO_TITULO);

        pnl_acciones = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[36!]"));
        pnl_acciones.setOpaque(false);

        txt_buscar_med = new javax.swing.JTextField();
        txt_buscar_med.putClientProperty("JTextField.placeholderText", "Buscar medicamento...");

        btn_buscar_med = new javax.swing.JButton("Buscar");
        btn_agregar_med = new javax.swing.JButton("+ Agregar");
        btn_agregar_med.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_agregar_med.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        pnl_acciones.add(txt_buscar_med, "growx, h 36!");
        pnl_acciones.add(btn_buscar_med, "h 36!, gapleft 8");
        // btn_agregar_med no se muestra: esta vista es solo lectura para el médico

        tbl_medicamentos = new javax.swing.JTable();
        scrl_medicamentos = new javax.swing.JScrollPane(tbl_medicamentos);
        UIConstants.configurarTabla(tbl_medicamentos, scrl_medicamentos);

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]12[]12[grow]"));
        add(lbl_titulo, "wrap");
        add(pnl_acciones, "growx, wrap");
        add(scrl_medicamentos, "grow");
    }

    public javax.swing.JTable     getTblMedicamentos() { return tbl_medicamentos; }
    public javax.swing.JButton    getBtnBuscar()        { return btn_buscar_med; }
    public javax.swing.JButton    getBtnAgregar()       { return btn_agregar_med; }
    public javax.swing.JTextField getTxtBuscar()        { return txt_buscar_med; }
}
