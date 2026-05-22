package pe.edu.uni.centromedico.ui.panels;

public class MedicamentoPanel extends javax.swing.JPanel {

    public MedicamentoPanel() {
        initComponents();

        // Barra de acciones
        pnl_acciones.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 0", "[grow][][][]", "[36!]"
        ));
        pnl_acciones.removeAll();
        pnl_acciones.add(txt_buscar_med, "growx, h 36!");
        pnl_acciones.add(btn_buscar_med, "h 36!, gapleft 8");
        // btn_agregar_med no se muestra: esta vista es solo lectura para el médico

        // Tabla
        tbl_medicamentos.setRowHeight(36);
        scrl_medicamentos.setBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232,221,216)));

        // Layout: título / acciones / tabla
        this.setLayout(new net.miginfocom.swing.MigLayout(
            "fill, insets 24", "[grow]", "[]12[]12[grow]"
        ));
        this.removeAll();
        this.add(lbl_titulo,        "wrap");
        this.add(pnl_acciones,      "growx, wrap");
        this.add(scrl_medicamentos, "grow");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_titulo = new javax.swing.JLabel();
        pnl_acciones = new javax.swing.JPanel();
        txt_buscar_med = new javax.swing.JTextField();
        btn_buscar_med = new javax.swing.JButton();
        btn_agregar_med = new javax.swing.JButton();
        scrl_medicamentos = new javax.swing.JScrollPane();
        tbl_medicamentos = new javax.swing.JTable();

        lbl_titulo.setText("Inventario de Medicamentos");

        pnl_acciones.setOpaque(false);

        txt_buscar_med.setText("Buscar medicamento...");
        txt_buscar_med.addActionListener(this::txt_buscar_medActionPerformed);

        btn_buscar_med.setText("Buscar");

        btn_agregar_med.setText("+ Agregar");

        javax.swing.GroupLayout pnl_accionesLayout = new javax.swing.GroupLayout(pnl_acciones);
        pnl_acciones.setLayout(pnl_accionesLayout);
        pnl_accionesLayout.setHorizontalGroup(
            pnl_accionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_accionesLayout.createSequentialGroup()
                .addGroup(pnl_accionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_accionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txt_buscar_med, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_accionesLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(pnl_accionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_agregar_med)
                            .addComponent(btn_buscar_med))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_accionesLayout.setVerticalGroup(
            pnl_accionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_accionesLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(txt_buscar_med, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_buscar_med)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_agregar_med)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        tbl_medicamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Stock", "Unidad", "Estado"
            }
        ));
        scrl_medicamentos.setViewportView(tbl_medicamentos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lbl_titulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrl_medicamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(pnl_acciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(lbl_titulo)
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrl_medicamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(pnl_acciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_buscar_medActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscar_medActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_buscar_medActionPerformed

    // ── API pública para MedicamentoController ───────────────────────────
    public javax.swing.JTable     getTblMedicamentos() { return tbl_medicamentos; }
    public javax.swing.JButton    getBtnBuscar()        { return btn_buscar_med; }
    public javax.swing.JButton    getBtnAgregar()       { return btn_agregar_med; }
    public javax.swing.JTextField getTxtBuscar()        { return txt_buscar_med; }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_agregar_med;
    private javax.swing.JButton btn_buscar_med;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPanel pnl_acciones;
    private javax.swing.JScrollPane scrl_medicamentos;
    private javax.swing.JTable tbl_medicamentos;
    private javax.swing.JTextField txt_buscar_med;
    // End of variables declaration//GEN-END:variables
}
