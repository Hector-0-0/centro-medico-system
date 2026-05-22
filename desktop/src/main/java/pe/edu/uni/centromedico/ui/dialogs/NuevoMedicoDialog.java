package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.db.dao.DoctorDAO;
import pe.edu.uni.centromedico.models.Doctor;

public class NuevoMedicoDialog extends javax.swing.JDialog {

    public NuevoMedicoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Registrar Médico");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        getContentPane().setLayout(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow]", "[54!][grow,fill][50!]"));

        // ── Header ────────────────────────────────────────────────────────
        javax.swing.JPanel panelHeader = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fill, insets 0 20 0 20", "[grow]", "[center]"));
        panelHeader.setBackground(new java.awt.Color(139, 20, 20));
        javax.swing.JLabel titulo = new javax.swing.JLabel("Registrar Médico");
        titulo.setForeground(java.awt.Color.WHITE);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        panelHeader.add(titulo, "align left");

        // ── Formulario ────────────────────────────────────────────────────
        javax.swing.JPanel panelFormulario = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[130!][grow]", "[]10[]10[]10[]10[]10[]"));

        javax.swing.JTextField    txtCodigo      = new javax.swing.JTextField();
        javax.swing.JTextField    txtNombre      = new javax.swing.JTextField();
        javax.swing.JTextField    txtEspecialidad = new javax.swing.JTextField();
        javax.swing.JTextField    txtConsultorio = new javax.swing.JTextField();
        javax.swing.JPasswordField txtPass       = new javax.swing.JPasswordField();

        panelFormulario.add(new javax.swing.JLabel("Código"),       "growx");
        panelFormulario.add(txtCodigo,       "growx, wrap");
        panelFormulario.add(new javax.swing.JLabel("Nombre"),       "growx");
        panelFormulario.add(txtNombre,       "growx, wrap");
        panelFormulario.add(new javax.swing.JLabel("Especialidad"), "growx");
        panelFormulario.add(txtEspecialidad, "growx, wrap");
        panelFormulario.add(new javax.swing.JLabel("Consultorio"),  "growx");
        panelFormulario.add(txtConsultorio,  "growx, wrap");
        panelFormulario.add(new javax.swing.JLabel("Contraseña"),   "growx");
        panelFormulario.add(txtPass,         "growx, wrap");

        // ── Footer ────────────────────────────────────────────────────────
        javax.swing.JPanel panelFooter = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fillx, insets 10 20 10 20", "[grow][][]", "[38!]"));
        panelFooter.setBackground(new java.awt.Color(240, 235, 230));
        panelFooter.add(new javax.swing.JLabel(), "growx");

        javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
        btnCancelar.setBackground(new java.awt.Color(139, 20, 20));
        btnCancelar.setForeground(java.awt.Color.WHITE);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        panelFooter.add(btnCancelar);

        javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
        btnGuardar.setBackground(new java.awt.Color(139, 20, 20));
        btnGuardar.setForeground(java.awt.Color.WHITE);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> {
            String codigo       = txtCodigo.getText().trim();
            String nombre       = txtNombre.getText().trim();
            String especialidad = txtEspecialidad.getText().trim();
            String consultorio  = txtConsultorio.getText().trim();
            String password     = new String(txtPass.getPassword()).trim();

            if (codigo.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()
                    || consultorio.isEmpty() || password.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.",
                    "Campos requeridos", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            Doctor doc = new Doctor();
            doc.setId(codigo);
            doc.setNombre(nombre);
            doc.setEspecialidad(especialidad);
            doc.setConsultorio(consultorio);

            boolean ok = new DoctorDAO().registrar(doc, password);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Médico registrado correctamente.",
                    "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al registrar. El código ya puede existir.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
        panelFooter.add(btnGuardar);

        // ── Ensamblar ─────────────────────────────────────────────────────
        getContentPane().add(panelHeader,    "growx, wrap");
        getContentPane().add(panelFormulario, "grow, push, wrap");
        getContentPane().add(panelFooter,    "growx");
        panelHeader.setPreferredSize(new java.awt.Dimension(0, 54));
        panelFooter.setPreferredSize(new java.awt.Dimension(0, 50));

        pack();
        setLocationRelativeTo(parent);
    }
}
