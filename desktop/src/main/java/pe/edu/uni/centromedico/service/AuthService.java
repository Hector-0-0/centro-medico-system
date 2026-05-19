package pe.edu.uni.centromedico.service;

import pe.edu.uni.centromedico.db.dao.UsuarioDAO;
import pe.edu.uni.centromedico.models.Persona;

public class AuthService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Persona autenticar(String id, String password) {
        if (id == null || id.isBlank()) return null;
        if (password == null || password.isBlank()) return null;
        return usuarioDAO.login(id.trim(), password.trim());
    }
}
