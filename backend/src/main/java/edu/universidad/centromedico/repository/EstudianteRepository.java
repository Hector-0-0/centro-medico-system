package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {

    /** Pacientes no eliminados, ordenados por nombre (igual que el desktop). */
    @Query(value = """
            SELECT e.* FROM estudiantes e
            JOIN usuarios u ON e.id_usuario = u.id
            WHERE u.eliminado = 0
            ORDER BY e.nombre
            """, nativeQuery = true)
    List<Estudiante> findAllActivos();

    /** Para impedir DNIs duplicados entre pacientes (un DNI identifica a una sola persona). */
    boolean existsByDni(String dni);
}
