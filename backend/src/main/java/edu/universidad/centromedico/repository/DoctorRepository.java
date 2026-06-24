package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    /** Médicos no eliminados, ordenados por especialidad y nombre (igual que el desktop). */
    @Query(value = """
            SELECT d.* FROM doctores d
            JOIN usuarios u ON d.id_usuario = u.id
            WHERE u.eliminado = 0
            ORDER BY d.especialidad, d.nombre
            """, nativeQuery = true)
    List<Doctor> findAllActivos();
}
