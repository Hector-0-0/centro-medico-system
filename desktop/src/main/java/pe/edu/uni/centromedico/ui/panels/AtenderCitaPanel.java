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

        // Formulario de atención (columna derecha)
        pnl_botones.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 0", "[grow][grow]", "[]"
        ));
        pnl_botones.removeAll();
        pnl_botones.add(btn_cancelar_aten, "growx, h 40!");
        pnl_botones.add(btn_guardar_aten,  "growx, h 40!");

        pnl_formulario.setLayout(new net.miginfocom.swing.MigLayout(
            "fill, insets 20", "[grow]",
            "[]6[80!]16[]6[80!]16[]6[100!]8[]16[]"
        ));
        pnl_formulario.removeAll();
        pnl_formulario.add(lbl_diag,    "wrap");
        pnl_formulario.add(scrl_diag,   "growx, wrap");
        pnl_formulario.add(lbl_trat,    "wrap");
        pnl_formulario.add(scrl_trat,   "growx, wrap");
        pnl_formulario.add(lbl_receta,  "wrap");
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
        lbl_diag = new javax.swing.JLabel();
        scrl_diag = new javax.swing.JScrollPane();
        ta_diagnostico = new javax.swing.JTextArea();
        lbl_trat = new javax.swing.JLabel();
        scrl_trat = new javax.swing.JScrollPane();
        ta_tratamiento = new javax.swing.JTextArea();
        lbl_receta = new javax.swing.JLabel();
        scrl_receta = new javax.swing.JScrollPane();
        tbl_receta = new javax.swing.JTable();
        pnl_botones = new javax.swing.JPanel();
        btn_guardar_aten = new javax.swing.JButton();
        btn_cancelar_aten = new javax.swing.JButton();

        pnl_paciente.setBackground(new java.awt.Color(249, 245, 240));

        lbl_nombre_pac.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        lbl_nombre_pac.setText("Nombre del Paciente");

        lbl_codigo_pac.setText("Código: —");

        lbl_edad_pac.setText("Edad: —");

        lbl_alergias_titulo.setText("Alergias");

        lbl_alergias.setText("—");

        javax.swing.GroupLayout pnl_pacienteLayout = new javax.swing.GroupLayout(pnl_paciente);
        pnl_paciente.setLayout(pnl_pacienteLayout);
        pnl_pacienteLayout.setHorizontalGroup(
            pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_pacienteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_nombre_pac)
                .addGap(24, 24, 24))
            .addGroup(pnl_pacienteLayout.createSequentialGroup()
                .addGroup(pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_pacienteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_alergias)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_codigo_pac)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_alergias_titulo))
                    .addGroup(pnl_pacienteLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(lbl_edad_pac)
                        .addGap(18, 18, 18)
                        .addComponent(sep_datos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_pacienteLayout.setVerticalGroup(
            pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_pacienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_edad_pac)
                    .addComponent(sep_datos, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(lbl_nombre_pac)
                .addGroup(pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_pacienteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_pacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_codigo_pac)
                            .addComponent(lbl_alergias_titulo))
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_pacienteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_alergias)
                        .addContainerGap())))
        );

        pnl_formulario.setBackground(new java.awt.Color(255, 255, 255));

        lbl_diag.setText("Diagnóstico");

        ta_diagnostico.setColumns(20);
        ta_diagnostico.setRows(5);
        scrl_diag.setViewportView(ta_diagnostico);

        lbl_trat.setText("Tratamiento");

        ta_tratamiento.setColumns(20);
        ta_tratamiento.setRows(5);
        scrl_trat.setViewportView(ta_tratamiento);

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

        javax.swing.GroupLayout pnl_formularioLayout = new javax.swing.GroupLayout(pnl_formulario);
        pnl_formulario.setLayout(pnl_formularioLayout);
        pnl_formularioLayout.setHorizontalGroup(
            pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_formularioLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lbl_diag)
                .addGap(18, 18, 18)
                .addComponent(lbl_receta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_formularioLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_formularioLayout.createSequentialGroup()
                        .addComponent(scrl_receta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(scrl_diag, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrl_trat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_formularioLayout.createSequentialGroup()
                        .addComponent(lbl_trat)
                        .addGap(18, 18, 18))))
        );
        pnl_formularioLayout.setVerticalGroup(
            pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_formularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrl_receta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_formularioLayout.createSequentialGroup()
                        .addComponent(lbl_trat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_diag)
                            .addComponent(lbl_receta))
                        .addGroup(pnl_formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_formularioLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(scrl_diag, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_formularioLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrl_trat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        btn_guardar_aten.setText("Guardar Consulta");

        btn_cancelar_aten.setText("Cancelar");

        javax.swing.GroupLayout pnl_botonesLayout = new javax.swing.GroupLayout(pnl_botones);
        pnl_botones.setLayout(pnl_botonesLayout);
        pnl_botonesLayout.setHorizontalGroup(
            pnl_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_botonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_guardar_aten)
                    .addComponent(btn_cancelar_aten))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_botonesLayout.setVerticalGroup(
            pnl_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_botonesLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btn_cancelar_aten)
                .addGap(18, 18, 18)
                .addComponent(btn_guardar_aten)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(pnl_paciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnl_botones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(pnl_formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(pnl_paciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(pnl_botones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // ── API pública para AtenderCitaController ───────────────────────────
    public javax.swing.JButton               getBtnGuardar()      { return btn_guardar_aten; }
    public javax.swing.JButton               getBtnCancelar()     { return btn_cancelar_aten; }
    public javax.swing.JTextArea             getTaDiagnostico()   { return ta_diagnostico; }
    public javax.swing.JTextArea             getTaTratamiento()   { return ta_tratamiento; }
    public javax.swing.JTable                getTblReceta()       { return tbl_receta; }
    public javax.swing.JComboBox<String>     getCbMedicamento()   { return cbMedicamento; }
    public javax.swing.JTextField            getTxtDosisMed()     { return txtDosisMed; }
    public javax.swing.JTextField            getTxtDuraMed()      { return txtDuraMed; }
    public javax.swing.JButton               getBtnAgregarMed()   { return btnAgregarMed; }

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
    private javax.swing.JLabel lbl_codigo_pac;
    private javax.swing.JLabel lbl_diag;
    private javax.swing.JLabel lbl_edad_pac;
    private javax.swing.JLabel lbl_nombre_pac;
    private javax.swing.JLabel lbl_receta;
    private javax.swing.JLabel lbl_trat;
    private javax.swing.JPanel pnl_botones;
    private javax.swing.JPanel pnl_formulario;
    private javax.swing.JPanel pnl_paciente;
    private javax.swing.JScrollPane scrl_diag;
    private javax.swing.JScrollPane scrl_receta;
    private javax.swing.JScrollPane scrl_trat;
    private javax.swing.JSeparator sep_datos;
    private javax.swing.JTextArea ta_diagnostico;
    private javax.swing.JTextArea ta_tratamiento;
    private javax.swing.JTable tbl_receta;
    // End of variables declaration//GEN-END:variables
}
