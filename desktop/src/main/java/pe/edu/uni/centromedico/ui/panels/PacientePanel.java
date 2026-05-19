package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.db.dao.EstudianteDAO;
import pe.edu.uni.centromedico.models.Estudiante;
import pe.edu.uni.centromedico.ui.dialogs.NuevaCitaDialog;
import pe.edu.uni.centromedico.ui.dialogs.ErrorDialog;
import java.util.List;

public class PacientePanel extends javax.swing.JPanel {

    public PacientePanel() {
        initComponents();

        tbl_pacientes.setRowHeight(36);
        scrl_pacientes.setBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 221, 216)));

        cargarEstudiantes();
        for (int i = 0; i < tbl_pacientes.getColumnCount(); i++) {
            tbl_pacientes.getColumnModel().getColumn(i).setResizable(false);
        }
        tbl_pacientes.setColumnSelectionAllowed(false);
        tbl_pacientes.getTableHeader().setReorderingAllowed(false);
        scrl_pacientes.setViewportView(tbl_pacientes);

        this.setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[grow]", "[]4[]12[]12[grow]16[]"));
        this.removeAll();
        this.add(lbl_titulo, "grow");
        this.add(txt_buscar_pac, "grow");
        this.add(btn_buscar_pac, "grow");
        this.add(btn_eliminar_pac, "grow");
        this.add(btn_nuevo_pac, "grow, wrap");
        this.add(scrl_pacientes, "span 5,grow, wrap");
    }

    private void cargarEstudiantes() {
        List<Estudiante> estudiantes = new EstudianteDAO().obtenerTodos();
        String[][] datos = new String[estudiantes.size()][5];
        for (int i = 0; i < estudiantes.size(); i++) {
            Estudiante e = estudiantes.get(i);
            datos[i][0] = e.getId();
            datos[i][1] = e.getNombre();
            datos[i][2] = String.valueOf(e.getEdad());
            datos[i][3] = e.getCarrera();
            datos[i][4] = e.getEmail();
        }
        tbl_pacientes.setModel(new javax.swing.table.DefaultTableModel(
                datos,
                new String[]{"Código", "Nombre", "Edad", "Carrera", "Email"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_titulo = new javax.swing.JLabel();
        pnl_top = new javax.swing.JPanel();
        txt_buscar_pac = new javax.swing.JTextField();
        btn_buscar_pac = new javax.swing.JButton();
        btn_nuevo_pac = new javax.swing.JButton();
        btn_eliminar_pac = new javax.swing.JButton();
        scrl_pacientes = new javax.swing.JScrollPane();
        tbl_pacientes = new javax.swing.JTable();

        setBackground(new java.awt.Color(249, 245, 240));

        lbl_titulo.setText("Gestión de Pacientes");

        pnl_top.setOpaque(false);

        txt_buscar_pac.setText("Buscar por nombre...");

        btn_buscar_pac.setText("Buscar");

        btn_nuevo_pac.setText("+ Nuevo Paciente");

        btn_eliminar_pac.setText("Eliminar");

        tbl_pacientes.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Código", "Nombre", " Edad", "Carrera", "Email"
                }));
        scrl_pacientes.setViewportView(tbl_pacientes);

        javax.swing.GroupLayout pnl_topLayout = new javax.swing.GroupLayout(pnl_top);
        pnl_top.setLayout(pnl_topLayout);
        pnl_topLayout.setHorizontalGroup(
                pnl_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_topLayout.createSequentialGroup()
                                .addGroup(pnl_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_buscar_pac, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnl_topLayout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addGroup(pnl_topLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btn_nuevo_pac)
                                                        .addGroup(pnl_topLayout.createSequentialGroup()
                                                                .addComponent(btn_buscar_pac)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btn_eliminar_pac)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrl_pacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(19, Short.MAX_VALUE)));
        pnl_topLayout.setVerticalGroup(
                pnl_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_topLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(pnl_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrl_pacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnl_topLayout.createSequentialGroup()
                                                .addComponent(txt_buscar_pac, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(pnl_topLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnl_topLayout.createSequentialGroup()
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btn_buscar_pac))
                                                        .addGroup(pnl_topLayout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(btn_eliminar_pac)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btn_nuevo_pac)))
                                .addContainerGap(44, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(43, 43, 43)
                                                .addComponent(lbl_titulo))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addComponent(pnl_top, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(92, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(lbl_titulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnl_top, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(64, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    // ── API pública para PacienteController ──────────────────────────────
    public javax.swing.JTable   getTblPacientes()   { return tbl_pacientes; }
    public javax.swing.JButton  getBtnBuscar()       { return btn_buscar_pac; }
    public javax.swing.JButton  getBtnNuevo()        { return btn_nuevo_pac; }
    public javax.swing.JButton  getBtnEliminar()     { return btn_eliminar_pac; }
    public javax.swing.JTextField getTxtBuscar()     { return txt_buscar_pac; }
    public int getFilaSeleccionada()                 { return tbl_pacientes.getSelectedRow(); }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar_pac;
    private javax.swing.JButton btn_eliminar_pac;
    private javax.swing.JButton btn_nuevo_pac;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPanel pnl_top;
    private javax.swing.JScrollPane scrl_pacientes;
    private javax.swing.JTable tbl_pacientes;
    private javax.swing.JTextField txt_buscar_pac;
    // End of variables declaration//GEN-END:variables
}
