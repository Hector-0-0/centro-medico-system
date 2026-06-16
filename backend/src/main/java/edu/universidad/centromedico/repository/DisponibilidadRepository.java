package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    List<Disponibilidad> findByActivoTrue();
    List<Disponibilidad> findByMedicoIdAndActivoTrue(Long medicoId);
}
