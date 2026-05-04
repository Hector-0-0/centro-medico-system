package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByCmp(String cmp);
    boolean existsByCmp(String cmp);
    List<Medico> findByEspecialidadId(Long especialidadId);
    List<Medico> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
