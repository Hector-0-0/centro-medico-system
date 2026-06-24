package pe.edu.uni.centromedico.ui.frames;

import pe.edu.uni.centromedico.util.ConfiguracionParametros;

public class LoginFrame extends javax.swing.JFrame {

    private final ConfiguracionParametros OBJ = new ConfiguracionParametros();

    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlCol1;
    private javax.swing.JLabel lblEnterateTitulo;
    private javax.swing.JLabel lblPortal;
    private javax.swing.JLabel lblUnisalud;
    private javax.swing.JPanel pnlCol2;
    private javax.swing.JLabel lblContactanosTitulo;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JPanel pnlCol3;
    private javax.swing.JLabel lblEncuentranosTitulo;
    private javax.swing.JLabel lblSede;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JPanel pnlFormulario;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblSubtitulo;

    public LoginFrame() {
        pnlFooter = new javax.swing.JPanel();
        pnlCol1 = new javax.swing.JPanel();
        lblEnterateTitulo = new javax.swing.JLabel();
        lblPortal = new javax.swing.JLabel();
        lblUnisalud = new javax.swing.JLabel();
        pnlCol2 = new javax.swing.JPanel();
        lblContactanosTitulo = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        pnlCol3 = new javax.swing.JPanel();
        lblEncuentranosTitulo = new javax.swing.JLabel();
        lblSede = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        panelLogin = new javax.swing.JPanel();
        lblFondo = new javax.swing.JLabel();
        pnlFormulario = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(245, 245, 245));

        pnlFooter.setBackground(new java.awt.Color(113, 22, 16));

        lblEnterateTitulo.setText("ENTERATE");
        lblPortal.setText("portal.uni.edu.pe");
        lblUnisalud.setText("@unisaludoficial");

        lblContactanosTitulo.setText("CONTÁCTANOS");
        lblEmail.setText("centromedico@uni.edu.pe");
        lblTelefono.setText("014811070:3004");

        lblEncuentranosTitulo.setText("ENCUÉNTRANOS");
        lblSede.setText("Sede Central");
        lblDireccion.setText("Av. Tupac Amaru N.º 210");

        panelLogin.setOpaque(false);

        lblFondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        pnlFormulario.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormulario.setPreferredSize(new java.awt.Dimension(400, 500));

        lblTitulo.setFont(new java.awt.Font("Liberation Sans", 1, 18));
        lblTitulo.setForeground(new java.awt.Color(113, 22, 16));
        lblTitulo.setText("Centro Médico UNI");

        lblUsuario.setFont(new java.awt.Font("Liberation Sans", 0, 14));
        lblUsuario.setText("Usuario");

        txtUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        txtUsuario.setPreferredSize(new java.awt.Dimension(250, 40));

        lblPassword.setFont(new java.awt.Font("Liberation Sans", 0, 14));
        lblPassword.setText("Contraseña");

        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        txtPassword.setPreferredSize(new java.awt.Dimension(250, 40));

        btnIngresar.setBackground(new java.awt.Color(113, 22, 16));
        btnIngresar.setFont(new java.awt.Font("Liberation Sans", 1, 14));
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar");
        btnIngresar.setBorder(null);
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo-uni.png")));

        lblSubtitulo.setForeground(new java.awt.Color(153, 153, 153));
        lblSubtitulo.setText("Universidad Nacional de Ingeniería");

        javax.swing.ImageIcon logoIcon = OBJ.cargarIcono("logo-uni.png");
        if (logoIcon != null) lblLogo.setIcon(logoIcon);

        pnlFormulario.setLayout(new net.miginfocom.swing.MigLayout(
                "fillx, insets 40", "[grow, center]", "[]5[]10[]10[]20[]5[]20[]30[]"));

        pnlFormulario.add(lblLogo, "center, wrap");
        pnlFormulario.add(lblTitulo, "center, wrap");
        pnlFormulario.add(lblSubtitulo, "center, wrap");
        pnlFormulario.add(lblUsuario, "align left, wrap");
        pnlFormulario.add(txtUsuario, "growx, h 40!, wrap");
        pnlFormulario.add(lblPassword, "align left, wrap");
        pnlFormulario.add(txtPassword, "growx, h 40!, wrap");
        pnlFormulario.add(btnIngresar, "growx, h 44!");

        lblTitulo.setFont(lblTitulo.getFont().deriveFont(java.awt.Font.BOLD, 22f));

        javax.swing.border.Border bordeCampo = javax.swing.UIManager.getBorder("TextField.border");
        txtUsuario.setBorder(bordeCampo);
        txtPassword.setBorder(bordeCampo);
        txtUsuario.putClientProperty("FlatLaf.style", "arc: 12");
        txtPassword.putClientProperty("FlatLaf.style", "arc: 12");
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ingresa tu usuario");
        txtPassword.putClientProperty("JTextField.placeholderText", "Ingresa tu contraseña");
        txtPassword.putClientProperty("JPasswordField.showRevealButton", true);

        btnIngresar.putClientProperty("FlatLaf.style",
                "arc: 12; borderWidth: 0; focusWidth: 0; innerFocusWidth: 0;"
                        + " background: #711610; foreground: #ffffff;"
                        + " hoverBackground: #711610; pressedBackground: #711610;"
                        + " focusedBackground: #711610; selectedBackground: #711610");
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        pnlFormulario.putClientProperty("FlatLaf.style", "arc: 24");

        lblFondo.setLayout(new net.miginfocom.swing.MigLayout(
                "fill", "[grow, center]", "[grow, center]"));
        lblFondo.add(pnlFormulario, "width 400!");

        pnlCol1.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 10", "[center, grow]", "[]5[]5[]"));
        pnlCol2.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 10", "[center, grow]", "[]5[]5[]"));
        pnlCol3.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 10", "[center, grow]", "[]5[]5[]"));

        pnlCol1.add(lblEnterateTitulo, "center, wrap");
        pnlCol1.add(lblPortal, "center, wrap");
        pnlCol1.add(lblUnisalud, "center, wrap");

        pnlCol2.add(lblContactanosTitulo, "center, wrap");
        pnlCol2.add(lblEmail, "center, wrap");
        pnlCol2.add(lblTelefono, "center, wrap");

        pnlCol3.add(lblEncuentranosTitulo, "center, wrap");
        pnlCol3.add(lblSede, "center, wrap");
        pnlCol3.add(lblDireccion, "center, wrap");

        pnlCol1.setOpaque(false);
        pnlCol2.setOpaque(false);
        pnlCol3.setOpaque(false);

        java.awt.Color blancoSuave = new java.awt.Color(255, 255, 255, 205);

        java.util.Map<java.awt.font.TextAttribute, Object> tracking = new java.util.HashMap<>();
        tracking.put(java.awt.font.TextAttribute.TRACKING, 0.12);
        for (javax.swing.JLabel titulo : new javax.swing.JLabel[]{
                lblEnterateTitulo, lblContactanosTitulo, lblEncuentranosTitulo}) {
            titulo.setForeground(java.awt.Color.WHITE);
            titulo.setFont(titulo.getFont().deriveFont(java.awt.Font.BOLD, 14f).deriveFont(tracking));
        }

        for (javax.swing.JLabel detalle : new javax.swing.JLabel[]{
                lblPortal, lblUnisalud, lblEmail, lblTelefono, lblSede, lblDireccion}) {
            detalle.setForeground(blancoSuave);
            detalle.setFont(detalle.getFont().deriveFont(12.5f));
        }

        pnlCol2.setBorder(javax.swing.BorderFactory.createMatteBorder(
                0, 1, 0, 0, new java.awt.Color(255, 255, 255, 45)));
        pnlCol3.setBorder(javax.swing.BorderFactory.createMatteBorder(
                0, 1, 0, 0, new java.awt.Color(255, 255, 255, 45)));

        pnlFooter.setLayout(new net.miginfocom.swing.MigLayout(
                "fillx, insets 0", "[grow, fill][grow, fill][grow, fill]", "[grow, fill]"));
        pnlFooter.add(pnlCol1, "grow");
        pnlFooter.add(pnlCol2, "grow");
        pnlFooter.add(pnlCol3, "grow");
        pnlFooter.setPreferredSize(new java.awt.Dimension(0, 120));

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(lblFondo, java.awt.BorderLayout.CENTER);
        getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                escalarFondo();
            }
            public void componentShown(java.awt.event.ComponentEvent e) {
                escalarFondo();
            }
        });

        setTitle("Centro Médico UNI");
        setMinimumSize(new java.awt.Dimension(900, 720));
        setSize(1000, 780);
        setLocationRelativeTo(null);

        new pe.edu.uni.centromedico.controller.LoginController(this);
    }

    private void escalarFondo() {
        int w = lblFondo.getWidth();
        int h = lblFondo.getHeight();
        if (w <= 0 || h <= 0) return;
        javax.swing.ImageIcon fondoIcon = OBJ.cargarIcono("banner-uni.jpeg");
        if (fondoIcon == null) return;
        java.awt.Image original = fondoIcon.getImage();

        java.awt.image.BufferedImage buffer = new java.awt.image.BufferedImage(
                w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = buffer.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, w, h, null);
        g.setColor(new java.awt.Color(0, 0, 0, 80));
        g.fillRect(0, 0, w, h);
        g.dispose();

        lblFondo.setIcon(new javax.swing.ImageIcon(buffer));
    }

    public static void main(String args[]) {
        com.formdev.flatlaf.FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    public javax.swing.JButton getBtnIngresar() { return btnIngresar; }
    public javax.swing.JTextField getTxtUsuario() { return txtUsuario; }
    public javax.swing.JPasswordField getTxtPassword() { return txtPassword; }
}
