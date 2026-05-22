package pe.edu.uni.centromedico.controller;

import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.ui.panels.CitaPanel;
import pe.edu.uni.centromedico.util.SesionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

public class CitaController {

    private final CitaPanel vista;
    private final CitaDAO   citaDAO;

    public CitaController(CitaPanel vista) {
        this.vista   = vista;
        this.citaDAO = new CitaDAO();
        mostrarFecha();
        cargarDatos();
        conectarEventos();
    }

    private void conectarEventos() {
        vista.getTblCitas().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = vista.getTblCitas().getSelectedRow();
                    if (row < 0) return;
                    String estado = vista.getTblCitas().getValueAt(row, 5) != null
                        ? vista.getTblCitas().getValueAt(row, 5).toString() : "";
                    if (!"PENDIENTE".equalsIgnoreCase(estado)) {
                        javax.swing.JOptionPane.showMessageDialog(vista,
                            "Solo se pueden atender citas PENDIENTES.",
                            "Cita no disponible", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    int idCita = Integer.parseInt(
                        vista.getTblCitas().getValueAt(row, 0).toString());
                    // Buscar la cita en la lista cargada
                    List<Cita> citas = citaDAO.obtenerPorDoctor(SesionManager.getId());
                    Cita citaSel = citas.stream()
                        .filter(c -> c.getId() == idCita)
                        .findFirst().orElse(null);
                    if (citaSel == null) return;
                    pe.edu.uni.centromedico.ui.frames.MainFrame mf =
                        pe.edu.uni.centromedico.ui.frames.MainFrame.getInstance();
                    if (mf == null) return;
                    pe.edu.uni.centromedico.ui.panels.AtenderCitaPanel panel =
                        new pe.edu.uni.centromedico.ui.panels.AtenderCitaPanel();
                    new pe.edu.uni.centromedico.controller.AtenderCitaController(panel, citaSel);
                    mf.mostrarPanel(panel, "Atender Cita");
                }
            }
        });
    }

    private void mostrarFecha() {
        String hoy = LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM, yyyy",
                Locale.forLanguageTag("es")));
        vista.setFecha(Character.toUpperCase(hoy.charAt(0)) + hoy.substring(1));
    }

    private void cargarDatos() {
        List<Cita> citas = citaDAO.obtenerPorDoctor(SesionManager.getId());
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
