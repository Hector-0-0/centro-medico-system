package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.ui.panels.HistorialPanel;
import pe.edu.uni.centromedico.util.SesionManager;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HistorialController {

    private final HistorialPanel vista;
    private final CitaDAO        citaDAO;

    public HistorialController(HistorialPanel vista) {
        this.vista   = vista;
        this.citaDAO = new CitaDAO();
        cargarDatos();
        conectarEventos();
    }

    private void cargarDatos() {
        cargarTabla(citaDAO.obtenerPorEstudiante(SesionManager.getId()));
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
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getTxtBuscar().addActionListener(e -> buscar());
    }

    private void buscar() {
        String texto = vista.getTxtBuscar().getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarDatos(); return; }
        List<Cita> filtradas = citaDAO.obtenerPorEstudiante(SesionManager.getId())
            .stream()
            .filter(c -> (c.getEspecialidad() != null && c.getEspecialidad().toLowerCase().contains(texto))
                      || (c.getNombreDoctor()  != null && c.getNombreDoctor().toLowerCase().contains(texto))
                      || c.getMotivo().toLowerCase().contains(texto)
                      || c.getEstado().toLowerCase().contains(texto))
            .toList();
        cargarTabla(filtradas);
    }
}
