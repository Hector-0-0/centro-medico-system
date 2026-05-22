package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.db.dao.DoctorDAO;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.ui.panels.AdminCitasPanel;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AdminCitasController {

    private final AdminCitasPanel vista;
    private final CitaDAO         citaDAO;
    private final DoctorDAO       doctorDAO;
    private List<Cita>            todasLasCitas;

    public AdminCitasController(AdminCitasPanel vista) {
        this.vista      = vista;
        this.citaDAO    = new CitaDAO();
        this.doctorDAO  = new DoctorDAO();
        cargarEspecialidades();
        cargarDatos();
        conectarEventos();
    }

    private void cargarEspecialidades() {
        vista.setEspecialidades(doctorDAO.obtenerEspecialidades());
    }

    private void cargarDatos() {
        todasLasCitas = citaDAO.obtenerTodas();
        filtrar();
    }

    private void filtrar() {
        String espFiltro    = (String) vista.getCmbEspecialidad().getSelectedItem();
        String estadoFiltro = (String) vista.getCmbEstado().getSelectedItem();

        List<Cita> resultado = todasLasCitas.stream()
            .filter(c -> "Todas".equals(espFiltro)
                || espFiltro == null
                || espFiltro.equals(c.getEspecialidad()))
            .filter(c -> "Todos".equals(estadoFiltro)
                || estadoFiltro == null
                || estadoFiltro.equals(c.getEstado()))
            .toList();

        cargarTabla(resultado);
    }

    private void cargarTabla(List<Cita> citas) {
        String[][] datos = new String[citas.size()][5];
        for (int i = 0; i < citas.size(); i++) {
            Cita c = citas.get(i);
            String paciente = c.getNombreEstudiante() != null
                ? c.getNombreEstudiante() : c.getIdEstudiante();
            String medico = c.getNombreDoctor() != null
                ? c.getNombreDoctor() : c.getIdDoctor();
            datos[i] = new String[]{
                String.valueOf(c.getId()),
                paciente,
                medico,
                c.getMotivo(),
                c.getEstado()
            };
        }
        vista.getTblCitasAdmin().setModel(new DefaultTableModel(
            datos, new String[]{"ID", "Paciente", "Médico", "Motivo", "Estado"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void conectarEventos() {
        vista.getBtnFiltrar().addActionListener(e -> filtrar());
        vista.getCmbEspecialidad().addActionListener(e -> filtrar());
        vista.getCmbEstado().addActionListener(e -> filtrar());
    }
}
