/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pe.edu.uni.centromedico.ui.dialogs;

import javax.swing.JButton;

/**
 *
 * @author hector0-0
 */
public class NuevoPacienteDialog extends javax.swing.JDialog {

        private static final java.util.logging.Logger logger = java.util.logging.Logger
                        .getLogger(NuevoPacienteDialog.class.getName());

        /**
         * Creates new form NuevoPacienteDialog
         */
        public NuevoPacienteDialog(java.awt.Frame parent, boolean modal) {
                super(parent, modal);
                initComponents();

                // 🔴 BORRAR todo lo que agregó el GroupLayout
                this.getContentPane().removeAll();

                // Layout principal del dialog
                this.getContentPane().setLayout(
                                new net.miginfocom.swing.MigLayout(
                                                "fill, insets 0",
                                                "[grow]",
                                                "[54!][grow, fill][50!]"));

                // Layout interno
                panelFormulario.removeAll();

                panelFormulario.setLayout(
                                new net.miginfocom.swing.MigLayout(
                                                "fill, insets 24",
                                                "[grow][grow]",
                                                "[]10[]10[]10[]10[]10[]10[]"));

                // 🔹 FILA 1
                panelFormulario.add(new javax.swing.JLabel("DNI"), "growx");
                javax.swing.JTextField txtDni = new javax.swing.JTextField();
                panelFormulario.add(txtDni, "growx, wrap");

                // 🔹 FILA 2
                panelFormulario.add(new javax.swing.JLabel("Nombre"), "growx");
                javax.swing.JTextField txtNombre = new javax.swing.JTextField();
                panelFormulario.add(txtNombre, "growx, wrap");

                // 🔹 FILA 3
                panelFormulario.add(new javax.swing.JLabel("Apellido"), "growx");
                javax.swing.JTextField txtApellido = new javax.swing.JTextField();
                panelFormulario.add(txtApellido, "growx, wrap");

                // 🔹 FILA 4
                panelFormulario.add(new javax.swing.JLabel("Fecha Nacimiento"), "growx");
                javax.swing.JTextField txtFecha = new javax.swing.JTextField();
                panelFormulario.add(txtFecha, "growx, wrap");

                // 🔹 FILA 5
                panelFormulario.add(new javax.swing.JLabel("Teléfono"), "growx");
                javax.swing.JTextField txtTelefono = new javax.swing.JTextField();
                panelFormulario.add(txtTelefono, "growx, wrap");

                // 🔹 FILA 6
                panelFormulario.add(new javax.swing.JLabel("Grupo Sanguíneo"), "growx");
                javax.swing.JComboBox<String> cbGrupo = new javax.swing.JComboBox<>(
                                new String[] { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" });
                panelFormulario.add(cbGrupo, "growx, wrap");

                // 🔹 FILA 7 (span 2 columnas)
                panelFormulario.add(new javax.swing.JLabel("Dirección"), "span 2, growx, wrap");
                javax.swing.JTextField txtDireccion = new javax.swing.JTextField();
                panelFormulario.add(txtDireccion, "span 2, growx, wrap");

                // 🔹 FILA 8 (span 2 columnas)
                panelFormulario.add(new javax.swing.JLabel("Alergias"), "span 2, growx, wrap");
                javax.swing.JTextArea txtAlergias = new javax.swing.JTextArea(3, 20);
                javax.swing.JScrollPane scrollAlergias = new javax.swing.JScrollPane(txtAlergias);
                panelFormulario.add(scrollAlergias, "span 2, growx, h 70!");

                panelFooter.removeAll();

                panelFooter.setLayout(
                                new net.miginfocom.swing.MigLayout(
                                                "fillx, insets 10 20 10 20",
                                                "[grow][][]",
                                                "[38!]"));

                // Espacio vacío
                panelFooter.add(new javax.swing.JLabel(), "growx");

                // Botón cancelar
                javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
                btnCancelar.setBackground(new java.awt.Color(139, 20, 20));
                btnCancelar.setForeground(java.awt.Color.WHITE);
                btnCancelar.addActionListener(e -> this.dispose());
                panelFooter.add(btnCancelar);

                // Botón guardar
                javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
                btnGuardar.setBackground(new java.awt.Color(139, 20, 20));
                btnGuardar.setForeground(java.awt.Color.WHITE);
                panelFooter.add(btnGuardar);

                panelHeader.removeAll();

                panelHeader.setLayout(
                                new net.miginfocom.swing.MigLayout(
                                                "fill, insets 0 20 0 20",
                                                "[grow]",
                                                "[center]"));

                javax.swing.JLabel titulo = new javax.swing.JLabel("Registrar Paciente");
                titulo.setForeground(java.awt.Color.WHITE);
                titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

                panelHeader.add(titulo, "align left");

                // AGREGAR los paneles manualmente
                this.getContentPane().add(panelHeader, "growx, wrap");
                this.getContentPane().add(panelFormulario, "grow, push, wrap");
                this.getContentPane().add(panelFooter, "growx");
                panelHeader.setPreferredSize(new java.awt.Dimension(0, 54));
                panelFooter.setPreferredSize(new java.awt.Dimension(0, 50));

                this.revalidate();
                this.repaint();
                this.pack();
                this.setLocationRelativeTo(null);
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                panelHeader = new javax.swing.JPanel();
                panelFormulario = new javax.swing.JPanel();
                panelFooter = new javax.swing.JPanel();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("Registrar Pacientes");
                setModal(true);
                setPreferredSize(new java.awt.Dimension(480, 600));
                setResizable(false);

                panelHeader.setBackground(new java.awt.Color(139, 20, 20));

                javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
                panelHeader.setLayout(panelHeaderLayout);
                panelHeaderLayout.setHorizontalGroup(
                                panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));
                panelHeaderLayout.setVerticalGroup(
                                panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));

                javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
                panelFormulario.setLayout(panelFormularioLayout);
                panelFormularioLayout.setHorizontalGroup(
                                panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 96, Short.MAX_VALUE));
                panelFormularioLayout.setVerticalGroup(
                                panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));

                panelFooter.setBackground(new java.awt.Color(240, 235, 230));

                javax.swing.GroupLayout panelFooterLayout = new javax.swing.GroupLayout(panelFooter);
                panelFooter.setLayout(panelFooterLayout);
                panelFooterLayout.setHorizontalGroup(
                                panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));
                panelFooterLayout.setVerticalGroup(
                                panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(146, 146, 146)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(panelFormulario,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(panelHeader,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(154, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(panelFooter,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(140, 140, 140)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(panelHeader,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(panelFormulario,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(panelFooter,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
                /* Set the Nimbus look and feel */
                // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
                // (optional) ">
                /*
                 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
                 * look and feel.
                 * For details see
                 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
                 */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
                        logger.log(java.util.logging.Level.SEVERE, null, ex);
                }
                // </editor-fold>

                /* Create and display the dialog */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                                NuevoPacienteDialog dialog = new NuevoPacienteDialog(new javax.swing.JFrame(), true);
                                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                                        @Override
                                        public void windowClosing(java.awt.event.WindowEvent e) {
                                                System.exit(0);
                                        }
                                });
                                dialog.setVisible(true);
                        }
                });
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel panelFooter;
        private javax.swing.JPanel panelFormulario;
        private javax.swing.JPanel panelHeader;
        // End of variables declaration//GEN-END:variables
}
