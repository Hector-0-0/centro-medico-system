package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.models.*;
import pe.edu.uni.centromedico.db.dao.EstudianteDAO;
import pe.edu.uni.centromedico.db.dao.DoctorDAO;

public class EditarPerfilDialog extends javax.swing.JDialog {

    private final javax.swing.JTextField txtNombre;
    private final javax.swing.JTextField txtEmail;
    private final javax.swing.JTextField txtCarrera;
    private final javax.swing.JTextField txtConsultorio;
    private final javax.swing.JPanel filasEstudiante;
    private final javax.swing.JPanel filasDoctor;
    private boolean guardado = false;

    public EditarPerfilDialog(java.awt.Frame parent, boolean modal, Persona persona) {
        super(parent, modal);
        setTitle("Editar Perfil");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(420, 380));
        setResizable(false);
        setModal(true);

        getContentPane().setLayout(new java.awt.BorderLayout());

        javax.swing.JPanel panelHeader = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fill, insets 0 20 0 20", "[grow]", "[center]"));
        panelHeader.setBackground(new java.awt.Color(139, 20, 20));
        javax.swing.JLabel titulo = new javax.swing.JLabel("Editar Perfil");
        titulo.setForeground(java.awt.Color.WHITE);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        panelHeader.add(titulo, "align left");
        panelHeader.setPreferredSize(new java.awt.Dimension(0, 54));
        getContentPane().add(panelHeader, java.awt.BorderLayout.NORTH);

        javax.swing.JPanel body = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fill, insets 24", "[grow]", "[]10[]10[]10[]"));
        body.setBackground(java.awt.Color.WHITE);

        body.add(new javax.swing.JLabel("Nombre"), "growx, wrap");
        txtNombre = new javax.swing.JTextField(persona.getNombre());
        body.add(txtNombre, "growx, wrap");

        filasEstudiante = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fill, insets 0", "[grow]", "[]10[]10[]"));
        filasEstudiante.setBackground(java.awt.Color.WHITE);
        filasEstudiante.add(new javax.swing.JLabel("Carrera"), "growx, wrap");
        txtCarrera = new javax.swing.JTextField();
        filasEstudiante.add(txtCarrera, "growx, wrap");
        filasEstudiante.add(new javax.swing.JLabel("Email"), "growx, wrap");
        txtEmail = new javax.swing.JTextField();
        filasEstudiante.add(txtEmail, "growx, wrap");
        body.add(filasEstudiante, "growx, wrap");

        filasDoctor = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fill, insets 0", "[grow]", "[]10[]"));
        filasDoctor.setBackground(java.awt.Color.WHITE);
        filasDoctor.add(new javax.swing.JLabel("Consultorio"), "growx, wrap");
        txtConsultorio = new javax.swing.JTextField();
        filasDoctor.add(txtConsultorio, "growx, wrap");
        body.add(filasDoctor, "growx, wrap");

        if (persona instanceof Estudiante e) {
            filasDoctor.setVisible(false);
            txtCarrera.setText(e.getCarrera());
            txtEmail.setText(e.getEmail() != null ? e.getEmail() : "");
        } else if (persona instanceof Doctor d) {
            filasEstudiante.setVisible(false);
            txtConsultorio.setText(d.getConsultorio() != null ? d.getConsultorio() : "");
        }

        getContentPane().add(body, java.awt.BorderLayout.CENTER);

        javax.swing.JPanel footer = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fillx, insets 10 20 10 20", "[grow][][]"));
        footer.setBackground(new java.awt.Color(240, 235, 230));
        footer.setPreferredSize(new java.awt.Dimension(0, 50));

        javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
        btnCancelar.setBackground(new java.awt.Color(139, 20, 20));
        btnCancelar.setForeground(java.awt.Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());
        footer.add(btnCancelar);

        javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar Cambios");
        btnGuardar.setBackground(new java.awt.Color(139, 20, 20));
        btnGuardar.setForeground(java.awt.Color.WHITE);
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                new ErrorDialog(null, true, "El nombre no puede estar vacío").setVisible(true);
                return;
            }
            String nombre = txtNombre.getText().trim();
            if (persona instanceof Estudiante est) {
                est.setNombre(nombre);
                est.setCarrera(txtCarrera.getText().trim());
                est.setEmail(txtEmail.getText().trim());
                boolean ok = new EstudianteDAO().actualizar(est);
                if (!ok) {
                    new ErrorDialog(null, true, "Error al guardar los cambios").setVisible(true);
                    return;
                }
            } else if (persona instanceof Doctor doc) {
                doc.setNombre(nombre);
                doc.setConsultorio(txtConsultorio.getText().trim());
                boolean ok = new DoctorDAO().actualizar(doc);
                if (!ok) {
                    new ErrorDialog(null, true, "Error al guardar los cambios").setVisible(true);
                    return;
                }
            }
            guardado = true;
            dispose();
        });
        footer.add(btnGuardar);
        getContentPane().add(footer, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isGuardado() { return guardado; }
}
