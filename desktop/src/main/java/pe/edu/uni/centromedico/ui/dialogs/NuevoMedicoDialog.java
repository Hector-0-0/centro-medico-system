package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.db.dao.DoctorDAO;
import pe.edu.uni.centromedico.db.dao.EspecialidadDAO;
import pe.edu.uni.centromedico.models.Doctor;
import pe.edu.uni.centromedico.models.Especialidad;
import pe.edu.uni.centromedico.util.UIConstants;
import pe.edu.uni.centromedico.util.Validador;
import java.util.List;

public class NuevoMedicoDialog extends javax.swing.JDialog {

    private final javax.swing.JTextField txtCodigo;
    private final javax.swing.JTextField txtNombre;
    private final javax.swing.JComboBox<Especialidad> combEspecialidad;
    private final javax.swing.JTextField txtConsultorio;
    private final javax.swing.JPasswordField txtPass;
    private final javax.swing.JLabel lblError;

    public NuevoMedicoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Registrar Médico");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);

        getContentPane().setLayout(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow]", "[54!][grow,fill][50!]"));

        javax.swing.JPanel panelHeader = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fill, insets 0 20 0 20", "[grow]", "[center]"));
        panelHeader.setBackground(UIConstants.CARMESI);
        javax.swing.JLabel titulo = new javax.swing.JLabel("Registrar Médico");
        titulo.setForeground(UIConstants.BLANCO);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        panelHeader.add(titulo, "align left");
        panelHeader.setPreferredSize(new java.awt.Dimension(0, UIConstants.HEADER_HEIGHT));

        javax.swing.JPanel panelFormulario = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[130!][grow]", "[]8[]8[]8[]8[]8[]0[]"));

        txtCodigo   = new javax.swing.JTextField();
        txtNombre   = new javax.swing.JTextField();

        List<Especialidad> especialidades = new EspecialidadDAO().obtenerTodas();
        combEspecialidad = new javax.swing.JComboBox<>(
            especialidades.toArray(new Especialidad[0]));
        combEspecialidad.setSelectedIndex(-1);

        txtConsultorio = new javax.swing.JTextField();
        txtPass        = new javax.swing.JPasswordField();
        lblError = new javax.swing.JLabel("");
        lblError.setForeground(new java.awt.Color(200, 0, 0));
        lblError.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        panelFormulario.add(new javax.swing.JLabel("Código *"), "growx");
        panelFormulario.add(txtCodigo, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Nombre *"), "growx");
        panelFormulario.add(txtNombre, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Especialidad *"), "growx");
        panelFormulario.add(combEspecialidad, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Consultorio *"), "growx");
        panelFormulario.add(txtConsultorio, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(new javax.swing.JLabel("Contraseña *"), "growx");
        panelFormulario.add(txtPass, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        panelFormulario.add(lblError, "span, growx, wrap");

        javax.swing.JPanel panelFooter = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fillx, insets 10 20 10 20", "[grow][][]", "[" + UIConstants.BTN_ALTURA + "!]"));
        panelFooter.setBackground(UIConstants.FOOTER_BG);
        panelFooter.add(new javax.swing.JLabel(), "growx");

        javax.swing.JButton btnCancelar = UIConstants.crearBotonSecundario("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelFooter.add(btnCancelar);

        javax.swing.JButton btnGuardar = UIConstants.crearBotonPrimario("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        panelFooter.add(btnGuardar);

        getContentPane().add(panelHeader, "growx, wrap");
        getContentPane().add(panelFormulario, "grow, push, wrap");
        getContentPane().add(panelFooter, "growx");
        panelFooter.setPreferredSize(new java.awt.Dimension(0, UIConstants.FOOTER_HEIGHT));

        setMinimumSize(new java.awt.Dimension(460, 460));
        pack();
        setLocationRelativeTo(parent);
    }

    private void guardar() {
        lblError.setText("");
        String codigo       = txtCodigo.getText().trim();
        String nombre       = txtNombre.getText().trim();
        Especialidad esp    = (Especialidad) combEspecialidad.getSelectedItem();
        String consultorio  = txtConsultorio.getText().trim();
        String password     = new String(txtPass.getPassword()).trim();

        if (codigo.isEmpty() || nombre.isEmpty() || esp == null
                || consultorio.isEmpty() || password.isEmpty()) {
            lblError.setText("Todos los campos con * son obligatorios.");
            return;
        }

        String errCod = Validador.validarCodigoDoctor(codigo);
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

        Doctor doc = new Doctor();
        doc.setId(codigo);
        doc.setNombre(nombre);
        doc.setEspecialidadId(esp.getId());
        doc.setEspecialidad(esp.getNombre());
        doc.setConsultorio(consultorio);

        boolean ok = new DoctorDAO().registrar(doc, password);
        if (ok) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Médico registrado correctamente.",
                "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            lblError.setText("Error al registrar. El código ya puede existir.");
        }
    }
}
