package pe.edu.uni.centromedico.service;

import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.db.dao.HorarioDAO;
import pe.edu.uni.centromedico.models.Cita;
import pe.edu.uni.centromedico.models.Horario;

import java.util.List;

public class CitaService {

    private final CitaDAO    citaDAO    = new CitaDAO();
    private final HorarioDAO horarioDAO = new HorarioDAO();

    public List<Horario> obtenerHorariosDisponibles() {
        return horarioDAO.obtenerTodos();
    }

    public boolean agendarCita(String idEstudiante, String idDoctor,
                               int idHorario, String motivo) {
        if (idEstudiante == null || idEstudiante.isBlank()) return false;
        if (idDoctor      == null || idDoctor.isBlank())    return false;
        if (motivo        == null || motivo.isBlank())      return false;
        if (idHorario <= 0)                                 return false;
        return citaDAO.crearCita(idEstudiante, idDoctor, idHorario, motivo);
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
