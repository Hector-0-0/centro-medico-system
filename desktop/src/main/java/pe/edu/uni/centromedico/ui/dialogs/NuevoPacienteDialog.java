package pe.edu.uni.centromedico.ui.dialogs;

public class NuevoPacienteDialog extends javax.swing.JDialog {

        public NuevoPacienteDialog(java.awt.Frame parent, boolean modal) {
                super(parent, modal);
                initComponents();

                this.getContentPane().removeAll();
                this.getContentPane().setLayout(
                        new net.miginfocom.swing.MigLayout(
                                "fill, insets 0", "[grow]", "[54!][grow,fill][50!]"));

                // ── Formulario ────────────────────────────────────────────
                panelFormulario.removeAll();
                panelFormulario.setLayout(
                        new net.miginfocom.swing.MigLayout(
                                "fill, insets 24", "[130!][grow]", "[]10[]10[]10[]10[]10[]10[]"));

                javax.swing.JTextField txtCodigo  = new javax.swing.JTextField();
                javax.swing.JTextField txtNombre  = new javax.swing.JTextField();
                javax.swing.JTextField txtEdad    = new javax.swing.JTextField();
                javax.swing.JTextField txtCarrera = new javax.swing.JTextField();
                javax.swing.JTextField txtEmail   = new javax.swing.JTextField();
                javax.swing.JPasswordField txtPass = new javax.swing.JPasswordField();

                panelFormulario.add(new javax.swing.JLabel("Código"),      "growx");
                panelFormulario.add(txtCodigo,  "growx, wrap");
                panelFormulario.add(new javax.swing.JLabel("Nombre"),      "growx");
                panelFormulario.add(txtNombre,  "growx, wrap");
                panelFormulario.add(new javax.swing.JLabel("Edad"),        "growx");
                panelFormulario.add(txtEdad,    "growx, wrap");
                panelFormulario.add(new javax.swing.JLabel("Carrera"),     "growx");
                panelFormulario.add(txtCarrera, "growx, wrap");
                panelFormulario.add(new javax.swing.JLabel("Email"),       "growx");
                panelFormulario.add(txtEmail,   "growx, wrap");
                panelFormulario.add(new javax.swing.JLabel("Contraseña"),  "growx");
                panelFormulario.add(txtPass,    "growx, wrap");

                // ── Footer ────────────────────────────────────────────────
                panelFooter.removeAll();
                panelFooter.setLayout(
                        new net.miginfocom.swing.MigLayout(
                                "fillx, insets 10 20 10 20", "[grow][][]", "[38!]"));
                panelFooter.add(new javax.swing.JLabel(), "growx");

                javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
                btnCancelar.setBackground(new java.awt.Color(139, 20, 20));
                btnCancelar.setForeground(java.awt.Color.WHITE);
                btnCancelar.setBorderPainted(false);
                btnCancelar.setFocusPainted(false);
                btnCancelar.addActionListener(e -> this.dispose());
                panelFooter.add(btnCancelar);

                javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
                btnGuardar.setBackground(new java.awt.Color(139, 20, 20));
                btnGuardar.setForeground(java.awt.Color.WHITE);
                btnGuardar.setBorderPainted(false);
                btnGuardar.setFocusPainted(false);
                btnGuardar.addActionListener(e -> {
                        String codigo   = txtCodigo.getText().trim();
                        String nombre   = txtNombre.getText().trim();
                        String edadTxt  = txtEdad.getText().trim();
                        String carrera  = txtCarrera.getText().trim();
                        String email    = txtEmail.getText().trim();
                        String password = new String(txtPass.getPassword()).trim();

                        if (codigo.isEmpty() || nombre.isEmpty() || edadTxt.isEmpty()
                                || carrera.isEmpty() || email.isEmpty() || password.isEmpty()) {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                        "Todos los campos son obligatorios.",
                                        "Campos requeridos", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        int edad;
                        try {
                                edad = Integer.parseInt(edadTxt);
                                if (edad <= 0 || edad > 120) throw new NumberFormatException();
                        } catch (NumberFormatException ex) {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                        "Ingresa una edad válida (número entero).",
                                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                                return;
                        }
                        pe.edu.uni.centromedico.models.Estudiante est =
                                new pe.edu.uni.centromedico.models.Estudiante();
                        est.setId(codigo);
                        est.setNombre(nombre);
                        est.setEdad(edad);
                        est.setCarrera(carrera);
                        est.setEmail(email);

                        boolean ok = new pe.edu.uni.centromedico.db.dao.EstudianteDAO()
                                .registrar(est, password);
                        if (ok) {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                        "Paciente registrado correctamente.",
                                        "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                this.dispose();
                        } else {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                        "Error al registrar. El código ya puede existir.",
                                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                });
                panelFooter.add(btnGuardar);

                // ── Header ────────────────────────────────────────────────
                panelHeader.removeAll();
                panelHeader.setLayout(
                        new net.miginfocom.swing.MigLayout(
                                "fill, insets 0 20 0 20", "[grow]", "[center]"));
                javax.swing.JLabel titulo = new javax.swing.JLabel("Registrar Paciente");
                titulo.setForeground(java.awt.Color.WHITE);
                titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
                panelHeader.add(titulo, "align left");

                this.getContentPane().add(panelHeader,    "growx, wrap");
                this.getContentPane().add(panelFormulario, "grow, push, wrap");
                this.getContentPane().add(panelFooter,    "growx");
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

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel panelFooter;
        private javax.swing.JPanel panelFormulario;
        private javax.swing.JPanel panelHeader;
        // End of variables declaration//GEN-END:variables
}
