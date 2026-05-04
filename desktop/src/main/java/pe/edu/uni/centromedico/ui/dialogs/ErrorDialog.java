/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pe.edu.uni.centromedico.ui.dialogs;

/**
 *
 * @author hector0-0
 */
public class ErrorDialog extends javax.swing.JDialog {

        private static final java.util.logging.Logger logger = java.util.logging.Logger
                        .getLogger(ErrorDialog.class.getName());

        /**
         * Creates new form ErrorDialog
         */
        public ErrorDialog(java.awt.Frame parent, boolean modal, String mensaje) {
                super(parent, modal);
                initComponents();
                this.getContentPane().removeAll();
                this.getContentPane().setLayout(
                                new net.miginfocom.swing.MigLayout(
                                                "fill, insets 0",
                                                "[grow]",
                                                "[54!][grow,fill]"));
                panelFormulario.removeAll();
                panelFormulario.setLayout(
                                new net.miginfocom.swing.MigLayout(
                                                "fill, insets 24",
                                                "[grow]",
                                                "50[]"));
                javax.swing.JLabel labelError = new javax.swing.JLabel(mensaje);
                labelError.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
                // utiliza lo de arriba para setear el tipo de error, por ejemplo: "Ocurrió un
                // error al
                // cargar los datos" o "Ocurrió un error al guardar los cambios"
                // ariana sapa
                panelFormulario.add(labelError, "growx, wrap");
                javax.swing.JButton btnCerrar = new javax.swing.JButton("Aceptar");
                btnCerrar.addActionListener(e -> this.dispose());
                panelFormulario.add(btnCerrar, "align center");

                panelHeader.removeAll();

                panelHeader.setLayout(
                                new net.miginfocom.swing.MigLayout(
                                                "fill, insets 0 20 0 20",
                                                "[grow]",
                                                "[center]"));

                javax.swing.JLabel titulo = new javax.swing.JLabel("Error");
                titulo.setForeground(java.awt.Color.WHITE);
                titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

                panelHeader.add(titulo, "align left");

                this.getContentPane().add(panelHeader, "growx, wrap");
                this.getContentPane().add(panelFormulario, "grow, push, wrap");
                panelHeader.setPreferredSize(new java.awt.Dimension(0, 54));
                this.revalidate();
                this.repaint();
                this.pack();
                this.setLocationRelativeTo(null);
                //falta ponerlo bonito noma 
                //POO esta acabando con la poca salud mental que me quedaba 
                /*
                                                ⢀⣤⣾⡿⠿⢿⣦⡀⠀⠀⠀⠀⠀⠀
⠀  ⢀⣶⣿⣶⣶⣶⣦⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⠟⠁⣀⣤⡄⢹⣷⡀⠀⠀⠀⠀⠀
⠀⠀⢸⣿⡧⠤⠤⣌⣉⣩⣿⡿⠶⠶⠒⠛⠛⠻⠿⠶⣾⣿⣣⠔⠉⠀⠀⠙⡆⢻⣷⠀⠀⠀⠀⠀
⠀⠀⢸⣿⠀⠀⢠⣾⠟⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣿⡃⠀⠀⠀⠀⠀⢻⠘⣿⡀⠀⠀⠀⠀
⠀⠀⠘⣿⡀⣴⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⠻⢶⣤⣀⠀⢘⠀⣿⡇⠀⠀⠀⠀
⠀⠀⠀⢿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠛⢿⣴⣿⠀⠀⠀⠀⠀
⠀⠀⠀⣸⡟⠀⠀⠀⣴⡆⠀⠀⠀⠀⠀⠀⠀⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⣷⡀⠀⠀⠀⠀
⠀⠀⢰⣿⠁⠀⠀⣰⠿⣇⠀⠀⠀⠀⠀⠀⠀⢻⣷⡀⠀⢠⡄⠀⠀⠀⠀⠀⡀⠀⠹⣷⠀⠀⠀⠀
⠀⠀⣾⡏⠀⢀⣴⣿⣤⢿⡄⠀⠀⠀⠀⠀⠀⠸⣿⣷⡀⠘⣧⠀⠀⠀⠀⠀⣷⣄⠀⢻⣇⠀⠀⠀
⠀⠀⢻⣇⠀⢸⡇⠀⠀⠀⢻⣄⠀⠀⠀⠀⠀⣤⡯⠈⢻⣄⢻⡄⠀⠀⠀⠀⣿⡿⣷⡌⣿⡄⠀⠀
⠀⢀⣸⣿⠀⢸⡷⣶⣶⡄⠀⠙⠛⠛⠛⠛⠛⠃⣠⣶⣄⠙⠿⣧⠀⠀⠀⢠⣿⢹⣻⡇⠸⣿⡄⠀
⢰⣿⢟⣿⡴⠞⠀⠘⢿⡿⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣿⡇⠀⣿⡀⢀⣴⠿⣿⣦⣿⠃⠀⢹⣷⠀
⠀⢿⣿⠁⠀⠀⠀⠀⠀⠀⠀⢠⣀⣀⡀⠀⡀⠀⠀⠀⠀⠀⠀⣿⠛⠛⠁⠀⣿⡟⠁⠀⠀⢀⣿⠂
⠀⢠⣿⢷⣤⣀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠛⠉⠀⠀⠀⠀⠀⢠⡿⢰⡟⠻⠞⠛⣧⣠⣦⣀⣾⠏⠀
⠀⢸⣿⠀⠈⢹⡿⠛⢶⡶⢶⣤⣤⣤⣤⣤⣤⣤⣤⣶⠶⣿⠛⠷⢾⣧⣠⡿⢿⡟⠋⠛⠋⠁⠀⠀
⠀⣾⣧⣤⣶⣟⠁⠀⢸⣇⣸⠹⣧⣠⡾⠛⢷⣤⡾⣿⢰⡟⠀⠀⠀⣿⠋⠁⢈⣿⣄⠀⠀⠀⠀⠀
⠀⠀⠀⣼⡏⠻⢿⣶⣤⣿⣿⠀⠈⢉⣿⠀⢸⣏⠀⣿⠈⣷⣤⣤⣶⡿⠶⠾⠋⣉⣿⣦⣀⠀⠀⠀
⠀⠀⣼⡿⣇⠀⠀⠙⠻⢿⣿⠀⠀⢸⣇⠀⠀⣻⠀⣿⠀⣿⠟⠋⠁⠀⠀⢀⡾⠋⠉⠙⣿⡆⠀⠀
⠀⠀⢻⣧⠙⢷⣤⣦⠀⢸⣿⡄⠀⠀⠉⠳⣾⠏⠀⢹⣾⡇⠀⠀⠙⠛⠶⣾⡁⠀⠀⠀⣼⡇⠀⠀
⠀⠀⠀⠙⠛⠛⣻⡟⠀⣼⣿⣇⣀⣀⣀⡀⠀⠀⠀⣸⣿⣇⠀⠀⠀⠀⠀⠈⢛⣷⠶⠿⠋⠀⠀⠀
⠀⠀⠀⠀⠀⢠⣿⣅⣠⣿⠛⠋⠉⠉⠛⠻⠛⠛⠛⠛⠋⠻⣧⡀⣀⣠⢴⠾⠉⣿⣆⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⣼⡏⠉⣿⡟⠁⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠙⠿⣿⣌⠁⠀⠀⠈⣿⡆⠀⠀⠀⠀
⠀⠀⠀⠀⠀⣿⣇⣠⣿⣿⡶⠶⠶⣶⣿⣷⡶⣶⣶⣶⣶⡶⠶⠶⢿⣿⡗⣀⣤⣾⠟⠁⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠈⠙⠛⢻⣿⡇⠀⠀⣿⡟⠛⠷⠶⠾⢿⣧⠁⠀⠀⣸⡿⠿⠟⠉⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣷⣦⣤⡿⠀⠀⠀⠀⠀⠀⢿⣧⣤⣼⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                */
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

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("Error");
                setModal(true);
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
                                                .addGap(0, 100, Short.MAX_VALUE));
                panelFormularioLayout.setVerticalGroup(
                                panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(156, Short.MAX_VALUE)
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
                                                                .addGap(144, 144, 144)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addComponent(panelHeader,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(panelFormulario,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 88, Short.MAX_VALUE)));

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
                                ErrorDialog dialog = new ErrorDialog(new javax.swing.JFrame(), true,"");
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
        private javax.swing.JPanel panelFormulario;
        private javax.swing.JPanel panelHeader;
        // End of variables declaration//GEN-END:variables
}
