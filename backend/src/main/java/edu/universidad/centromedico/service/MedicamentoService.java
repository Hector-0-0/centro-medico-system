package edu.universidad.centromedico.service;

import edu.universidad.centromedico.model.Medicamento;
import edu.universidad.centromedico.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio para el inventario de medicamentos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public List<Medicamento> listarActivos() {
        return medicamentoRepository.findByActivoTrue();
    }

    public List<Medicamento> listarTodos() {
        return medicamentoRepository.findAll();
    }

    public List<Medicamento> conStockBajo() {
        return medicamentoRepository.findMedicamentosConStockBajo();
    }

    public Medicamento buscarPorId(Long id) {
        return medicamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con id: " + id));
    }

    public Medicamento registrar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public Medicamento actualizar(Long id, Medicamento datos) {
        Medicamento medicamento = buscarPorId(id);
        medicamento.setNombre(datos.getNombre());
        medicamento.setPrincipioActivo(datos.getPrincipioActivo());
        medicamento.setPresentacion(datos.getPresentacion());
        medicamento.setStockActual(datos.getStockActual());
        medicamento.setStockMinimo(datos.getStockMinimo());
        medicamento.setUnidad(datos.getUnidad());
        return medicamentoRepository.save(medicamento);
    }

    public Medicamento actualizarStock(Long id, int cantidad) {
        Medicamento medicamento = buscarPorId(id);
        int nuevoStock = medicamento.getStockActual() + cantidad;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + medicamento.getStockActual());
        }
        medicamento.setStockActual(nuevoStock);
        return medicamentoRepository.save(medicamento);
    }

    public void desactivar(Long id) {
        Medicamento medicamento = buscarPorId(id);
        medicamento.setActivo(false);
        medicamentoRepository.save(medicamento);
    }
}
