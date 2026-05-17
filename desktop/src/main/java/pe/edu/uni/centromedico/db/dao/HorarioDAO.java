package pe.edu.uni.centromedico.db.dao;

import pe.edu.uni.centromedico.db.DatabaseManager;
import pe.edu.uni.centromedico.models.Doctor;
import pe.edu.uni.centromedico.models.Horario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {

    // Trae todos los horarios con datos del doctor incluidos (para la tabla)
    public List<Horario> obtenerTodos() {
        List<Horario> lista = new ArrayList<>();
        String sql = """
                SELECT h.id, h.id_doctor, h.dia_semana, h.hora_inicio, h.hora_fin,
                       h.disponible, d.nombre AS nombre_doctor,
                       d.especialidad, d.consultorio
                FROM horarios_doctor h
                JOIN doctores d ON h.id_doctor = d.id_usuario
                ORDER BY d.especialidad, h.dia_semana, h.hora_inicio
                """;

        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Horario h = new Horario();
                h.setId(rs.getInt("id"));
                h.setIdDoctor(rs.getString("id_doctor"));
                h.setDiaSemana(rs.getString("dia_semana"));
                h.setHoraInicio(rs.getString("hora_inicio"));
                h.setHoraFin(rs.getString("hora_fin"));
                h.setDisponible(rs.getInt("disponible") == 1);
                // Datos extra del doctor que necesita la tabla
                h.setNombreDoctor(rs.getString("nombre_doctor"));
                h.setEspecialidad(rs.getString("especialidad"));
                h.setConsultorio(rs.getString("consultorio"));
                lista.add(h);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener horarios: " + e.getMessage());
        }
        return lista;
    }
}