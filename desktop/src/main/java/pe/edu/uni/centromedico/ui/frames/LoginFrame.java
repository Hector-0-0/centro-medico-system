package pe.edu.uni.centromedico.ui.frames;
import pe.edu.uni.centromedico.ui.dialogs.ErrorDialog;
import pe.edu.uni.centromedico.service.BuscarPersona;
import pe.edu.uni.centromedico.models.Persona;
    public class LoginFrame extends javax.swing.JFrame {

        private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginFrame.class.getName());

        public LoginFrame() {
            initComponents();
            getContentPane().setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow, fill]", "[grow, fill][120!]"));

            panelLogin.setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow, center]", "[grow, center]"));

            pnlFormulario.setLayout(new net.miginfocom.swing.MigLayout(
                "fillx, insets 40", "[grow, center]", "[]5[]10[]10[]20[]5[]20[]30[]"));

            pnlFormulario.add(lblLogo,      "center, wrap");
            pnlFormulario.add(lblTitulo,    "center, wrap");
            pnlFormulario.add(lblSubtitulo, "center, wrap");
            pnlFormulario.add(lblUsuario,   "align left, wrap");
            pnlFormulario.add(txtUsuario,   "growx, h 35!, wrap");
            pnlFormulario.add(lblPassword,  "align left, wrap");
            pnlFormulario.add(txtPassword,  "growx, h 35!, wrap");
            pnlFormulario.add(btnIngresar,  "growx, h 40!");

            panelLogin.add(pnlFormulario, "width 400!");

            getContentPane().add(panelLogin, "grow, pos 0 0, width 100%, height 100%");
            getContentPane().add(lblFondo,   "grow, pos 0 0, width 100%, height 100%");
            getContentPane().setComponentZOrder(panelLogin, 0);
            getContentPane().setComponentZOrder(lblFondo,   1);

            this.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent e) {
                    escalarFondo();
                }
            });

            pnlCol1.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 10", "[center, grow]", "[]5[]5[]"));
            pnlCol2.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 10", "[center, grow]", "[]5[]5[]"));
            pnlCol3.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 10", "[center, grow]", "[]5[]5[]"));

            pnlCol1.add(lblEnterateTitulo,     "center, wrap");
            pnlCol1.add(lblPortal,             "center, wrap");
            pnlCol1.add(lblUnisalud,           "center, wrap");

            pnlCol2.add(lblContactanosTitulo,  "center, wrap");
            pnlCol2.add(lblEmail,              "center, wrap");
            pnlCol2.add(lblTelefono,           "center, wrap");

            pnlCol3.add(lblEncuentranosTitulo, "center, wrap");
            pnlCol3.add(lblSede,               "center, wrap");
            pnlCol3.add(lblDireccion,          "center, wrap");

            pnlFooter.setLayout(new net.miginfocom.swing.MigLayout(
                "fillx, insets 0", "[grow, fill][grow, fill][grow, fill]", "[grow, fill]"));
            pnlFooter.add(pnlCol1, "grow");
            pnlFooter.add(pnlCol2, "grow");
            pnlFooter.add(pnlCol3, "grow");

            getContentPane().add(pnlFooter, "dock south, h 120!");
            // ── CONEXIÓN: botón ingresar ─────────────────────────────────────────
            btnIngresar.addActionListener(e -> {
                String codigo   = txtUsuario.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                BuscarPersona buscador = new BuscarPersona();
                Persona persona = buscador.buscarPersonaPorCodigo(codigo);

                if (persona != null && persona.getPassword().equals(password)) {
                    dispose();
                    new pe.edu.uni.centromedico.ui.frames.MainFrame(
                        persona).setVisible(true);
                    return;
                }

                // Si no encontró nada:
                ErrorDialog error = new ErrorDialog(this,true, "Código o contraseña incorrectos.");
                error.setVisible(true);
            });
        }

        // Método auxiliar — fuera del constructor, dentro de la clase
        private String[] buscarEnTxt(String ruta, String codigo, String password) {
            try (java.util.Scanner sc = new java.util.Scanner(
                    getClass().getResourceAsStream(ruta))) {
                while (sc.hasNextLine()) {
                    String[] p = sc.nextLine().split("\\|");
                    if (p.length >= 2 && p[0].equals(codigo) && p[1].equals(password)) {
                        return p;
                    }
                }
            } catch (Exception ex) { /* archivo no encontrado */ }
            return null;
        }

        private void escalarFondo() {
            java.awt.Image img = new javax.swing.ImageIcon(
                getClass().getResource("/Images/banner-uni.jpeg")).getImage();
                lblFondo.setIcon(new javax.swing.ImageIcon(img.getScaledInstance(getWidth(), getHeight(), java.awt.Image.SCALE_SMOOTH)));
        }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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

        javax.swing.GroupLayout pnlCol1Layout = new javax.swing.GroupLayout(pnlCol1);
        pnlCol1.setLayout(pnlCol1Layout);
        pnlCol1Layout.setHorizontalGroup(
            pnlCol1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCol1Layout.createSequentialGroup()
                .addGroup(pnlCol1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCol1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(lblEnterateTitulo))
                    .addGroup(pnlCol1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblPortal))
                    .addGroup(pnlCol1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblUnisalud)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pnlCol1Layout.setVerticalGroup(
            pnlCol1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCol1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEnterateTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPortal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUnisalud)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        lblContactanosTitulo.setText("CONTÁCTANOS");

        lblEmail.setText("centromedico@uni.edu.pe");

        lblTelefono.setText("014811070:3004");

        javax.swing.GroupLayout pnlCol2Layout = new javax.swing.GroupLayout(pnlCol2);
        pnlCol2.setLayout(pnlCol2Layout);
        pnlCol2Layout.setHorizontalGroup(
            pnlCol2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCol2Layout.createSequentialGroup()
                .addGroup(pnlCol2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCol2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(lblEmail))
                    .addGroup(pnlCol2Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(lblContactanosTitulo)))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCol2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblTelefono)
                .addGap(43, 43, 43))
        );
        pnlCol2Layout.setVerticalGroup(
            pnlCol2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCol2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblContactanosTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTelefono)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        lblEncuentranosTitulo.setText("ENCUÉNTRANOS");

        lblSede.setText("Sede Central");

        lblDireccion.setText("Av. Tupac Amaru N.º 210");

        javax.swing.GroupLayout pnlCol3Layout = new javax.swing.GroupLayout(pnlCol3);
        pnlCol3.setLayout(pnlCol3Layout);
        pnlCol3Layout.setHorizontalGroup(
            pnlCol3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCol3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(pnlCol3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCol3Layout.createSequentialGroup()
                        .addGroup(pnlCol3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDireccion)
                            .addGroup(pnlCol3Layout.createSequentialGroup()
                                .addComponent(lblSede)
                                .addGap(32, 32, 32)))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCol3Layout.createSequentialGroup()
                        .addComponent(lblEncuentranosTitulo)
                        .addGap(36, 36, 36))))
        );
        pnlCol3Layout.setVerticalGroup(
            pnlCol3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCol3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEncuentranosTitulo)
                .addGap(16, 16, 16)
                .addComponent(lblSede)
                .addGap(18, 18, 18)
                .addComponent(lblDireccion)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlFooterLayout = new javax.swing.GroupLayout(pnlFooter);
        pnlFooter.setLayout(pnlFooterLayout);
        pnlFooterLayout.setHorizontalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFooterLayout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(pnlCol1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 576, Short.MAX_VALUE)
                .addComponent(pnlCol2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
            .addGroup(pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFooterLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(pnlCol3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlFooterLayout.setVerticalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFooterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCol2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlCol1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFooterLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(pnlCol3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panelLogin.setOpaque(false);

        lblFondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        pnlFormulario.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormulario.setPreferredSize(new java.awt.Dimension(400, 500));

        lblTitulo.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(113, 22, 16));
        lblTitulo.setText("Centro Médico UNI");

        lblUsuario.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        lblUsuario.setText("Usuario");

        txtUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        txtUsuario.setPreferredSize(new java.awt.Dimension(250, 40));

        lblPassword.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        lblPassword.setText("Contraseña");

        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        txtPassword.setPreferredSize(new java.awt.Dimension(250, 40));

        btnIngresar.setBackground(new java.awt.Color(113, 22, 16));
        btnIngresar.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar");
        btnIngresar.setBorder(null);
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo-uni.png"))); // NOI18N

        lblSubtitulo.setForeground(new java.awt.Color(153, 153, 153));
        lblSubtitulo.setText("Universidad Nacional de Ingeniería");

        javax.swing.GroupLayout pnlFormularioLayout = new javax.swing.GroupLayout(pnlFormulario);
        pnlFormulario.setLayout(pnlFormularioLayout);
        pnlFormularioLayout.setHorizontalGroup(
            pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormularioLayout.createSequentialGroup()
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(btnIngresar))
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsuario)
                            .addComponent(lblPassword)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(74, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(116, 116, 116))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                        .addComponent(lblLogo)
                        .addGap(148, 148, 148))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblSubtitulo)
                .addGap(99, 99, 99))
        );
        pnlFormularioLayout.setVerticalGroup(
            pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormularioLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(lblLogo)
                .addGap(18, 18, 18)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitulo)
                .addGap(19, 19, 19)
                .addComponent(lblUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnIngresar)
                .addGap(55, 55, 55))
        );

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(311, 311, 311)
                .addComponent(pnlFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(398, Short.MAX_VALUE))
            .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLoginLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(710, Short.MAX_VALUE)))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(pnlFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
            .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLoginLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(562, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1109, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFooter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 703, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(585, Short.MAX_VALUE)
                    .addComponent(pnlFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel lblContactanosTitulo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEncuentranosTitulo;
    private javax.swing.JLabel lblEnterateTitulo;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPortal;
    private javax.swing.JLabel lblSede;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUnisalud;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JPanel pnlCol1;
    private javax.swing.JPanel pnlCol2;
    private javax.swing.JPanel pnlCol3;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormulario;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
