package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.db.dao.DoctorDAO;
import pe.edu.uni.centromedico.db.dao.HorarioDAO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import pe.edu.uni.centromedico.models.*;
import pe.edu.uni.centromedico.ui.dialogs.ErrorDialog;

/**
 *
 * @author hector0-0
 */
public class NuevaCitaDialog extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(NuevaCitaDialog.class.getName());

    public NuevaCitaDialog(java.awt.Frame parent, boolean modal, Persona persona) {
        super(parent, modal);
        initComponents();
        // la rueda esta girando mno, tu tranqui
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(
                new net.miginfocom.swing.MigLayout(
                        "fill, insets 0",
                        "[grow]",
                        "[54!][grow,fill][50!]"));
        panelFormulario.removeAll();
        panelFormulario.setLayout(
                new net.miginfocom.swing.MigLayout(
                        "fill, insets 24",
                        "[grow][grow]",
                        "[]10[]10[]10[]10[]10[]10[]"));

        panelFormulario.add(new javax.swing.JLabel("Especialidad"), "growx");
        panelFormulario.add(new javax.swing.JLabel("Medico"), "growx, wrap");
        javax.swing.JComboBox<String> cbEspecialidad = new javax.swing.JComboBox<>();
        panelFormulario.add(cbEspecialidad, "growx");
        javax.swing.JComboBox<String> cbMedico = new javax.swing.JComboBox<>();
        panelFormulario.add(cbMedico, "growx, wrap");
        panelFormulario.add(new javax.swing.JLabel("Fecha"), "growx");
        panelFormulario.add(new javax.swing.JLabel("Hora"), "growx, wrap");
        javax.swing.JComboBox<String> cbFecha = new javax.swing.JComboBox<>();
        panelFormulario.add(cbFecha, "growx");
        javax.swing.JComboBox<String> cbHora = new javax.swing.JComboBox<>();
        panelFormulario.add(cbHora, "growx, wrap");
        panelFormulario.add(new javax.swing.JLabel("Motivo de consulta"), "growx, wrap");
        javax.swing.JTextArea txtMotivoConsulta = new javax.swing.JTextArea(5, 20);
        panelFormulario.add(txtMotivoConsulta, "span 2, growx, wrap");

        DoctorDAO doctorDAO = new DoctorDAO();
        HorarioDAO horarioDAO = new HorarioDAO();
        List<Horario> todosHorarios = horarioDAO.obtenerTodos();

        doctorDAO.obtenerEspecialidades()
                .forEach(cbEspecialidad::addItem);

        // Especialidad → llena médicos disponibles en esa especialidad
        cbEspecialidad.addActionListener(e -> {
            cbMedico.removeAllItems();
            cbFecha.removeAllItems();
            cbHora.removeAllItems();

            String espSeleccionada = (String) cbEspecialidad.getSelectedItem();
            if (espSeleccionada == null)
                return;

            doctorDAO.obtenerPorEspecialidad(espSeleccionada)
                    .forEach(d -> cbMedico.addItem(d.getNombre()));
        });

        // Médico → llena días disponibles de ese médico
        cbMedico.addActionListener(e -> {
            cbFecha.removeAllItems();
            cbHora.removeAllItems();

            String medSeleccionado = (String) cbMedico.getSelectedItem();
            if (medSeleccionado == null)
                return;

            todosHorarios.stream()
                    .filter(h -> h.getNombreDoctor().equals(medSeleccionado) && h.isDisponible())
                    .map(Horario::getDiaSemana)
                    .distinct()
                    .forEach(cbFecha::addItem);
        });

        // Día → llena horas disponibles de ese médico en ese día
        cbFecha.addActionListener(e -> {
            cbHora.removeAllItems();

            String medSeleccionado = (String) cbMedico.getSelectedItem();
            String diaSeleccionado = (String) cbFecha.getSelectedItem();
            if (medSeleccionado == null || diaSeleccionado == null)
                return;

            todosHorarios.stream()
                    .filter(h -> h.getNombreDoctor().equals(medSeleccionado)
                            && h.getDiaSemana().equals(diaSeleccionado)
                            && h.isDisponible())
                    .forEach(h -> cbHora.addItem(h.getHoraInicio() + " - " + h.getHoraFin()));
        });

        // Footer
        panelFooter.removeAll();
        // espacio vacio
        panelFooter.add(new javax.swing.JLabel(), "growx");
        panelFooter.setLayout(
                new net.miginfocom.swing.MigLayout(
                        "fillx, insets 10 20 10 20",
                        "[grow][][]",
                        "[38!]"));

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
        btnGuardar.addActionListener(e -> {
            if (txtMotivoConsulta.getText().isEmpty()
                    || cbEspecialidad.getSelectedItem() == null
                    || cbMedico.getSelectedItem() == null
                    || cbFecha.getSelectedItem() == null
                    || cbHora.getSelectedItem() == null) {
                new ErrorDialog(null, true, "Por favor complete todos los campos")
                        .setVisible(true);
                return;
            }

            // Encontrar el horario seleccionado para obtener su ID
            String medSeleccionado = (String) cbMedico.getSelectedItem();
            String diaSeleccionado = (String) cbFecha.getSelectedItem();
            String horaSeleccionada = (String) cbHora.getSelectedItem();

            Horario horarioSeleccionado = todosHorarios.stream()
                    .filter(h -> h.getNombreDoctor().equals(medSeleccionado)
                            && h.getDiaSemana().equals(diaSeleccionado)
                            && (h.getHoraInicio() + " - " + h.getHoraFin()).equals(horaSeleccionada)
                            && h.isDisponible())
                    .findFirst()
                    .orElse(null);

            if (horarioSeleccionado == null) {
                new ErrorDialog(null, true, "El horario seleccionado ya no está disponible.")
                        .setVisible(true);
                return;
            }

            // Obtener el doctor para conseguir su ID
            Doctor doctor = doctorDAO.obtenerPorEspecialidad(
                    (String) cbEspecialidad.getSelectedItem())
                    .stream()
                    .filter(d -> d.getNombre().equals(medSeleccionado))
                    .findFirst()
                    .orElse(null);

            if (doctor == null) {
                new ErrorDialog(null, true, "Error al obtener datos del médico.")
                        .setVisible(true);
                return;
            }

            CitaDAO citaDAO = new CitaDAO();
            boolean exito = citaDAO.crearCita(
                    persona.getId(), // ← getId() no getCodigo()
                    doctor.getId(),
                    horarioSeleccionado.getId(),
                    txtMotivoConsulta.getText().trim());

            if (exito) {
                this.dispose();
            } else {
                new ErrorDialog(null, true, "Error al guardar la cita. Intente nuevamente.")
                        .setVisible(true);
            }
        });
        panelFooter.add(btnGuardar);

        panelHeader.removeAll();

        panelHeader.setLayout(
                new net.miginfocom.swing.MigLayout(
                        "fill, insets 0 20 0 20",
                        "[grow]",
                        "[center]"));

        javax.swing.JLabel titulo = new javax.swing.JLabel("Nueva Cita");
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        panelFormulario = new javax.swing.JPanel();
        panelFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NuevaCita");
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
                        .addGap(0, 100, Short.MAX_VALUE));
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
                                .addGap(143, 143, 143)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelFooter, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(panelHeader,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(panelFormulario,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(249, 249, 249))))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panelFooter, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
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
                NuevaCitaDialog dialog = new NuevaCitaDialog(new javax.swing.JFrame(), true, null);
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
