package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.AtencionDAO;
import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.models.AtencionCita;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.models.Medicamento;
import pe.edu.uni.centromedico.models.RecetaDetalle;
import pe.edu.uni.centromedico.service.RecetaService;
import pe.edu.uni.centromedico.ui.frames.MainFrame;
import pe.edu.uni.centromedico.ui.panels.AtenderCitaPanel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AtenderCitaController {

    private final AtenderCitaPanel vista;
    private final AtencionDAO      atencionDAO;
    private final RecetaService    recetaService;
    private final MedicamentoDAO   medicamentoDAO;
    private final Cita             citaActual;

    // Medicamentos disponibles cargados en el combo (mismo índice que el combo)
    private List<Medicamento> medicamentosDisponibles;
    // Detalles de receta acumulados mientras el médico agrega medicamentos
    private final List<RecetaDetalle> detallesReceta = new ArrayList<>();

    public AtenderCitaController(AtenderCitaPanel vista, Cita cita) {
        this.vista           = vista;
        this.atencionDAO     = new AtencionDAO();
        this.recetaService   = new RecetaService();
        this.medicamentoDAO  = new MedicamentoDAO();
        this.citaActual      = cita;
        mostrarDatosPaciente();
        inicializarTablaReceta();
        cargarMedicamentos();
        conectarEventos();
    }

    private void mostrarDatosPaciente() {
        if (citaActual == null) return;
        vista.setDatosPaciente(
            citaActual.getNombreEstudiante() != null
                ? citaActual.getNombreEstudiante() : citaActual.getIdEstudiante(),
            citaActual.getIdEstudiante(),
            "—"
        );
    }

    private void inicializarTablaReceta() {
        vista.getTblReceta().setModel(new DefaultTableModel(
            new Object[0][3], new String[]{"Medicamento", "Dosis", "Duración"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void cargarMedicamentos() {
        medicamentosDisponibles = medicamentoDAO.obtenerConStock();
        vista.getCbMedicamento().removeAllItems();
        for (Medicamento m : medicamentosDisponibles) {
            vista.getCbMedicamento().addItem(m.getNombre() + " (stock: " + m.getStock() + ")");
        }
    }

    private void conectarEventos() {
        vista.getBtnAgregarMed().addActionListener(e -> agregarMedicamento());
        vista.getBtnGuardar().addActionListener(e -> guardarConsulta());
        vista.getBtnCancelar().addActionListener(e -> volver());
    }

    private void agregarMedicamento() {
        int idx = vista.getCbMedicamento().getSelectedIndex();
        if (idx < 0 || medicamentosDisponibles.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                "Selecciona un medicamento del combo.",
                "Sin selección", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        String dosis    = vista.getTxtDosisMed().getText().trim();
        String duracion = vista.getTxtDuraMed().getText().trim();
        if (dosis.isEmpty() || duracion.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                "Ingresa la dosis y la duración del medicamento.",
                "Campos requeridos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        Medicamento med = medicamentosDisponibles.get(idx);
        RecetaDetalle detalle = new RecetaDetalle();
        detalle.setIdMedicamento(med.getId());
        detalle.setNombreMedicamento(med.getNombre());
        detalle.setDosis(dosis);
        detalle.setDuracion(duracion);
        detallesReceta.add(detalle);

        DefaultTableModel model = (DefaultTableModel) vista.getTblReceta().getModel();
        model.addRow(new Object[]{ med.getNombre(), dosis, duracion });

        vista.getTxtDosisMed().setText("");
        vista.getTxtDuraMed().setText("");
    }

    private void guardarConsulta() {
        String diagnostico = vista.getTaDiagnostico().getText().trim();
        String tratamiento = vista.getTaTratamiento().getText().trim();

        if (diagnostico.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                "El diagnóstico no puede estar vacío.",
                "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        AtencionCita atencion = new AtencionCita();
        atencion.setIdCita(citaActual != null ? citaActual.getId() : 0);
        atencion.setDiagnostico(diagnostico);
        atencion.setComentarios(tratamiento);

        boolean exito = atencionDAO.registrar(atencion);
        if (!exito) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                "Error al guardar la consulta. Intenta nuevamente.",
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registrar receta si el médico añadió medicamentos
        if (!detallesReceta.isEmpty()) {
            pe.edu.uni.centromedico.models.AtencionCita guardada =
                atencionDAO.obtenerPorCita(citaActual.getId());
            if (guardada != null) {
                recetaService.registrarReceta(guardada.getId(), detallesReceta);
            }
        }

        javax.swing.JOptionPane.showMessageDialog(vista,
            "Consulta guardada correctamente.",
            "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        volver();
    }

    private void volver() {
        MainFrame mf = MainFrame.getInstance();
        if (mf != null) {
            pe.edu.uni.centromedico.ui.panels.CitaPanel panel =
                new pe.edu.uni.centromedico.ui.panels.CitaPanel();
            new CitaController(panel);
            mf.mostrarPanel(panel, "Mis Citas");
        }
    }
}
