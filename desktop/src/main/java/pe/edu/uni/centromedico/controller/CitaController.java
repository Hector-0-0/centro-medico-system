package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.service.CitaService;
import pe.edu.uni.centromedico.ui.panels.CitaPanel;
import pe.edu.uni.centromedico.util.ErrorHandler;
import pe.edu.uni.centromedico.util.SesionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

public class CitaController {

    private final CitaPanel    vista;
    private final CitaService  citaService;

    public CitaController(CitaPanel vista) {
        this.vista       = vista;
        this.citaService = new CitaService();
        mostrarFecha();
        cargarDatos();
        conectarEventos();
    }

    private void conectarEventos() {
        vista.getTblCitas().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    ErrorHandler.ejecutarSeguro(vista, CitaController.this::abrirAtencion);
            }
        });
    }

    private void abrirAtencion() {
        int row = vista.getTblCitas().getSelectedRow();
        if (row < 0) return;

        Object estadoObj = vista.getTblCitas().getValueAt(row, 5);
        String estado = estadoObj != null ? estadoObj.toString() : "";
        if (!"PENDIENTE".equalsIgnoreCase(estado)) {
            ErrorHandler.mostrarInfo(vista, "Cita no disponible",
                "Solo se pueden atender citas PENDIENTES.");
            return;
        }

        Object idObj = vista.getTblCitas().getValueAt(row, 0);
        if (idObj == null) return;
        int idCita;
        try {
            idCita = Integer.parseInt(idObj.toString());
        } catch (NumberFormatException ex) {
            ErrorHandler.mostrarError(vista, "ID de cita inválido.");
            return;
        }

        Cita citaSel = citaService.obtenerCitasDoctor(SesionManager.getId()).stream()
            .filter(c -> c.getId() == idCita)
            .findFirst().orElse(null);
        if (citaSel == null) {
            ErrorHandler.mostrarError(vista, "No se encontró la cita seleccionada.");
            return;
        }

        pe.edu.uni.centromedico.ui.frames.MainFrame mf =
            pe.edu.uni.centromedico.ui.frames.MainFrame.getInstance();
        if (mf == null) return;
        pe.edu.uni.centromedico.ui.panels.AtenderCitaPanel panel =
            new pe.edu.uni.centromedico.ui.panels.AtenderCitaPanel();
        new AtenderCitaController(panel, citaSel);
        mf.mostrarPanel(panel, "Atender Cita");
    }

    private void mostrarFecha() {
        String hoy = LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM, yyyy",
                Locale.forLanguageTag("es")));
        vista.setFecha(Character.toUpperCase(hoy.charAt(0)) + hoy.substring(1));
    }

    private void cargarDatos() {
        List<Cita> citas = citaService.obtenerCitasDoctor(SesionManager.getId());
        String[][] datos = new String[citas.size()][6];
        for (int i = 0; i < citas.size(); i++) {
            Cita c = citas.get(i);
            datos[i] = new String[]{
                String.valueOf(c.getId()),
                c.getNombreEstudiante() != null ? c.getNombreEstudiante() : c.getIdEstudiante(),
                c.getDiaSemana() != null ? c.getDiaSemana() : "—",
                (c.getHoraInicio() != null ? c.getHoraInicio() : "") +
                    (c.getHoraFin() != null ? " - " + c.getHoraFin() : ""),
                c.getMotivo(),
                c.getEstado()
            };
        }
        vista.getTblCitas().setModel(new DefaultTableModel(
            datos, new String[]{"ID","Paciente","Día","Hora","Motivo","Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }
}
