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
import pe.edu.uni.centromedico.util.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AtenderCitaController {

    private final AtenderCitaPanel  vista;
    private final AtencionDAO       atencionDAO;
    private final RecetaService     recetaService;
    private final MedicamentoDAO    medicamentoDAO;
    private final Cita              citaActual;

    private List<Medicamento>       medicamentosDisponibles;
    private final List<RecetaDetalle> detallesReceta = new ArrayList<>();

    public AtenderCitaController(AtenderCitaPanel vista, Cita cita) {
        this.vista          = vista;
        this.atencionDAO    = new AtencionDAO();
        this.recetaService  = new RecetaService();
        this.medicamentoDAO = new MedicamentoDAO();
        this.citaActual     = cita;
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
        vista.getBtnAgregarMed().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::agregarMedicamento));
        vista.getBtnGuardar().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::guardarConsulta));
        vista.getBtnCancelar().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::volver));
    }

    private void agregarMedicamento() {
        int idx = vista.getCbMedicamento().getSelectedIndex();
        if (idx < 0 || medicamentosDisponibles.isEmpty()) {
            ErrorHandler.mostrarAdvertencia(vista, "Selecciona un medicamento del combo.");
            return;
        }
        String dosis    = vista.getTxtDosisMed().getText().trim();
        String duracion = vista.getTxtDuraMed().getText().trim();
        if (dosis.isEmpty() || duracion.isEmpty()) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Ingresa la dosis y la duración del medicamento.");
            return;
        }

        Medicamento med = medicamentosDisponibles.get(idx);

        // Evitar duplicados de un mismo medicamento
        boolean duplicado = detallesReceta.stream()
            .anyMatch(d -> d.getIdMedicamento().equals(med.getId()));
        if (duplicado) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Ese medicamento ya está en la receta.");
            return;
        }

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
        if (citaActual == null) {
            ErrorHandler.mostrarError(vista, "No hay cita activa para registrar.");
            return;
        }
        String diagnostico = vista.getTaDiagnostico().getText().trim();
        String tratamiento = vista.getTaTratamiento().getText().trim();

        if (diagnostico.isEmpty()) {
            ErrorHandler.mostrarAdvertencia(vista, "El diagnóstico no puede estar vacío.");
            return;
        }

        AtencionCita atencion = new AtencionCita();
        atencion.setIdCita(citaActual.getId());
        atencion.setDiagnostico(diagnostico);
        atencion.setComentarios(tratamiento);

        int idAtencion = atencionDAO.registrar(atencion);
        if (idAtencion <= 0) {
            ErrorHandler.mostrarError(vista,
                "Error al guardar la consulta. Intenta nuevamente.");
            return;
        }

        if (!detallesReceta.isEmpty()) {
            boolean recetaOk = recetaService.registrarReceta(idAtencion, detallesReceta);
            if (!recetaOk) {
                ErrorHandler.mostrarError(vista,
                    "La consulta se guardó, pero hubo un problema al registrar la receta.");
                volver();
                return;
            }
        }

        ErrorHandler.mostrarInfo(vista, "Éxito", "Consulta guardada correctamente.");
        volver();
    }

    private void volver() {
        MainFrame mf = MainFrame.getInstance();
        if (mf == null) return;
        pe.edu.uni.centromedico.ui.panels.CitaPanel panel =
            new pe.edu.uni.centromedico.ui.panels.CitaPanel();
        new CitaController(panel);
        mf.mostrarPanel(panel, "Mis Citas");
    }
}
