package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.db.dao.DoctorDAO;
import pe.edu.uni.centromedico.db.dao.SlotDAO;
import pe.edu.uni.centromedico.service.CitaService;
import java.util.List;
import pe.edu.uni.centromedico.models.*;

public class NuevaCitaDialog extends javax.swing.JDialog {

    private final javax.swing.JPanel panelHeader;
    private final javax.swing.JPanel panelFormulario;
    private final javax.swing.JPanel panelFooter;

    public NuevaCitaDialog(java.awt.Frame parent, boolean modal, Persona persona) {
        super(parent, modal);
        if (persona == null) {
            throw new IllegalArgumentException(
                    "NuevaCitaDialog requiere una persona con sesión activa.");
        }

        panelHeader = new javax.swing.JPanel();
        panelFormulario = new javax.swing.JPanel();
        panelFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NuevaCita");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(480, 600));
        setResizable(false);

        panelHeader.setBackground(new java.awt.Color(139, 20, 20));
        panelFooter.setBackground(new java.awt.Color(240, 235, 230));

        this.getContentPane().setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 0",
                "[grow]",
                "[54!][grow,fill][50!]"));

        panelFormulario.setLayout(new net.miginfocom.swing.MigLayout(
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
        SlotDAO slotDAO = new SlotDAO();
        List<Slot> todosSlots = slotDAO.obtenerTodos();

        doctorDAO.obtenerNombresEspecialidad()
                .forEach(cbEspecialidad::addItem);

        cbEspecialidad.addActionListener(e -> {
            cbMedico.removeAllItems();
            cbFecha.removeAllItems();
            cbHora.removeAllItems();

            String espSeleccionada = (String) cbEspecialidad.getSelectedItem();
            if (espSeleccionada == null) return;

            doctorDAO.obtenerPorEspecialidad(espSeleccionada)
                    .forEach(d -> cbMedico.addItem(d.getNombre()));
        });

        cbMedico.addActionListener(e -> {
            cbFecha.removeAllItems();
            cbHora.removeAllItems();

            String medSeleccionado = (String) cbMedico.getSelectedItem();
            if (medSeleccionado == null) return;

            todosSlots.stream()
                    .filter(s -> s.getNombreDoctor().equals(medSeleccionado) && s.isDisponible())
                    .map(Slot::getDiaSemana)
                    .distinct()
                    .forEach(cbFecha::addItem);
        });

        cbFecha.addActionListener(e -> {
            cbHora.removeAllItems();

            String medSeleccionado = (String) cbMedico.getSelectedItem();
            String diaSeleccionado = (String) cbFecha.getSelectedItem();
            if (medSeleccionado == null || diaSeleccionado == null) return;

            todosSlots.stream()
                    .filter(s -> s.getNombreDoctor().equals(medSeleccionado)
                            && s.getDiaSemana().equals(diaSeleccionado)
                            && s.isDisponible())
                    .forEach(s -> cbHora.addItem(s.getHoraInicio() + " - " + s.getHoraFin()));
        });

        panelFooter.setLayout(new net.miginfocom.swing.MigLayout(
                "fillx, insets 10 20 10 20",
                "[grow][][]",
                "[38!]"));

        javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
        btnCancelar.setBackground(new java.awt.Color(139, 20, 20));
        btnCancelar.setForeground(java.awt.Color.WHITE);
        btnCancelar.addActionListener(e -> this.dispose());
        panelFooter.add(btnCancelar);

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

            String medSeleccionado = (String) cbMedico.getSelectedItem();
            String diaSeleccionado = (String) cbFecha.getSelectedItem();
            String horaSeleccionada = (String) cbHora.getSelectedItem();

            Slot slotSeleccionado = todosSlots.stream()
                    .filter(s -> s.getNombreDoctor().equals(medSeleccionado)
                            && s.getDiaSemana().equals(diaSeleccionado)
                            && (s.getHoraInicio() + " - " + s.getHoraFin())
                                    .equals(horaSeleccionada)
                            && s.isDisponible())
                    .findFirst()
                    .orElse(null);

            if (slotSeleccionado == null) {
                new ErrorDialog(null, true, "El slot seleccionado ya no está disponible.")
                        .setVisible(true);
                return;
            }

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

            boolean exito = new CitaService().agendarCita(
                    persona.getId(),
                    doctor.getId(),
                    slotSeleccionado.getId(),
                    txtMotivoConsulta.getText().trim());

            if (exito) {
                this.dispose();
            } else {
                new ErrorDialog(null, true, "Error al guardar la cita. Intente nuevamente.")
                        .setVisible(true);
            }
        });
        panelFooter.add(btnGuardar);

        panelHeader.setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 0 20 0 20",
                "[grow]",
                "[center]"));

        javax.swing.JLabel titulo = new javax.swing.JLabel("Nueva Cita");
        titulo.setForeground(java.awt.Color.WHITE);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

        panelHeader.add(titulo, "align left");

        this.getContentPane().add(panelHeader, "growx, wrap");
        this.getContentPane().add(panelFormulario, "grow, push, wrap");
        this.getContentPane().add(panelFooter, "growx");
        panelHeader.setPreferredSize(new java.awt.Dimension(0, 54));
        panelFooter.setPreferredSize(new java.awt.Dimension(0, 50));

        this.pack();
        this.setLocationRelativeTo(null);
    }
}
