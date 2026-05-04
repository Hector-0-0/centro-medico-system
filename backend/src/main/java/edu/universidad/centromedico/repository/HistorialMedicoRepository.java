package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Long> {
    Optional<HistorialMedico> findByCitaId(Long citaId);
    List<HistorialMedico> findByCitaPacienteId(Long pacienteId);
    List<HistorialMedico> findByCitaMedicoId(Long medicoId);
}
