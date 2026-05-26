package pe.edu.uni.centromedico.service;

import pe.edu.uni.centromedico.db.dao.RecetaDAO;
import pe.edu.uni.centromedico.models.Receta;
import pe.edu.uni.centromedico.models.RecetaDetalle;

import java.util.List;

public class RecetaService {

    private final RecetaDAO recetaDAO = new RecetaDAO();

    public List<Receta> obtenerRecetasPendientes() {
        return recetaDAO.obtenerPendientes();
    }

    public boolean confirmarEntrega(int idReceta) {
        if (idReceta <= 0) return false;
        return recetaDAO.entregarYDescontarStock(idReceta);
    }

    public boolean registrarReceta(int idAtencion, List<RecetaDetalle> detalles) {
        if (idAtencion <= 0) return false;
        if (detalles == null || detalles.isEmpty()) return false;
        return recetaDAO.registrarConDetalles(idAtencion, detalles);
    }
}
