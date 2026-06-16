package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.AtencionDAO;
import pe.edu.uni.centromedico.db.dao.CodigoCieDAO;
import pe.edu.uni.centromedico.models.AtencionCita;
import pe.edu.uni.centromedico.models.AtencionDiagnostico;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.service.CitaService;
import pe.edu.uni.centromedico.ui.panels.HistorialPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;
import pe.edu.uni.centromedico.util.SesionManager;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;

public class HistorialController {

    private final HistorialPanel vista;
    private final CitaService    citaService;
    private final AtencionDAO    atencionDAO;
    private final CodigoCieDAO   codigoCieDAO;

    public HistorialController(HistorialPanel vista) {
        this.vista         = vista;
        this.citaService   = new CitaService();
        this.atencionDAO   = new AtencionDAO();
        this.codigoCieDAO  = new CodigoCieDAO();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        cargarTabla(citaService.obtenerHistorialPaciente(SesionManager.getId()));
    }

    private void cargarTabla(List<Cita> citas) {
        String[][] datos = new String[citas.size()][6];
        for (int i = 0; i < citas.size(); i++) {
            Cita c = citas.get(i);
            String medico = c.getNombreDoctor() != null ? c.getNombreDoctor() : c.getIdDoctor();
            String hora   = (c.getHoraInicio() != null ? c.getHoraInicio() : "")
                          + (c.getHoraFin()    != null ? " - " + c.getHoraFin() : "");
            datos[i] = new String[]{
                c.getEspecialidad() != null ? c.getEspecialidad() : "—",
                medico,
                c.getDiaSemana() != null ? c.getDiaSemana() : "—",
                hora,
                c.getMotivo(),
                c.getEstado()
            };
        }
        vista.getTblCitas().setModel(new DefaultTableModel(
            datos, new String[]{"Especialidad","Médico","Día","Hora","Motivo","Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getTxtBuscar().addActionListener(e -> ErrorHandler.ejecutarSeguro(vista, this::buscar));
        vista.getTblCitas().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    ErrorHandler.ejecutarSeguro(vista, HistorialController.this::mostrarDetalle);
            }
        });
    }

    private void mostrarDetalle() {
        int row = vista.getTblCitas().getSelectedRow();
        if (row < 0) return;

        // Obtener la cita seleccionada desde el modelo de tabla
        String especialidad = vista.getTblCitas().getValueAt(row, 0) != null
            ? vista.getTblCitas().getValueAt(row, 0).toString() : "—";
        String medico = vista.getTblCitas().getValueAt(row, 1) != null
            ? vista.getTblCitas().getValueAt(row, 1).toString() : "—";
        String motivo = vista.getTblCitas().getValueAt(row, 4) != null
            ? vista.getTblCitas().getValueAt(row, 4).toString() : "—";

        // Buscar idCita desde la lista original
        List<Cita> citas = citaService.obtenerHistorialPaciente(SesionManager.getId());
        if (row >= citas.size()) return;
        Cita cita = citas.get(row);

        AtencionCita atencion = atencionDAO.obtenerPorCitaId(cita.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("=== Diagnósticos ===\n");

        if (atencion != null) {
            List<AtencionDiagnostico> diags = codigoCieDAO.obtenerPorAtencion(atencion.getId());
            if (!diags.isEmpty()) {
                for (AtencionDiagnostico d : diags) {
                    sb.append("• ").append(d.getCodigoCie())
                      .append(" - ").append(d.getDescripcionCie());
                    if (d.getObservacion() != null && !d.getObservacion().isEmpty()) {
                        sb.append(" (").append(d.getObservacion()).append(")");
                    }
                    sb.append("\n");
                }
            } else if (atencion.getDiagnostico() != null && !atencion.getDiagnostico().isEmpty()) {
                sb.append(atencion.getDiagnostico()).append("\n");
            } else {
                sb.append("(Sin diagnóstico registrado)\n");
            }

            sb.append("\n=== Comentarios ===\n");
            sb.append(atencion.getComentarios() != null && !atencion.getComentarios().isEmpty()
                ? atencion.getComentarios() : "(Sin comentarios)");
        } else {
            sb.append("(Cita no atendida aún)\n");
        }

        String titulo = "Detalle de cita - " + especialidad + " - Dr(a). " + medico;
        ErrorHandler.mostrarInfo(vista, titulo, sb.toString());
    }

    private void buscar() {
        String texto = vista.getTxtBuscar().getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarDatos(); return; }
        List<Cita> filtradas = citaService.obtenerHistorialPaciente(SesionManager.getId())
            .stream()
            .filter(c -> (c.getEspecialidad() != null && c.getEspecialidad().toLowerCase().contains(texto))
                      || (c.getNombreDoctor()  != null && c.getNombreDoctor().toLowerCase().contains(texto))
                      || c.getMotivo().toLowerCase().contains(texto)
                      || c.getEstado().toLowerCase().contains(texto))
            .toList();
        cargarTabla(filtradas);
    }
}
