package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.MedicamentoDTO;
import edu.universidad.centromedico.dto.MovimientoStockDTO;
import edu.universidad.centromedico.dto.NuevoMedicamentoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/** Consulta de medicamentos (inventario de farmacia) + auditoría de stock. */
@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final JdbcTemplate jdbc;

    /** Tipos válidos de presentación (en vez de texto libre). */
    public static final Set<String> TIPOS = Set.of(
        "tabletas", "capsulas", "comprimidos", "jarabe", "sobre", "inyectable", "crema", "gotas");

    private static final RowMapper<MedicamentoDTO> MAPPER = (rs, n) -> {
        int dosis = rs.getInt("dosis_mg");
        return new MedicamentoDTO(
            rs.getString("id"), rs.getString("nombre"), rs.getInt("stock"),
            rs.getString("tipo"), rs.wasNull() ? null : dosis);
    };

    private static final RowMapper<MovimientoStockDTO> MOV_MAPPER = (rs, n) -> new MovimientoStockDTO(
        rs.getInt("id"), rs.getString("id_medicamento"), rs.getString("tipo_movimiento"),
        rs.getInt("cantidad"), rs.getInt("stock_resultante"), rs.getString("motivo"),
        rs.getString("usuario"), String.valueOf(rs.getTimestamp("fecha")));

    /** Todos los medicamentos, ordenados por nombre. */
    public List<MedicamentoDTO> listar() {
        return jdbc.query("SELECT id, nombre, stock, tipo, dosis_mg FROM medicamentos ORDER BY nombre", MAPPER);
    }

    /** Solo los que tienen stock disponible (para recetar). */
    public List<MedicamentoDTO> conStock() {
        return jdbc.query("SELECT id, nombre, stock, tipo, dosis_mg FROM medicamentos WHERE stock > 0 ORDER BY nombre", MAPPER);
    }

    /** Movimientos de stock de un medicamento (auditoría), del más reciente al más antiguo. */
    public List<MovimientoStockDTO> movimientos(String idMedicamento) {
        return jdbc.query(
            "SELECT id, id_medicamento, tipo_movimiento, cantidad, stock_resultante, motivo, usuario, fecha "
            + "FROM movimientos_stock WHERE id_medicamento = ? ORDER BY fecha DESC, id DESC",
            MOV_MAPPER, idMedicamento);
    }

    /** Crea un medicamento (Gestión de Stock). Registra la entrada inicial en la auditoría. */
    @Transactional
    public void crear(NuevoMedicamentoRequest req, String usuario) {
        if (!TIPOS.contains(req.getTipo().toLowerCase())) {
            throw new RuntimeException("Tipo no válido. Usa uno de: " + String.join(", ", TIPOS));
        }
        Integer existe = jdbc.queryForObject(
            "SELECT COUNT(*) FROM medicamentos WHERE id = ?", Integer.class, req.getId());
        if (existe != null && existe > 0) {
            throw new RuntimeException("El código ya existe");
        }
        jdbc.update("INSERT INTO medicamentos (id, nombre, stock, tipo, dosis_mg) VALUES (?, ?, ?, ?, ?)",
            req.getId(), req.getNombre(), req.getStock(), req.getTipo().toLowerCase(), req.getDosisMg());

        if (req.getStock() > 0) {
            registrarMovimiento(req.getId(), "ENTRADA", req.getStock(), req.getStock(),
                "Alta de medicamento", usuario);
        }
    }

    /**
     * Ajusta el stock manualmente (reabastecimiento). Solo permite AUMENTAR:
     * las salidas de stock se descuentan automáticamente al entregar recetas,
     * evitando bajadas manuales drásticas sin justificación (auditoría).
     */
    @Transactional
    public void actualizarStock(String id, int nuevoStock, String usuario) {
        if (nuevoStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        List<Integer> actuales = jdbc.queryForList(
            "SELECT stock FROM medicamentos WHERE id = ?", Integer.class, id);
        if (actuales.isEmpty()) {
            throw new RuntimeException("El medicamento no existe");
        }
        int actual = actuales.get(0);
        if (nuevoStock < actual) {
            throw new RuntimeException(
                "El stock solo puede aumentarse manualmente (reabastecimiento). "
                + "Las salidas se descuentan automáticamente al entregar recetas.");
        }
        if (nuevoStock == actual) {
            return; // sin cambios, no se registra movimiento
        }
        jdbc.update("UPDATE medicamentos SET stock = ? WHERE id = ?", nuevoStock, id);
        registrarMovimiento(id, "ENTRADA", nuevoStock - actual, nuevoStock,
            "Reabastecimiento manual", usuario);
    }

    /** Inserta una fila en el log de auditoría de stock. */
    public void registrarMovimiento(String idMedicamento, String tipo, int cantidad,
                                    int stockResultante, String motivo, String usuario) {
        jdbc.update(
            "INSERT INTO movimientos_stock "
            + "(id_medicamento, tipo_movimiento, cantidad, stock_resultante, motivo, usuario) "
            + "VALUES (?, ?, ?, ?, ?, ?)",
            idMedicamento, tipo, cantidad, stockResultante, motivo, usuario);
    }
}
