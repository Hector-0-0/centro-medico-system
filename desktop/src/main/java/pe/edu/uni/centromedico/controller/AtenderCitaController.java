package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.AtencionDAO;
import pe.edu.uni.centromedico.db.dao.CodigoCieDAO;
import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.models.AtencionCita;
import pe.edu.uni.centromedico.models.AtencionDiagnostico;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.models.CodigoCie;
import pe.edu.uni.centromedico.models.Medicamento;
import pe.edu.uni.centromedico.models.RecetaDetalle;
import pe.edu.uni.centromedico.service.RecetaService;
import pe.edu.uni.centromedico.ui.frames.MainFrame;
import pe.edu.uni.centromedico.ui.panels.AtenderCitaPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class AtenderCitaController {

    private final AtenderCitaPanel  vista;
    private final AtencionDAO       atencionDAO;
    private final RecetaService     recetaService;
    private final MedicamentoDAO    medicamentoDAO;
    private final CodigoCieDAO      codigoCieDAO;
    private final Cita              citaActual;

    private List<Medicamento>              medicamentosDisponibles;
    private List<CodigoCie>                ultimosResultadosBusqueda;
    private final List<AtencionDiagnostico> diagnosticosCie;
    private final List<RecetaDetalle>       detallesReceta;

    public AtenderCitaController(AtenderCitaPanel vista, Cita cita) {
        this.vista              = vista;
        this.atencionDAO        = new AtencionDAO();
        this.recetaService      = new RecetaService();
        this.medicamentoDAO     = new MedicamentoDAO();
        this.codigoCieDAO       = new CodigoCieDAO();
        this.citaActual         = cita;
        this.diagnosticosCie    = new ArrayList<>();
        this.detallesReceta     = new ArrayList<>();
        mostrarDatosPaciente();
        inicializarTablaReceta();
        inicializarTablaDiagnosticos();
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

    private void inicializarTablaDiagnosticos() {
        vista.getTblDiagnosticos().setModel(new DefaultTableModel(
            new Object[0][3], new String[]{"Código", "Descripción", "Observación"}) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
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
        // Receta
        vista.getBtnAgregarMed().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::agregarMedicamento));
        // CIE
        vista.getBtnBuscarCie().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::buscarCie));
        vista.getTxtBuscarCie().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::buscarCie));
        vista.getTxtBuscarCie().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) {
                if (vista.getTxtBuscarCie().getText().trim().length() >= 2) {
                    ErrorHandler.ejecutarSeguro(vista, AtenderCitaController.this::buscarCie);
                }
            }
        });
        vista.getBtnAgregarCie().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::agregarDiagnosticoCie));
        vista.getBtnQuitarCie().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::quitarDiagnosticoCie));
        // Guardar / Cancelar
        vista.getBtnGuardar().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::guardarConsulta));
        vista.getBtnCancelar().addActionListener(e ->
            ErrorHandler.ejecutarSeguro(vista, this::volver));
    }

    // ── CIE: Buscar ──────────────────────────────────────────────────────
    private void buscarCie() {
        String texto = vista.getTxtBuscarCie().getText().trim();
        if (texto.isEmpty()) return;
        ultimosResultadosBusqueda = codigoCieDAO.buscarPorTexto(texto);
        DefaultListModel<String> model = new DefaultListModel<>();
        for (CodigoCie cie : ultimosResultadosBusqueda) {
            model.addElement(cie.toString());
        }
        vista.getLstResultadosCie().setModel(model);
        vista.getLstResultadosCie().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // ── CIE: Agregar diagnóstico ─────────────────────────────────────────
    private void agregarDiagnosticoCie() {
        int idx = vista.getLstResultadosCie().getSelectedIndex();
        if (idx < 0 || ultimosResultadosBusqueda == null || idx >= ultimosResultadosBusqueda.size()) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Selecciona un código CIE de la lista de resultados.");
            return;
        }
        CodigoCie seleccionado = ultimosResultadosBusqueda.get(idx);

        boolean duplicado = diagnosticosCie.stream()
            .anyMatch(d -> d.getIdCie() == seleccionado.getId());
        if (duplicado) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Ese diagnóstico CIE ya está agregado.");
            return;
        }

        AtencionDiagnostico diag = new AtencionDiagnostico(
            seleccionado.getId(),
            seleccionado.getCodigo(),
            seleccionado.getDescripcion(),
            ""
        );
        diagnosticosCie.add(diag);

        DefaultTableModel model = (DefaultTableModel) vista.getTblDiagnosticos().getModel();
        model.addRow(new Object[]{
            diag.getCodigoCie(),
            diag.getDescripcionCie(),
            diag.getObservacion()
        });

        vista.getTxtBuscarCie().setText("");
        vista.getLstResultadosCie().setModel(new DefaultListModel<>());
        ultimosResultadosBusqueda = null;
    }

    // ── CIE: Quitar diagnóstico ──────────────────────────────────────────
    private void quitarDiagnosticoCie() {
        int fila = vista.getTblDiagnosticos().getSelectedRow();
        if (fila < 0) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Selecciona un diagnóstico de la tabla para quitarlo.");
            return;
        }
        diagnosticosCie.remove(fila);
        ((DefaultTableModel) vista.getTblDiagnosticos().getModel()).removeRow(fila);
    }

    // ── Receta: agregar medicamento ──────────────────────────────────────
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

    // ── Guardar consulta ─────────────────────────────────────────────────
    private void guardarConsulta() {
        if (citaActual == null) {
            ErrorHandler.mostrarError(vista, "No hay cita activa para registrar.");
            return;
        }

        if (diagnosticosCie.isEmpty()) {
            ErrorHandler.mostrarAdvertencia(vista,
                "Debe agregar al menos un diagnóstico CIE.");
            return;
        }

        // Sincronizar observaciones editadas en la tabla
        DefaultTableModel modelDiag = (DefaultTableModel) vista.getTblDiagnosticos().getModel();
        for (int i = 0; i < diagnosticosCie.size() && i < modelDiag.getRowCount(); i++) {
            Object obs = modelDiag.getValueAt(i, 2);
            diagnosticosCie.get(i).setObservacion(obs != null ? obs.toString() : "");
        }

        String comentarios = vista.getTaComentarios().getText().trim();

        AtencionCita atencion = new AtencionCita();
        atencion.setIdCita(citaActual.getId());
        atencion.setDiagnostico("");
        atencion.setComentarios(comentarios);
        atencion.setDiagnosticos(diagnosticosCie);

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
