package pe.edu.uni.centromedico.ui.panels;

public class AtenderCitaPanel extends javax.swing.JPanel {

    public AtenderCitaPanel() {
        initComponents();

        // Datos del paciente (columna izquierda)
        pnl_paciente.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 20 16 20 16", "[grow]",
            "[]8[]8[]16[]6[]20[]"
        ));
        pnl_paciente.removeAll();
        pnl_paciente.add(lbl_nombre_pac,       "wrap");
        pnl_paciente.add(lbl_codigo_pac,       "wrap");
        pnl_paciente.add(lbl_edad_pac,         "wrap");
        pnl_paciente.add(sep_datos,            "growx, wrap");
        pnl_paciente.add(lbl_alergias_titulo,  "wrap");
        pnl_paciente.add(lbl_alergias,         "wrap");

        // Botones inferiores
        pnl_botones.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 0", "[grow][grow]", "[]"
        ));
        pnl_botones.removeAll();
        pnl_botones.add(btn_cancelar_aten, "growx, h 40!");
        pnl_botones.add(btn_guardar_aten,  "growx, h 40!");

        // Formulario de atención (columna derecha)
        pnl_formulario.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 20", "[grow]",
            "[]4[]4[120!]4[]4[100!]4[]6[80!]10[]6[100!]4[]16[]"
        ));
        pnl_formulario.removeAll();

        // Sección CIE: buscador
        pnl_formulario.add(lbl_cie, "wrap");
        javax.swing.JPanel pnlBuscarCie = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[]"));
        pnlBuscarCie.add(txtBuscarCie, "growx, h 30!");
        pnlBuscarCie.add(btnBuscarCie, "w 100!, h 30!");
        pnl_formulario.add(pnlBuscarCie, "growx, wrap");

        // Sección CIE: resultados + botón agregar
        javax.swing.JPanel pnlResultadosCie = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[120!]"));
        pnlResultadosCie.add(scrlResultadosCie, "grow");
        pnlResultadosCie.add(btnAgregarCie, "w 90!, h 30!, top");
        pnl_formulario.add(pnlResultadosCie, "growx, wrap");

        // Sección CIE: tabla de diagnósticos seleccionados + quitar
        javax.swing.JPanel pnlDiagSel = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[100!]"));
        pnlDiagSel.add(scrlDiagnosticos, "grow");
        pnlDiagSel.add(btnQuitarCie, "w 90!, h 30!, top");
        pnl_formulario.add(pnlDiagSel, "growx, wrap");

        // Comentarios / Observaciones
        pnl_formulario.add(lbl_comentarios, "wrap");
        pnl_formulario.add(scrl_comentarios, "growx, h 80!, wrap");

        // Receta Médica
        pnl_formulario.add(lbl_receta, "wrap");
        pnl_formulario.add(scrl_receta, "growx, h 100!, wrap");

        // Fila de controles para agregar medicamentos a la receta
        javax.swing.JPanel pnlAddMed = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fillx, insets 0", "[grow][120!][80!][90!]", "[30!]"));
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
        pnlAddMed.add(cbMedicamento, "growx, h 30!");
        pnlAddMed.add(txtDosisMed,   "w 120!, h 30!");
        pnlAddMed.add(txtDuraMed,    "w 80!,  h 30!");
        pnlAddMed.add(btnAgregarMed, "w 90!,  h 30!");

        pnl_formulario.add(pnlAddMed, "growx, wrap");
        pnl_formulario.add(pnl_botones, "growx");

        // Layout principal: split horizontal 35% | 65%
        this.setLayout(new net.miginfocom.swing.MigLayout(
            "fill, insets 0", "[35%][grow]", "[grow]"
        ));
        this.removeAll();
        this.add(pnl_paciente,   "growy");
        this.add(pnl_formulario, "grow");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_paciente = new javax.swing.JPanel();
        lbl_nombre_pac = new javax.swing.JLabel();
        lbl_codigo_pac = new javax.swing.JLabel();
        lbl_edad_pac = new javax.swing.JLabel();
        sep_datos = new javax.swing.JSeparator();
        lbl_alergias_titulo = new javax.swing.JLabel();
        lbl_alergias = new javax.swing.JLabel();
        pnl_formulario = new javax.swing.JPanel();
        lbl_cie = new javax.swing.JLabel();
        txtBuscarCie = new javax.swing.JTextField();
        btnBuscarCie = new javax.swing.JButton();
        scrlResultadosCie = new javax.swing.JScrollPane();
        lstResultadosCie = new javax.swing.JList<>();
        btnAgregarCie = new javax.swing.JButton();
        btnQuitarCie = new javax.swing.JButton();
        scrlDiagnosticos = new javax.swing.JScrollPane();
        tblDiagnosticos = new javax.swing.JTable();
        lbl_comentarios = new javax.swing.JLabel();
        scrl_comentarios = new javax.swing.JScrollPane();
        ta_comentarios = new javax.swing.JTextArea();
        lbl_receta = new javax.swing.JLabel();
        scrl_receta = new javax.swing.JScrollPane();
        tbl_receta = new javax.swing.JTable();
        pnl_botones = new javax.swing.JPanel();
        btn_guardar_aten = new javax.swing.JButton();
        btn_cancelar_aten = new javax.swing.JButton();

        pnl_paciente.setBackground(new java.awt.Color(249, 245, 240));

        lbl_nombre_pac.setFont(new java.awt.Font("Liberation Sans", 1, 15));
        lbl_nombre_pac.setText("Nombre del Paciente");

        lbl_codigo_pac.setText("Código: —");

        lbl_edad_pac.setText("Edad: —");

        lbl_alergias_titulo.setText("Alergias");

        lbl_alergias.setText("—");

        javax.swing.GroupLayout pnl_pacienteLayout = new javax.swing.GroupLayout(pnl_paciente);
        pnl_paciente.setLayout(pnl_pacienteLayout);
        pnl_pacienteLayout.setHorizontalGroup(
            pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_pacienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_alergias)
                    .addComponent(lbl_nombre_pac)
                    .addComponent(lbl_edad_pac)
                    .addComponent(lbl_codigo_pac)
                    .addComponent(sep_datos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_alergias_titulo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_pacienteLayout.setVerticalGroup(
            pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_pacienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_edad_pac)
                .addGap(14, 14, 14)
                .addComponent(lbl_nombre_pac)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_codigo_pac)
                .addGap(18, 18, 18)
                .addComponent(lbl_alergias_titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_alergias)
                .addGap(18, 18, 18)
                .addComponent(sep_datos, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pnl_formulario.setBackground(new java.awt.Color(255, 255, 255));

        lbl_cie.setText("Diagnósticos CIE (código internacional)");

        btnBuscarCie.setText("Buscar");

        lstResultadosCie.setModel(new javax.swing.AbstractListModel<>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        scrlResultadosCie.setViewportView(lstResultadosCie);

        btnAgregarCie.setText("Agregar >>");
        btnAgregarCie.setBackground(new java.awt.Color(139, 20, 20));
        btnAgregarCie.setForeground(java.awt.Color.WHITE);
        btnAgregarCie.setBorderPainted(false);
        btnAgregarCie.setFocusPainted(false);

        btnQuitarCie.setText("Quitar");
        btnQuitarCie.setBackground(new java.awt.Color(180, 120, 110));
        btnQuitarCie.setForeground(java.awt.Color.WHITE);
        btnQuitarCie.setBorderPainted(false);
        btnQuitarCie.setFocusPainted(false);

        tblDiagnosticos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] { },
            new String [] { "Código", "Descripción", "Observación" }
        ) {
            Class<?>[] types = new Class<?>[] { String.class, String.class, String.class };
            boolean[] canEdit = new boolean[] { false, false, true };
            public Class<?> getColumnClass(int idx) { return types[idx]; }
            public boolean isCellEditable(int r, int c) { return canEdit[c]; }
        });
        scrlDiagnosticos.setViewportView(tblDiagnosticos);

        lbl_comentarios.setText("Comentarios / Observaciones");

        ta_comentarios.setColumns(20);
        ta_comentarios.setRows(5);
        scrl_comentarios.setViewportView(ta_comentarios);

        lbl_receta.setText("Receta Médica");

        tbl_receta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Medicamento", "Dosis", "Frecuencia"
            }
        ));
        scrl_receta.setViewportView(tbl_receta);

        btn_guardar_aten.setText("Guardar Consulta");

        btn_cancelar_aten.setText("Cancelar");

        javax.swing.GroupLayout pnl_formularioLayout = new javax.swing.GroupLayout(pnl_formulario);
        pnl_formulario.setLayout(pnl_formularioLayout);
        pnl_formularioLayout.setHorizontalGroup(
            pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        pnl_formularioLayout.setVerticalGroup(
            pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // ── API pública para AtenderCitaController ───────────────────────────
    public javax.swing.JButton               getBtnGuardar()      { return btn_guardar_aten; }
    public javax.swing.JButton               getBtnCancelar()     { return btn_cancelar_aten; }
    public javax.swing.JTextArea             getTaComentarios()   { return ta_comentarios; }
    public javax.swing.JTable                getTblReceta()       { return tbl_receta; }
    public javax.swing.JComboBox<String>     getCbMedicamento()   { return cbMedicamento; }
    public javax.swing.JTextField            getTxtDosisMed()     { return txtDosisMed; }
    public javax.swing.JTextField            getTxtDuraMed()      { return txtDuraMed; }
    public javax.swing.JButton               getBtnAgregarMed()   { return btnAgregarMed; }

    // Nuevos getters para CIE
    public javax.swing.JTextField            getTxtBuscarCie()    { return txtBuscarCie; }
    public javax.swing.JButton               getBtnBuscarCie()    { return btnBuscarCie; }
    public javax.swing.JList<String>         getLstResultadosCie(){ return lstResultadosCie; }
    public javax.swing.JButton               getBtnAgregarCie()   { return btnAgregarCie; }
    public javax.swing.JTable                getTblDiagnosticos() { return tblDiagnosticos; }
    public javax.swing.JButton               getBtnQuitarCie()    { return btnQuitarCie; }

    public void setDatosPaciente(String nombre, String codigo, String edad) {
        lbl_nombre_pac.setText(nombre);
        lbl_codigo_pac.setText("Código: " + codigo);
        lbl_edad_pac.setText("Edad: " + edad);
    }

    // Campos declarados programáticamente (no generados por GUI Builder)
    private javax.swing.JComboBox<String> cbMedicamento;
    private javax.swing.JTextField        txtDosisMed;
    private javax.swing.JTextField        txtDuraMed;
    private javax.swing.JButton           btnAgregarMed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancelar_aten;
    private javax.swing.JButton btn_guardar_aten;
    private javax.swing.JLabel lbl_alergias;
    private javax.swing.JLabel lbl_alergias_titulo;
    private javax.swing.JLabel lbl_cie;
    private javax.swing.JLabel lbl_codigo_pac;
    private javax.swing.JLabel lbl_comentarios;
    private javax.swing.JLabel lbl_edad_pac;
    private javax.swing.JLabel lbl_nombre_pac;
    private javax.swing.JLabel lbl_receta;
    private javax.swing.JButton btnAgregarCie;
    private javax.swing.JButton btnBuscarCie;
    private javax.swing.JButton btnQuitarCie;
    private javax.swing.JList<String> lstResultadosCie;
    private javax.swing.JPanel pnl_botones;
    private javax.swing.JPanel pnl_formulario;
    private javax.swing.JPanel pnl_paciente;
    private javax.swing.JScrollPane scrl_comentarios;
    private javax.swing.JScrollPane scrlDiagnosticos;
    private javax.swing.JScrollPane scrl_receta;
    private javax.swing.JScrollPane scrlResultadosCie;
    private javax.swing.JSeparator sep_datos;
    private javax.swing.JTextArea ta_comentarios;
    private javax.swing.JTable tblDiagnosticos;
    private javax.swing.JTable tbl_receta;
    private javax.swing.JTextField txtBuscarCie;
    // End of variables declaration//GEN-END:variables
}
