package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {

    // Para DashboardPanel — todos los slots activos con datos del doctor
    public List<Slot> obtenerTodos() {
        List<Slot> lista = new ArrayList<>();
        String sql = """
                SELECT s.id, s.id_disponibilidad, s.id_doctor,
                       s.dia_semana, s.hora_inicio, s.hora_fin, s.disponible,
                       d.nombre       AS nombre_doctor,
                       d.consultorio, e.nombre AS especialidad
                FROM slots_disponibilidad s
                JOIN doctores d ON s.id_doctor = d.id_usuario
                LEFT JOIN especialidades e ON d.especialidad_id = e.id
                WHERE s.eliminado = 0
                ORDER BY e.nombre, s.dia_semana, s.hora_inicio
                """;
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener slots: " + e.getMessage());
        }
        return lista;
    }

    // Para NuevaCitaDialog — solo slots disponibles
    public List<Slot> obtenerDisponibles() {
        List<Slot> lista = new ArrayList<>();
        String sql = """
                SELECT s.id, s.id_disponibilidad, s.id_doctor,
                       s.dia_semana, s.hora_inicio, s.hora_fin, s.disponible,
                       d.nombre       AS nombre_doctor,
                       d.consultorio, e.nombre AS especialidad
                FROM slots_disponibilidad s
                JOIN doctores d ON s.id_doctor = d.id_usuario
                LEFT JOIN especialidades e ON d.especialidad_id = e.id
                WHERE s.disponible = 1 AND s.eliminado = 0
                ORDER BY e.nombre, s.dia_semana, s.hora_inicio
                """;
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener slots disponibles: " + e.getMessage());
        }
        return lista;
    }

    // Reutilizable — mapea un ResultSet a un Slot
    private Slot mapear(ResultSet rs) throws SQLException {
        Slot s = new Slot();
        s.setId(rs.getInt("id"));
        s.setIdDisponibilidad(rs.getInt("id_disponibilidad"));
        s.setIdDoctor(rs.getString("id_doctor"));
        s.setDiaSemana(rs.getString("dia_semana"));
        s.setHoraInicio(rs.getString("hora_inicio"));
        s.setHoraFin(rs.getString("hora_fin"));
        s.setDisponible(rs.getInt("disponible") == 1);
        s.setNombreDoctor(rs.getString("nombre_doctor"));
        s.setEspecialidad(rs.getString("especialidad"));
        s.setConsultorio(rs.getString("consultorio"));
        return s;
    }
}
