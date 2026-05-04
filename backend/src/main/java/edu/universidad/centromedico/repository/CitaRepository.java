package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.Cita;
import edu.universidad.centromedico.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByMedicoId(Long medicoId);
    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByMedicoIdAndFechaHoraBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT c FROM Cita c WHERE CAST(c.fechaHora AS date) = CURRENT_DATE ORDER BY c.fechaHora ASC")
    List<Cita> findCitasDeHoy();
}
