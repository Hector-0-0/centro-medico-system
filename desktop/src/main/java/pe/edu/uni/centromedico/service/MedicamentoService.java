package pe.edu.uni.centromedico.service;

import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.models.Medicamento;

import java.util.List;
import java.util.stream.Collectors;

public class MedicamentoService {

    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();

    public List<Medicamento> obtenerTodos() {
        return medicamentoDAO.obtenerTodos();
    }

    public boolean actualizarStock(String id, int nuevoStock) {
        if (id == null || id.isBlank()) return false;
        if (nuevoStock < 0) return false;
        return medicamentoDAO.actualizarStock(id, nuevoStock);
    }

    public List<Medicamento> buscar(String texto) {
        if (texto == null || texto.isBlank()) return obtenerTodos();
        String q = texto.trim().toLowerCase();
        return medicamentoDAO.obtenerTodos().stream()
            .filter(m -> m.getNombre().toLowerCase().contains(q)
                      || m.getId().toLowerCase().contains(q))
            .collect(Collectors.toList());
    }
}
