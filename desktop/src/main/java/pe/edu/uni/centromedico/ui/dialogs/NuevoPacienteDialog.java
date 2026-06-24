package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.util.UIConstants;
import pe.edu.uni.centromedico.util.Validador;
import pe.edu.uni.centromedico.models.Estudiante;
import pe.edu.uni.centromedico.db.dao.EstudianteDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class NuevoPacienteDialog extends javax.swing.JDialog {

    private static final List<String> CARRERAS = Arrays.asList(
        "Ingenieria Civil", "Ingenieria de Sistemas", "Ingenieria Industrial",
        "Ingenieria Electronica", "Ingenieria Electrica", "Ingenieria Mecanica",
        "Ingenieria Mecatronica", "Ingenieria Quimica", "Ingenieria de Petroleo",
        "Ingenieria de Minas", "Ingenieria Ambiental", "Ingenieria Textil",
        "Arquitectura", "Ciencias de la Computacion", "Matematicas",
        "Fisica", "Quimica", "Economia", "Estadistica"
    );

    private final javax.swing.JTextField txtCodigo;
    private final javax.swing.JTextField txtNombre;
    private final javax.swing.JTextField txtFechaNac;
    private final javax.swing.JComboBox<String> comboCarrera;
    private final javax.swing.JTextField txtEmail;
    private final javax.swing.JPasswordField txtPass;
    private final javax.swing.JLabel lblError;

    public NuevoPacienteDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Registrar Paciente");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);

        getContentPane().setLayout(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow]", "[54!][grow,fill][50!]"));

        javax.swing.JPanel panelHeader = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fill, insets 0 20 0 20", "[grow]", "[center]"));
        panelHeader.setBackground(UIConstants.CARMESI);
        javax.swing.JLabel titulo = new javax.swing.JLabel("Registrar Paciente");
        titulo.setForeground(UIConstants.BLANCO);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        panelHeader.add(titulo, "align left");
        panelHeader.setPreferredSize(new java.awt.Dimension(0, UIConstants.HEADER_HEIGHT));

        javax.swing.JPanel panelFormulario = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[130!][grow]", "[]8[]8[]8[]8[]8[]8[]0[]"));

        txtCodigo   = new javax.swing.JTextField();
        txtNombre   = new javax.swing.JTextField();
        txtFechaNac = new javax.swing.JTextField();
        txtFechaNac.putClientProperty("JTextField.placeholderText", "dd/mm/aaaa");
        comboCarrera = new javax.swing.JComboBox<>(CARRERAS.toArray(new String[0]));
        comboCarrera.setSelectedIndex(-1);
        txtEmail    = new javax.swing.JTextField();
        txtPass     = new javax.swing.JPasswordField();
        lblError    = new javax.swing.JLabel("");
        lblError.setForeground(new java.awt.Color(200, 0, 0));
        lblError.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        panelFormulario.add(new javax.swing.JLabel("Código *"), "growx");
        panelFormulario.add(txtCodigo, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Nombre *"), "growx");
        panelFormulario.add(txtNombre, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Fecha Nac. *"), "growx");
        panelFormulario.add(txtFechaNac, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Carrera *"), "growx");
        panelFormulario.add(comboCarrera, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Email *"), "growx");
        panelFormulario.add(txtEmail, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Contraseña *"), "growx");
        panelFormulario.add(txtPass, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(lblError, "span, growx, wrap");

        javax.swing.JPanel panelFooter = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fillx, insets 10 20 10 20", "[grow][][]", "[" + UIConstants.BTN_ALTURA + "!]"));
        panelFooter.setBackground(UIConstants.FOOTER_BG);
        panelFooter.add(new javax.swing.JLabel(), "growx");

        javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
        btnCancelar.putClientProperty("FlatLaf.style",
            "arc: 8; borderWidth: 0; focusWidth: 0;"
            + " background: #888888; foreground: #ffffff;"
            + " hoverBackground: #888888; pressedBackground: #888888");
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> this.dispose());
        panelFooter.add(btnCancelar);

        javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
        btnGuardar.putClientProperty("FlatLaf.style",
            "arc: 8; borderWidth: 0; focusWidth: 0; innerFocusWidth: 0;"
            + " background: #711610; foreground: #ffffff;"
            + " hoverBackground: #711610; pressedBackground: #711610");
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardar());
        panelFooter.add(btnGuardar);

        getContentPane().add(panelHeader, "growx, wrap");
        getContentPane().add(panelFormulario, "grow, push, wrap");
        getContentPane().add(panelFooter, "growx");
        panelHeader.setPreferredSize(new java.awt.Dimension(0, UIConstants.HEADER_HEIGHT));
        panelFooter.setPreferredSize(new java.awt.Dimension(0, UIConstants.FOOTER_HEIGHT));

        setMinimumSize(new java.awt.Dimension(460, 520));
        pack();
        setLocationRelativeTo(parent);
    }

    private void guardar() {
        lblError.setText("");
        String codigo   = txtCodigo.getText().trim();
        String nombre   = txtNombre.getText().trim();
        String fechaTxt = txtFechaNac.getText().trim();
        String carrera  = comboCarrera.getSelectedItem() != null
            ? comboCarrera.getSelectedItem().toString() : "";
        String email    = txtEmail.getText().trim();
        String password = new String(txtPass.getPassword()).trim();

        if (codigo.isEmpty() || nombre.isEmpty() || fechaTxt.isEmpty()
                || carrera.isEmpty() || email.isEmpty() || password.isEmpty()) {
            lblError.setText("Todos los campos con * son obligatorios.");
            return;
        }

        String errCod = Validador.validarCodigoPaciente(codigo);
        if (errCod != null) {
            lblError.setText(errCod);
            txtCodigo.requestFocus();
            return;
        }

        String errNomNum = Validador.validarNombreSinNumeros(nombre);
        if (errNomNum != null) {
            lblError.setText(errNomNum);
            txtNombre.requestFocus();
            return;
        }

        LocalDate fechaNac;
        try {
            fechaNac = LocalDate.parse(fechaTxt, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            lblError.setText("Fecha inválida. Use el formato dd/mm/aaaa.");
            txtFechaNac.requestFocus();
            return;
        }

        if (!Validador.esMayorDeEdad(fechaNac)) {
            lblError.setText("El paciente debe ser mayor de edad (+18).");
            txtFechaNac.requestFocus();
            return;
        }

        String errEmail = Validador.validarEmail(email);
        if (errEmail != null) {
            lblError.setText(errEmail);
            return;
        }

        String errNombre = Validador.validarTextoLibre(nombre);
        if (errNombre != null) {
            lblError.setText(errNombre);
            return;
        }

        Estudiante est = new Estudiante();
        est.setId(codigo);
        est.setNombre(nombre);
        est.setFechaNacimiento(fechaNac);
        est.setCarrera(carrera);
        est.setEmail(email);

        boolean ok = new EstudianteDAO().registrar(est, password);
        if (ok) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Paciente registrado correctamente.",
                "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            lblError.setText("Error al registrar. El código ya puede existir.");
        }
    }
}
