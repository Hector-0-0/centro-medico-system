package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.util.UIConstants;

public class AtenderCitaPanel extends javax.swing.JPanel {

    private final javax.swing.JPanel pnl_paciente;
    private final javax.swing.JLabel lbl_nombre_pac;
    private final javax.swing.JLabel lbl_codigo_pac;
    private final javax.swing.JLabel lbl_edad_pac;
    private final javax.swing.JSeparator sep_datos;
    private final javax.swing.JLabel lbl_alergias_titulo;
    private final javax.swing.JLabel lbl_alergias;
    private final javax.swing.JPanel pnl_formulario;
    private final javax.swing.JLabel lbl_cie;
    private final javax.swing.JTextField txtBuscarCie;
    private final javax.swing.JButton btnBuscarCie;
    private final javax.swing.JScrollPane scrlResultadosCie;
    private final javax.swing.JList<String> lstResultadosCie;
    private final javax.swing.JButton btnAgregarCie;
    private final javax.swing.JButton btnQuitarCie;
    private final javax.swing.JScrollPane scrlDiagnosticos;
    private final javax.swing.JTable tblDiagnosticos;
    private final javax.swing.JLabel lbl_comentarios;
    private final javax.swing.JScrollPane scrl_comentarios;
    private final javax.swing.JTextArea ta_comentarios;
    private final javax.swing.JLabel lbl_receta;
    private final javax.swing.JScrollPane scrl_receta;
    private final javax.swing.JTable tbl_receta;
    private final javax.swing.JPanel pnl_botones;
    private final javax.swing.JButton btn_guardar_aten;
    private final javax.swing.JButton btn_cancelar_aten;
    private javax.swing.JComboBox<String> cbMedicamento;
    private javax.swing.JTextField txtDosisMed;
    private javax.swing.JTextField txtDuraMed;
    private javax.swing.JButton btnAgregarMed;

    public AtenderCitaPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        // Panel izquierdo: datos del paciente
        pnl_paciente = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 20 16 20 16", "[grow]", "[]8[]8[]16[]6[]20[]"));
        pnl_paciente.setBackground(UIConstants.FONDO_PANEL);

        lbl_nombre_pac = new javax.swing.JLabel("Nombre del Paciente");
        lbl_nombre_pac.setFont(new java.awt.Font("Liberation Sans", 1, 15));
        lbl_codigo_pac = new javax.swing.JLabel("Código: —");
        lbl_edad_pac   = new javax.swing.JLabel("Edad: —");
        sep_datos      = new javax.swing.JSeparator();
        lbl_alergias_titulo = new javax.swing.JLabel("Alergias");
        lbl_alergias   = new javax.swing.JLabel("—");

        pnl_paciente.add(lbl_nombre_pac, "wrap");
        pnl_paciente.add(lbl_codigo_pac, "wrap");
        pnl_paciente.add(lbl_edad_pac, "wrap");
        pnl_paciente.add(sep_datos, "growx, wrap");
        pnl_paciente.add(lbl_alergias_titulo, "wrap");
        pnl_paciente.add(lbl_alergias, "wrap");

        // Panel derecho: formulario de atención
        pnl_formulario = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 20", "[grow]",
                "[]4[]4[120!]4[]4[100!]4[]6[80!]10[]6[100!]4[]16[]"));
        pnl_formulario.setBackground(java.awt.Color.WHITE);

        lbl_cie = new javax.swing.JLabel("Diagnósticos CIE (código internacional)");

        txtBuscarCie = new javax.swing.JTextField();
        btnBuscarCie = new javax.swing.JButton("Buscar");

        lstResultadosCie = new javax.swing.JList<>();
        scrlResultadosCie = new javax.swing.JScrollPane(lstResultadosCie);

        btnAgregarCie = new javax.swing.JButton("Agregar >>");
        btnAgregarCie.setBackground(new java.awt.Color(139, 20, 20));
        btnAgregarCie.setForeground(java.awt.Color.WHITE);
        btnAgregarCie.setBorderPainted(false);
        btnAgregarCie.setFocusPainted(false);
        btnAgregarCie.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnQuitarCie = new javax.swing.JButton("Quitar");
        btnQuitarCie.setBackground(new java.awt.Color(180, 120, 110));
        btnQuitarCie.setForeground(java.awt.Color.WHITE);
        btnQuitarCie.setBorderPainted(false);
        btnQuitarCie.setFocusPainted(false);
        btnQuitarCie.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tblDiagnosticos = new javax.swing.JTable();
        scrlDiagnosticos = new javax.swing.JScrollPane(tblDiagnosticos);
        tblDiagnosticos.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Código", "Descripción", "Observación"}) {
            Class<?>[] types = new Class<?>[]{String.class, String.class, String.class};
            boolean[] canEdit = new boolean[]{false, false, true};
            public Class<?> getColumnClass(int idx) { return types[idx]; }
            public boolean isCellEditable(int r, int c) { return canEdit[c]; }
        });

        lbl_comentarios = new javax.swing.JLabel("Comentarios / Observaciones");
        ta_comentarios = new javax.swing.JTextArea(5, 20);
        scrl_comentarios = new javax.swing.JScrollPane(ta_comentarios);

        lbl_receta = new javax.swing.JLabel("Receta Médica");
        tbl_receta = new javax.swing.JTable();
        scrl_receta = new javax.swing.JScrollPane(tbl_receta);
        tbl_receta.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Medicamento", "Dosis", "Frecuencia"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        // Sección CIE buscador
        pnl_formulario.add(lbl_cie, "wrap");
        javax.swing.JPanel pnlBuscarCie = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[]"));
        pnlBuscarCie.add(txtBuscarCie, "growx, h 30!");
        pnlBuscarCie.add(btnBuscarCie, "w 100!, h 30!");
        pnl_formulario.add(pnlBuscarCie, "growx, wrap");

        javax.swing.JPanel pnlResultadosCie = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[120!]"));
        pnlResultadosCie.add(scrlResultadosCie, "grow");
        pnlResultadosCie.add(btnAgregarCie, "w 90!, h 30!, top");
        pnl_formulario.add(pnlResultadosCie, "growx, wrap");

        javax.swing.JPanel pnlDiagSel = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[100!]"));
        pnlDiagSel.add(scrlDiagnosticos, "grow");
        pnlDiagSel.add(btnQuitarCie, "w 90!, h 30!, top");
        pnl_formulario.add(pnlDiagSel, "growx, wrap");

        pnl_formulario.add(lbl_comentarios, "wrap");
        pnl_formulario.add(scrl_comentarios, "growx, h 80!, wrap");

        pnl_formulario.add(lbl_receta, "wrap");
        pnl_formulario.add(scrl_receta, "growx, h 100!, wrap");

        // Fila: agregar medicamento a receta
        javax.swing.JPanel pnlAddMed = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][120!][80!][90!]", "[30!]"));
        cbMedicamento = new javax.swing.JComboBox<>();
        txtDosisMed   = new javax.swing.JTextField();
        txtDuraMed    = new javax.swing.JTextField();
        btnAgregarMed = new javax.swing.JButton("+ Agregar");
        txtDosisMed.setToolTipText("Dosis (ej: 500mg)");
        txtDuraMed.setToolTipText("Duración (ej: 7 días)");
        btnAgregarMed.setBackground(new java.awt.Color(139, 20, 20));
        btnAgregarMed.setForeground(java.awt.Color.WHITE);
        btnAgregarMed.setBorderPainted(false);
        btnAgregarMed.setFocusPainted(false);
        btnAgregarMed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlAddMed.add(cbMedicamento, "growx, h 30!");
        pnlAddMed.add(txtDosisMed,   "w 120!, h 30!");
        pnlAddMed.add(txtDuraMed,    "w 80!,  h 30!");
        pnlAddMed.add(btnAgregarMed, "w 90!,  h 30!");

        pnl_formulario.add(pnlAddMed, "growx, wrap");

        // Botones inferiores
        pnl_botones = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][grow]", "[]"));
        btn_guardar_aten = new javax.swing.JButton("Guardar Consulta");
        btn_guardar_aten.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_guardar_aten.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelar_aten = new javax.swing.JButton("Cancelar");
        btn_cancelar_aten.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_SECUNDARIO);
        btn_cancelar_aten.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_botones.add(btn_cancelar_aten, "growx, h 40!");
        pnl_botones.add(btn_guardar_aten, "growx, h 40!");
        pnl_formulario.add(pnl_botones, "growx");

        // Layout principal: split 35% | 65% envuelto en JScrollPane
        javax.swing.JPanel wrapper = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fill, insets 0", "[35%][grow]", "[grow]"));
        wrapper.add(pnl_paciente, "growy");
        wrapper.add(pnl_formulario, "grow");

        javax.swing.JScrollPane scrlPrincipal = new javax.swing.JScrollPane(wrapper);
        scrlPrincipal.setBorder(null);
        scrlPrincipal.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 0", "[grow]", "[grow]"));
        add(scrlPrincipal, "grow");
    }

    public javax.swing.JButton            getBtnGuardar()      { return btn_guardar_aten; }
    public javax.swing.JButton            getBtnCancelar()     { return btn_cancelar_aten; }
    public javax.swing.JTextArea           getTaComentarios()   { return ta_comentarios; }
    public javax.swing.JTable              getTblReceta()       { return tbl_receta; }
    public javax.swing.JComboBox<String>   getCbMedicamento()   { return cbMedicamento; }
    public javax.swing.JTextField          getTxtDosisMed()     { return txtDosisMed; }
    public javax.swing.JTextField          getTxtDuraMed()      { return txtDuraMed; }
    public javax.swing.JButton             getBtnAgregarMed()   { return btnAgregarMed; }
    public javax.swing.JTextField          getTxtBuscarCie()    { return txtBuscarCie; }
    public javax.swing.JButton             getBtnBuscarCie()    { return btnBuscarCie; }
    public javax.swing.JList<String>       getLstResultadosCie(){ return lstResultadosCie; }
    public javax.swing.JButton             getBtnAgregarCie()   { return btnAgregarCie; }
    public javax.swing.JTable              getTblDiagnosticos() { return tblDiagnosticos; }
    public javax.swing.JButton             getBtnQuitarCie()    { return btnQuitarCie; }

    public void setDatosPaciente(String nombre, String codigo, String edad) {
        lbl_nombre_pac.setText(nombre);
        lbl_codigo_pac.setText("Código: " + codigo);
        lbl_edad_pac.setText("Edad: " + edad);
    }
}
