package pe.edu.uni.centromedico.ui.panels;

public class MedicoPanel extends javax.swing.JPanel {

    private javax.swing.JLabel     lbl_titulo;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JButton    btn_buscar;
    private javax.swing.JButton    btn_nuevo;
    private javax.swing.JScrollPane scrl_medicos;
    private javax.swing.JTable     tbl_medicos;

    public MedicoPanel() {
        initComponents();

        this.setBackground(new java.awt.Color(249, 245, 240));
        this.setLayout(new net.miginfocom.swing.MigLayout(
            "fill, insets 24", "[grow]", "[]12[]12[grow]"));

        javax.swing.JPanel pnl_top = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][][][]", "[36!]"));
        pnl_top.setOpaque(false);
        pnl_top.add(txt_buscar, "growx, h 36!");
        pnl_top.add(btn_buscar,  "h 36!, gapleft 8");
        pnl_top.add(btn_nuevo,   "h 36!, gapleft 4");

        tbl_medicos.setRowHeight(36);
        scrl_medicos.setBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 221, 216)));

        this.add(lbl_titulo,  "wrap");
        this.add(pnl_top,     "growx, wrap");
        this.add(scrl_medicos,"grow");
    }

    // ── API pública para MedicoController ────────────────────────────────
    public javax.swing.JTable     getTblMedicos()   { return tbl_medicos; }
    public javax.swing.JButton    getBtnBuscar()    { return btn_buscar; }
    public javax.swing.JButton    getBtnNuevo()     { return btn_nuevo; }
    public javax.swing.JTextField getTxtBuscar()    { return txt_buscar; }
    public int getFilaSeleccionada()                { return tbl_medicos.getSelectedRow(); }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_titulo  = new javax.swing.JLabel();
        txt_buscar  = new javax.swing.JTextField();
        btn_buscar  = new javax.swing.JButton();
        btn_nuevo   = new javax.swing.JButton();
        tbl_medicos = new javax.swing.JTable();
        scrl_medicos = new javax.swing.JScrollPane();

        lbl_titulo.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(30, 41, 59));
        lbl_titulo.setText("Gestión de Médicos");

        txt_buscar.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        txt_buscar.setText("Buscar por nombre...");

        btn_buscar.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        btn_buscar.setText("Buscar");

        btn_nuevo.setBackground(new java.awt.Color(113, 22, 16));
        btn_nuevo.setFont(new java.awt.Font("Liberation Sans", 1, 12)); // NOI18N
        btn_nuevo.setForeground(java.awt.Color.WHITE);
        btn_nuevo.setText("+ Nuevo Médico");
        btn_nuevo.setBorderPainted(false);
        btn_nuevo.setFocusPainted(false);

        tbl_medicos.setBackground(java.awt.Color.WHITE);
        tbl_medicos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrl_medicos.setViewportView(tbl_medicos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
