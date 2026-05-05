package pe.edu.uni.centromedico.ui.panels;

public class HistorialPanel extends javax.swing.JPanel {


    public HistorialPanel() {
        initComponents();

        // Buscador: campo crece, botón a la derecha
        pnl_busqueda.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 0", "[grow][]", "[36!]"
        ));
        pnl_busqueda.removeAll();
        pnl_busqueda.add(txt_buscar, "growx, h 36!");
        pnl_busqueda.add(btn_buscar, "h 36!, gapleft 8");


        //botones de filtro 

        javax.swing.JComboBox<String> btn_fecha = new javax.swing.JComboBox<>();
        btn_fecha.addItem("Todas");
        btn_fecha.addItem("Hoy");
        btn_fecha.addItem("Esta semana");
        btn_fecha.addItem("Este mes");

        // Tabla
        tbl_citas.setRowHeight(34);
        scrl_citas.setBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232,221,216)));

        // Layout: título / buscador / tabla
        this.setLayout(new net.miginfocom.swing.MigLayout(
            "fill, insets 24", "[grow]", "[]12[]-50[grow]"
        ));
        this.removeAll();
        this.add(lbl_titulo,    "wrap");
        this.add(pnl_busqueda,  "growx, wrap");
        this.add(btn_fecha,     "wrap");
        this.add(scrl_citas,    "grow");
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
