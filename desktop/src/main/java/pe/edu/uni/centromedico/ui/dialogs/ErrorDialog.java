package pe.edu.uni.centromedico.ui.dialogs;

public class ErrorDialog extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(ErrorDialog.class.getName());

    private final javax.swing.JPanel panelHeader;
    private final javax.swing.JPanel panelFormulario;

    public ErrorDialog(java.awt.Frame parent, boolean modal, String mensaje) {
        super(parent, modal);

        panelHeader = new javax.swing.JPanel();
        panelFormulario = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Error");
        setModal(true);
        setResizable(false);

        panelHeader.setBackground(new java.awt.Color(139, 20, 20));

        this.getContentPane().setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 0",
                "[grow]",
                "[54!][grow,fill]"));

        panelFormulario.setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 24",
                "[grow]",
                "50[]"));

        javax.swing.JLabel labelError = new javax.swing.JLabel(mensaje);
        labelError.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        panelFormulario.add(labelError, "growx, wrap");

        javax.swing.JButton btnCerrar = new javax.swing.JButton("Aceptar");
        btnCerrar.addActionListener(e -> this.dispose());
        panelFormulario.add(btnCerrar, "align center");

        panelHeader.setLayout(new net.miginfocom.swing.MigLayout(
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
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            ErrorDialog dialog = new ErrorDialog(new javax.swing.JFrame(), true, "");
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
}
