package pe.edu.uni.centromedico.service;

import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.db.dao.SlotDAO;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.models.Slot;

import java.util.List;

public class CitaService {

    private final CitaDAO citaDAO = new CitaDAO();
    private final SlotDAO slotDAO = new SlotDAO();

    public List<Slot> obtenerTodosLosSlots() {
        return slotDAO.obtenerTodos();
    }

    public boolean agendarCita(String idEstudiante, String idDoctor,
                               int idSlot, String motivo) {
        if (idEstudiante == null || idEstudiante.isBlank()) return false;
        if (idDoctor     == null || idDoctor.isBlank())     return false;
        if (motivo       == null || motivo.isBlank())       return false;
        if (idSlot <= 0)                                    return false;
        return citaDAO.crearCita(idEstudiante, idDoctor, idSlot, motivo);
    }

    public List<Cita> obtenerHistorialPaciente(String idPaciente) {
        return citaDAO.obtenerPorEstudiante(idPaciente);
    }

    public List<Cita> obtenerCitasDoctor(String idDoctor) {
        return citaDAO.obtenerPorDoctor(idDoctor);
    }

    public List<Cita> obtenerTodas() {
        return citaDAO.obtenerTodas();
    }
}
