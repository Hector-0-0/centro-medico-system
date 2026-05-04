package edu.universidad.centromedico.repository;

import edu.universidad.centromedico.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    List<Medicamento> findByActivoTrue();
    List<Medicamento> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT m FROM Medicamento m WHERE m.stockActual <= m.stockMinimo AND m.activo = true")
    List<Medicamento> findMedicamentosConStockBajo();
}
