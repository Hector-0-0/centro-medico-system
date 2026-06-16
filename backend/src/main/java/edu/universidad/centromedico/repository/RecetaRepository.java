package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.EstadoReceta;
import edu.universidad.centromedico.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByEstadoOrderByFechaDesc(EstadoReceta estado);
    List<Receta> findByPacienteIdOrderByFechaDesc(Long pacienteId);
    List<Receta> findByMedicoIdOrderByFechaDesc(Long medicoId);
    List<Receta> findAllByOrderByFechaDesc();
}
