package edu.universidad.centromedico.service;

import edu.universidad.centromedico.dto.CieDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/** Búsqueda en el catálogo CIE-10 (replica CodigoCieDAO.buscarPorTexto). */
@Service
@RequiredArgsConstructor
public class CieService {

    private final JdbcTemplate jdbc;

    public List<CieDTO> buscar(String texto) {
        String sql = """
            SELECT id, codigo, descripcion FROM codigos_cie
            WHERE codigo LIKE ? OR descripcion LIKE ?
            ORDER BY codigo
            """;
        String patron = "%" + texto + "%";
        return jdbc.query(sql,
            (rs, n) -> new CieDTO(rs.getInt("id"), rs.getString("codigo"), rs.getString("descripcion")),
            patron, patron);
    }
}
