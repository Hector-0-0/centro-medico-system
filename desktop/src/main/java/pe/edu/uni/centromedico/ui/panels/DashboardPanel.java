package pe.edu.uni.centromedico.ui.panels;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import pe.edu.uni.centromedico.ui.dialogs.NuevaCitaDialog;

public class DashboardPanel extends javax.swing.JPanel {
    
    private java.util.ArrayList<String[]> listaDatos = new java.util.ArrayList<>();

    public DashboardPanel() {
        initComponents();

        // Filtros de horarios: fila horizontal
        pnl_filtros.setLayout(new net.miginfocom.swing.MigLayout(
                "insets 0, gapx 8", "[][][]", "[]"));
        pnl_filtros.removeAll();
        pnl_filtros.add(btn_todos);
        pnl_filtros.add(btn_disponibles);
        pnl_filtros.add(btn_ocupados);

        // Tabla responsive
        tbl_horarios.setRowHeight(36);
        scrl_horarios.setBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 221, 216)));

        InputStream data_doctor = getClass().getResourceAsStream("/data/data_doctor.txt");

        try (Scanner myreader = new Scanner(data_doctor)) {
            while (myreader.hasNextLine()) {
                String data = myreader.nextLine();
                String[] data_split = data.split("\\|");
                listaDatos.add(data_split);
            }
        } catch (Exception e) {
            System.out.println("Error al leer: " + e.getMessage());
        }

        filtrar("Todos");

        String[][] datosTabla = new String[listaDatos.size()][6];

        for (int i = 0; i < listaDatos.size(); i++) {
            String[] filaT = listaDatos.get(i);
            for(int j = 0; j < 6; j++){
                if(j==5){
                    if(filaT[j+2].equals("1")){
                        datosTabla[i][j] = "Disponible";
                    }
                    else{
                        datosTabla[i][j] = "Ocupado";
                    }
                }
                else{
                datosTabla[i][j] = filaT[j+2];
                }
            }
        }

        // Crear modelo con datos reales
        tbl_horarios.setModel(new javax.swing.table.DefaultTableModel(
                datosTabla,
                new String[] { "Especialidad", "Médico", "Día", "Hora", "Consultorio", "Estado" }
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }); 
        for (int i = 0; i < tbl_horarios.getColumnCount(); i++) {
            tbl_horarios.getColumnModel().getColumn(i).setResizable(false);
        }
        tbl_horarios.setColumnSelectionAllowed(false);
        tbl_horarios.getTableHeader().setReorderingAllowed(false);

        scrl_horarios.setViewportView(tbl_horarios);
        btn_agendar.addActionListener(e->{
            NuevaCitaDialog dialogNuevaCita = new NuevaCitaDialog(null, true);
            dialogNuevaCita.setVisible(true);
        });
        // Layout principal: título / subtítulo / filtros / tabla / botón
        this.setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[grow]", "[]4[]12[]12[grow]16[]"));
        this.removeAll();
        this.add(lbl_titulo, "wrap");
        this.add(lbl_subtitulo, "wrap");
        this.add(pnl_filtros, "wrap");
        this.add(scrl_horarios, "grow, wrap");
        this.add(btn_agendar, "right, h 44!, w 200!");

    }
    
    private void filtrar(String tipo) {
    
    ((javax.swing.table.DefaultTableModel) tbl_horarios.getModel()).setRowCount(0);

    // 2. CREAR UNA LISTA TEMPORAL PARA EL FILTRO
    // Necesitamos saber qué filas pasan el filtro antes de crear la matriz
    java.util.List<String[]> filtrados = new java.util.ArrayList<>();
    for (String[] f : listaDatos) {
        if (tipo.equals("Todos") || f[7].equals(tipo)) {
            filtrados.add(f);
        }
    }
    String[][] datosTabla = new String[filtrados.size()][6];

    for (int i = 0; i < filtrados.size(); i++) {
        String[] filaT = filtrados.get(i);
        for (int j = 0; j < 6; j++) {
            if (j == 5) {
                if (filaT[j + 2].equals("1")) {
                    datosTabla[i][j] = "Disponible";
                } else {
                    datosTabla[i][j] = "Ocupado";
                }
            } else {
                datosTabla[i][j] = filaT[j + 2];
            }
        }
    }
    // 4. PASAR LA MATRIZ AL MODELO
    javax.swing.table.DefaultTableModel mod = (javax.swing.table.DefaultTableModel) tbl_horarios.getModel();
    for (String[] fila : datosTabla) {
        mod.addRow(fila);
    }
}
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_titulo = new javax.swing.JLabel();
        lbl_subtitulo = new javax.swing.JLabel();
        pnl_filtros = new javax.swing.JPanel();
        btn_disponibles = new javax.swing.JButton();
        btn_ocupados = new javax.swing.JButton();
        btn_todos = new javax.swing.JButton();
        scrl_horarios = new javax.swing.JScrollPane();
        tbl_horarios = new javax.swing.JTable();
        btn_agendar = new javax.swing.JButton();

        setBackground(new java.awt.Color(249, 245, 240));

        lbl_titulo.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        lbl_titulo.setText("Horarios Disponibles");

        lbl_subtitulo.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        lbl_subtitulo.setText("Selecciona un horario y agenda tu cita");

        pnl_filtros.setOpaque(false);

        btn_disponibles.setText("Disponibles");
        btn_disponibles.addActionListener(this::btn_disponiblesActionPerformed);

        btn_ocupados.setText("Ocupados");
        btn_ocupados.addActionListener(this::btn_ocupadosActionPerformed);

        btn_todos.setText("Todos");
        btn_todos.addActionListener(this::btn_todosActionPerformed);

        javax.swing.GroupLayout pnl_filtrosLayout = new javax.swing.GroupLayout(pnl_filtros);
        pnl_filtros.setLayout(pnl_filtrosLayout);
        pnl_filtrosLayout.setHorizontalGroup(
            pnl_filtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_filtrosLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(pnl_filtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_filtrosLayout.createSequentialGroup()
                        .addComponent(btn_disponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_filtrosLayout.createSequentialGroup()
                        .addComponent(btn_ocupados)
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_filtrosLayout.createSequentialGroup()
                        .addComponent(btn_todos)
                        .addGap(38, 38, 38))))
        );
        pnl_filtrosLayout.setVerticalGroup(
            pnl_filtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_filtrosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_todos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_disponibles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ocupados)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        tbl_horarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Especialidad", "Médico", "Día", "Hora", "Consultorio", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_horarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        scrl_horarios.setViewportView(tbl_horarios);
        if (tbl_horarios.getColumnModel().getColumnCount() > 0) {
            tbl_horarios.getColumnModel().getColumn(0).setResizable(false);
            tbl_horarios.getColumnModel().getColumn(1).setResizable(false);
            tbl_horarios.getColumnModel().getColumn(2).setResizable(false);
            tbl_horarios.getColumnModel().getColumn(3).setResizable(false);
            tbl_horarios.getColumnModel().getColumn(4).setResizable(false);
            tbl_horarios.getColumnModel().getColumn(5).setResizable(false);
        }

        btn_agendar.setText("Agendar Cita");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lbl_titulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_subtitulo)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(pnl_filtros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btn_agendar)
                        .addGap(18, 18, 18)
                        .addComponent(scrl_horarios, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(125, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lbl_titulo)
                .addGap(18, 18, 18)
                .addComponent(lbl_subtitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_filtros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrl_horarios, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_agendar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_todosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_todosActionPerformed
            filtrar("Todos");
    }//GEN-LAST:event_btn_todosActionPerformed

    private void btn_disponiblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_disponiblesActionPerformed
        filtrar("1");
    }//GEN-LAST:event_btn_disponiblesActionPerformed

    private void btn_ocupadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ocupadosActionPerformed
        filtrar("0");
    }//GEN-LAST:event_btn_ocupadosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_agendar;
    private javax.swing.JButton btn_disponibles;
    private javax.swing.JButton btn_ocupados;
    private javax.swing.JButton btn_todos;
    private javax.swing.JLabel lbl_subtitulo;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPanel pnl_filtros;
    private javax.swing.JScrollPane scrl_horarios;
    private javax.swing.JTable tbl_horarios;
    // End of variables declaration//GEN-END:variables
}
