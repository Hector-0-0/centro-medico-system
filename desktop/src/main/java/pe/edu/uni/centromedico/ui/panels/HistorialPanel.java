package pe.edu.uni.centromedico.ui.panels;

import javax.swing.table.DefaultTableModel;

public class HistorialPanel extends javax.swing.JPanel {


    public HistorialPanel() {
    initComponents(); // Deja que NetBeans cree los objetos
    
    // 1. Configurar el Panel de Búsqueda
    pnl_busqueda.removeAll(); 
    pnl_busqueda.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][]", "[36!]"));
    
    // Verificamos que no sean nulos antes de agregar
    if (txt_buscar != null && btn_buscar != null) {
        pnl_busqueda.add(txt_buscar, "growx, h 36!");
        pnl_busqueda.add(btn_buscar, "h 36!, gapleft 8");
    }

    // 2. Filtro de fecha (ComboBox)
    javax.swing.JComboBox<String> cmb_fecha = new javax.swing.JComboBox<>(new String[]{
        "Todas", "Hoy", "Esta semana", "Este mes"
    });

    // 3. Configurar Tabla (Limpiamos el modelo previo)
    tbl_citas.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][] {}, 
        new String[] { "Especialidad", "Médico", "Día", "Hora", "Consultorio", "Estado" }
    ) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    });
    tbl_citas.setRowHeight(34);
    tbl_citas.getTableHeader().setReorderingAllowed(false);

    // 4. RE-ESTRUCTURAR EL PANEL PRINCIPAL
    // Esto sobreescribe el GroupLayout de NetBeans con MigLayout
    this.setLayout(new net.miginfocom.swing.MigLayout("fill, insets 24", "[grow]", "[]12[]12[]12[grow]"));
    this.removeAll(); 
    
    this.add(lbl_titulo, "wrap");
    this.add(pnl_busqueda, "growx, wrap");
    this.add(cmb_fecha, "wrap");
    this.add(scrl_citas, "grow");

    this.revalidate();
    this.repaint();
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_titulo = new javax.swing.JLabel();
        pnl_busqueda = new javax.swing.JPanel();
        txt_buscar = new javax.swing.JTextField();
        btn_buscar = new javax.swing.JButton();
        scrl_citas = new javax.swing.JScrollPane();
        tbl_citas = new javax.swing.JTable();

        lbl_titulo.setText("Mis Citas");

        pnl_busqueda.setOpaque(false);

        btn_buscar.setText("Buscar");

        tbl_citas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "N°", "Fecha", "Especialidad", "Médico", "Estado", "Detalle"
            }
        ));
        scrl_citas.setViewportView(tbl_citas);

        javax.swing.GroupLayout pnl_busquedaLayout = new javax.swing.GroupLayout(pnl_busqueda);
        pnl_busqueda.setLayout(pnl_busquedaLayout);
        pnl_busquedaLayout.setHorizontalGroup(
            pnl_busquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_busquedaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(btn_buscar)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_busquedaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(scrl_citas, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnl_busquedaLayout.setVerticalGroup(
            pnl_busquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_busquedaLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(pnl_busquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_buscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrl_citas, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(lbl_titulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(pnl_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lbl_titulo)
                .addGap(33, 33, 33)
                .addComponent(pnl_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPanel pnl_busqueda;
    private javax.swing.JScrollPane scrl_citas;
    private javax.swing.JTable tbl_citas;
    private javax.swing.JTextField txt_buscar;
    // End of variables declaration//GEN-END:variables
}
