package pe.edu.uni.centromedico.service;

import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.db.dao.RecetaDAO;
import pe.edu.uni.centromedico.models.Receta;
import pe.edu.uni.centromedico.models.RecetaDetalle;

import java.util.List;

public class RecetaService {

    private final RecetaDAO      recetaDAO      = new RecetaDAO();
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();

    public List<Receta> obtenerRecetasPendientes() {
        return recetaDAO.obtenerPendientes();
    }

    public boolean confirmarEntrega(int idReceta) {
        if (idReceta <= 0) return false;

        List<RecetaDetalle> detalles = recetaDAO.obtenerDetallesPorReceta(idReceta);
        for (RecetaDetalle det : detalles) {
            var med = medicamentoDAO.obtenerPorId(det.getIdMedicamento());
            if (med != null && med.getStock() > 0) {
                medicamentoDAO.actualizarStock(med.getId(), med.getStock() - 1);
            }
        }

        return recetaDAO.confirmarEntrega(idReceta);
    }

    public boolean registrarReceta(int idAtencion, List<RecetaDetalle> detalles) {
        if (idAtencion <= 0) return false;
        if (detalles == null || detalles.isEmpty()) return false;

        int idReceta = recetaDAO.registrar(idAtencion);
        if (idReceta <= 0) return false;

        for (RecetaDetalle det : detalles) {
            det.setIdReceta(idReceta);
            recetaDAO.registrarDetalle(idReceta, det);
        }
        return true;
    }
}
