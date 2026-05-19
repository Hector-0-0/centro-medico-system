package pe.edu.uni.centromedico.ui.panels;

public class CitaPanel extends javax.swing.JPanel {

public CitaPanel() {
    initComponents();

    // Fecha del día actual en español
    String hoy = java.time.LocalDate.now().format(
        java.time.format.DateTimeFormatter.ofPattern(
            "EEEE dd 'de' MMMM, yyyy", java.util.Locale.forLanguageTag("es")));
    lbl_fecha_hoy.setText(Character.toUpperCase(hoy.charAt(0)) + hoy.substring(1));

    // Tabla
    tbl_citas_medico.setRowHeight(40);
    scrl_citas_medico.setBorder(
        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232,221,216)));

    // Layout: título / fecha / tabla
    this.setLayout(new net.miginfocom.swing.MigLayout(
        "fill, insets 24", "[grow]", "[]4[]16[grow]"
    ));
    this.removeAll();
    this.add(lbl_titulo,          "wrap");
    this.add(lbl_fecha_hoy,       "wrap");
    this.add(scrl_citas_medico,   "grow");
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_titulo = new javax.swing.JLabel();
        lbl_fecha_hoy = new javax.swing.JLabel();
        scrl_citas_medico = new javax.swing.JScrollPane();
        tbl_citas_medico = new javax.swing.JTable();

        lbl_titulo.setText("Mis Citas del Día");

        lbl_fecha_hoy.setText("(se llena dinámicamente)");

        tbl_citas_medico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Hora", "Paciente", "Especialidad", "Estado", "Acción"
            }
        ));
        scrl_citas_medico.setViewportView(tbl_citas_medico);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lbl_titulo)
                        .addGap(30, 30, 30)
                        .addComponent(scrl_citas_medico, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(lbl_fecha_hoy)))
                .addContainerGap(126, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(lbl_titulo)
                        .addGap(77, 77, 77))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrl_citas_medico, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(lbl_fecha_hoy)
                .addContainerGap(120, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // ── API pública para CitaController ──────────────────────────────────
    public javax.swing.JTable getTblCitas()          { return tbl_citas_medico; }
    public void setFecha(String fecha)               { lbl_fecha_hoy.setText(fecha); }
    public int getFilaSeleccionada()                 { return tbl_citas_medico.getSelectedRow(); }
    public Object getValueAt(int row, int col)       { return tbl_citas_medico.getValueAt(row, col); }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbl_fecha_hoy;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JScrollPane scrl_citas_medico;
    private javax.swing.JTable tbl_citas_medico;
    // End of variables declaration//GEN-END:variables
}
