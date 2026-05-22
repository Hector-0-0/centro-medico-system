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

    public List<Horario> obtenerPorDoctor(String idDoctor) {
        List<Horario> lista = new ArrayList<>();
        String sql = """
                SELECT h.id, h.id_doctor, h.dia_semana, h.hora_inicio, h.hora_fin,
                       h.disponible
                FROM horarios_doctor h
                WHERE h.id_doctor = ?
                ORDER BY h.dia_semana, h.hora_inicio
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idDoctor);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Horario h = new Horario();
                h.setId(rs.getInt("id"));
                h.setIdDoctor(rs.getString("id_doctor"));
                h.setDiaSemana(rs.getString("dia_semana"));
                h.setHoraInicio(rs.getString("hora_inicio"));
                h.setHoraFin(rs.getString("hora_fin"));
                h.setDisponible(rs.getInt("disponible") == 1);
                lista.add(h);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener horarios del doctor: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardar(Horario horario) {
        String sql = """
                INSERT INTO horarios_doctor
                    (id_doctor, dia_semana, hora_inicio, hora_fin, disponible)
                VALUES (?, ?, ?, ?, 1)
                ON DUPLICATE KEY UPDATE
                    hora_inicio = VALUES(hora_inicio),
                    hora_fin    = VALUES(hora_fin),
                    disponible  = 1
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, horario.getIdDoctor());
            stmt.setString(2, horario.getDiaSemana());
            stmt.setString(3, horario.getHoraInicio());
            stmt.setString(4, horario.getHoraFin());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al guardar horario: " + e.getMessage());
            return false;
        }
    }

    // funcion de prueba
    public boolean eliminarHorariosSinCitas(String idDoctor) {

        // Primero mostrar cuáles NO se pueden eliminar
        String sqlConCitas = """
                SELECT id, dia_semana, hora_inicio, hora_fin
                FROM horarios_doctor
                WHERE id_doctor = ?
                  AND disponible = 0
                """;

        // Luego eliminar los que sí se pueden eliminar
        String sqlEliminar = """
                DELETE FROM horarios_doctor
                WHERE id_doctor = ?
                AND disponible = 1
                """;

        try (Connection conn = DatabaseManager.getConnection()) {

            // Ver horarios con citas asociadas
            try (PreparedStatement stmt = conn.prepareStatement(sqlConCitas)) {

                stmt.setString(1, idDoctor);

                ResultSet rs = stmt.executeQuery();

                boolean hayBloqueados = false;

                while (rs.next()) {

                    hayBloqueados = true;

                    System.out.println(
                            "No se pudo eliminar el horario ID "
                                    + rs.getInt("id")
                                    + " | "
                                    + rs.getString("dia_semana")
                                    + " "
                                    + rs.getString("hora_inicio")
                                    + " - "
                                    + rs.getString("hora_fin")
                                    + " porque tiene una cita agendada.");
                }

                if (!hayBloqueados) {
                    System.out.println("No existen horarios con citas agendadas.");
                }
            }

            // Eliminar horarios disponibles
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminar)) {

                stmt.setString(1, idDoctor);

                int eliminados = stmt.executeUpdate();

                System.out.println("Horarios eliminados correctamente: " + eliminados);
            }

            return true;

        } catch (SQLException e) {

            System.err.println("Error al eliminar horarios: " + e.getMessage());

            return false;
        }
    }
}