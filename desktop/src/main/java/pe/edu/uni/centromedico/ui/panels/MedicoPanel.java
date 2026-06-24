package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class MedicoPanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JTextField txt_buscar;
    private final javax.swing.JButton btn_buscar;
    private final javax.swing.JButton btn_nuevo;
    private final javax.swing.JScrollPane scrl_medicos;
    private final javax.swing.JTable tbl_medicos;

    public MedicoPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Gestión de Médicos");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);
        lbl_titulo.setForeground(UIConstants.TEXTO_TITULO);

        txt_buscar = new javax.swing.JTextField();
        txt_buscar.putClientProperty("JTextField.placeholderText", "Buscar por nombre...");

        btn_buscar = new javax.swing.JButton("Buscar");
        btn_nuevo = new javax.swing.JButton("+ Nuevo Médico");
        btn_nuevo.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_nuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tbl_medicos = new javax.swing.JTable();
        scrl_medicos = new javax.swing.JScrollPane(tbl_medicos);
        UIConstants.configurarTabla(tbl_medicos, scrl_medicos);

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]12[]12[grow]"));

        javax.swing.JPanel pnlTop = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][][]", "[36!]"));
        pnlTop.setOpaque(false);
        pnlTop.add(txt_buscar, "growx, h 36!");
        pnlTop.add(btn_buscar, "h 36!, gapleft 8");
        pnlTop.add(btn_nuevo,  "h 36!, gapleft 4");

        add(lbl_titulo, "wrap");
        add(pnlTop,    "growx, wrap");
        add(scrl_medicos, "grow");
    }

    public javax.swing.JTable     getTblMedicos()   { return tbl_medicos; }
    public javax.swing.JButton    getBtnBuscar()    { return btn_buscar; }
    public javax.swing.JButton    getBtnNuevo()     { return btn_nuevo; }
    public javax.swing.JTextField getTxtBuscar()    { return txt_buscar; }
    public int getFilaSeleccionada()                { return tbl_medicos.getSelectedRow(); }
}
