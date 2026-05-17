package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.ui.components.TablaManager;
import pe.edu.uni.centromedico.ui.dialogs.NuevaCitaDialog;
import pe.edu.uni.centromedico.models.Horario;
import pe.edu.uni.centromedico.models.Persona;

import java.util.List;

import pe.edu.uni.centromedico.db.dao.HorarioDAO;

public class DashboardPanel extends javax.swing.JPanel {

        Persona persona = null;

        private TablaManager<Horario> tablaManager;

        public DashboardPanel(Persona persona) {
                initComponents();
                this.persona = persona;

                // Filtros de horarios: fila horizontal
                pnl_filtros.setLayout(new net.miginfocom.swing.MigLayout(
                                "insets 0, gapx 8", "[][][]", "[]"));
                pnl_filtros.removeAll();
                pnl_filtros.add(cbx_Especialidad);
                pnl_filtros.add(btn_Todos);
                pnl_filtros.add(btn_disponibles);
                pnl_filtros.add(btn_ocupados);

                // tabla responsive
                tablaManager = new TablaManager<>(
                                tbl_horarios,
                                new String[] { "Especialidad", "Médico", "Día", "Hora", "Consultorio", "Estado" },
                                java.util.List.of(
                                                Horario::getEspecialidad,
                                                Horario::getNombreDoctor,
                                                Horario::getDiaSemana,
                                                h -> h.getHoraInicio() + " - " + h.getHoraFin(),
                                                Horario::getConsultorio,
                                                h -> h.isDisponible() ? "Disponible" : "Ocupado"));

                List<Horario> datos = new HorarioDAO().obtenerTodos();
                tablaManager.cargar(datos);
                scrl_horarios.setBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 221, 216)));

                // Llenar combobox de especialidades
                cbx_Especialidad.removeAllItems();
                cbx_Especialidad.addItem("Seleccione Especialidad");
                datos.stream()
                                .map(Horario::getEspecialidad)
                                .distinct()
                                .forEach(cbx_Especialidad::addItem);

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

        private String ultimo = "Todos";

        private void filtrar(String tipo) {
                String especialidad = (String) cbx_Especialidad.getSelectedItem();
                boolean todasEspecialidades = especialidad == null
                                || especialidad.equals("Seleccione Especialidad");

                tablaManager.filtrarPorCondicion(h -> {
                        boolean coincideEspecialidad = todasEspecialidades
                                        || h.getEspecialidad().equals(especialidad);

                        boolean coincideEstado = tipo.equals("Todos")
                                        || (tipo.equals("1") && h.isDisponible())
                                        || (tipo.equals("0") && !h.isDisponible());

                        return coincideEspecialidad && coincideEstado;
                });
        }

        
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                lbl_titulo = new javax.swing.JLabel();
                lbl_subtitulo = new javax.swing.JLabel();
                pnl_filtros = new javax.swing.JPanel();
                btn_disponibles = new javax.swing.JButton();
                btn_ocupados = new javax.swing.JButton();
                scrl_horarios = new javax.swing.JScrollPane();
                tbl_horarios = new javax.swing.JTable();
                btn_agendar = new javax.swing.JButton();
                cbx_Especialidad = new javax.swing.JComboBox<>();
                btn_Todos = new javax.swing.JButton();

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

                javax.swing.GroupLayout pnl_filtrosLayout = new javax.swing.GroupLayout(pnl_filtros);
                pnl_filtros.setLayout(pnl_filtrosLayout);
                pnl_filtrosLayout.setHorizontalGroup(
                                pnl_filtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnl_filtrosLayout.createSequentialGroup()
                                                                .addContainerGap(27, Short.MAX_VALUE)
                                                                .addGroup(pnl_filtrosLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                pnl_filtrosLayout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(btn_disponibles,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                136,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addContainerGap())
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                pnl_filtrosLayout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(btn_ocupados)
                                                                                                                .addGap(32, 32, 32)))));
                pnl_filtrosLayout.setVerticalGroup(
                                pnl_filtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnl_filtrosLayout.createSequentialGroup()
                                                                .addGap(34, 34, 34)
                                                                .addComponent(btn_disponibles)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btn_ocupados)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                tbl_horarios.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null, null },
                                                { null, null, null, null, null, null },
                                                { null, null, null, null, null, null },
                                                { null, null, null, null, null, null }
                                },
                                new String[] {
                                                "Especialidad", "Médico", "Día", "Hora", "Consultorio", "Estado"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
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
                btn_agendar.addActionListener(this::btn_agendarActionPerformed);

                cbx_Especialidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Especialidad" }));
                cbx_Especialidad.addActionListener(this::cbx_EspecialidadActionPerformed);

                btn_Todos.setText("Todos");
                btn_Todos.addActionListener(this::btn_TodosActionPerformed);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(36, 36, 36)
                                                                                                .addComponent(lbl_titulo))
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(67, 67, 67)
                                                                                                .addGroup(layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lbl_subtitulo)
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addGap(6, 6, 6)
                                                                                                                                .addComponent(pnl_filtros,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addGroup(layout.createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                                                .addGap(26, 26, 26)
                                                                                                                                                                .addComponent(cbx_Especialidad,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                                                .addGap(18, 18, 18)
                                                                                                                                                                .addComponent(btn_Todos))))))
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(8, 8, 8)
                                                                                                .addComponent(btn_agendar)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(scrl_horarios,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                100,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(29, Short.MAX_VALUE)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(31, 31, 31)
                                                                .addComponent(lbl_titulo)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(lbl_subtitulo)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(pnl_filtros,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(cbx_Especialidad,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(52, 52, 52)
                                                                                                .addComponent(btn_Todos)))
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(scrl_horarios,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                100,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addContainerGap())
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(btn_agendar)
                                                                                                .addContainerGap(
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)))));
        }// </editor-fold>//GEN-END:initComponents

        private void cbx_EspecialidadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbx_EspecialidadActionPerformed
                filtrar(ultimo);
        }// GEN-LAST:event_cbx_EspecialidadActionPerformed

        private void btn_agendarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_agendarActionPerformed
                NuevaCitaDialog dialogNuevaCita = new NuevaCitaDialog(null, true, persona);
                dialogNuevaCita.setVisible(true);
        }// GEN-LAST:event_btn_agendarActionPerformed

        private void btn_TodosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_TodosActionPerformed
                ultimo = "Todos";
                filtrar(ultimo);
        }// GEN-LAST:event_btn_TodosActionPerformed

        private void btn_disponiblesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_disponiblesActionPerformed
                ultimo = "1";
                filtrar(ultimo);
        }// GEN-LAST:event_btn_disponiblesActionPerformed

        private void btn_ocupadosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_ocupadosActionPerformed
                ultimo = "0";
                filtrar(ultimo);
        }// GEN-LAST:event_btn_ocupadosActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btn_Todos;
        private javax.swing.JButton btn_agendar;
        private javax.swing.JButton btn_disponibles;
        private javax.swing.JButton btn_ocupados;
        private javax.swing.JComboBox<String> cbx_Especialidad;
        private javax.swing.JLabel lbl_subtitulo;
        private javax.swing.JLabel lbl_titulo;
        private javax.swing.JPanel pnl_filtros;
        private javax.swing.JScrollPane scrl_horarios;
        private javax.swing.JTable tbl_horarios;
        // End of variables declaration//GEN-END:variables
}
