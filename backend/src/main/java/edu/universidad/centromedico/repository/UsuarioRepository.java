package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    /** Login: busca por id (= usuario) descartando los eliminados. */
    Optional<Usuario> findByIdAndEliminadoFalse(String id);
}
