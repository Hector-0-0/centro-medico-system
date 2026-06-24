package edu.universidad.centromedico.service;

import edu.universidad.centromedico.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación de UserDetailsService para Spring Security.
 * El "username" es el id del usuario (ej. ADM001). La contraseña va en texto
 * plano (igual que el desktop) y solo se usa para construir el UserDetails;
 * el filtro JWT no la valida de nuevo.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return usuarioRepository.findByIdAndEliminadoFalse(id)
            .map(u -> User.withUsername(u.getId())
                .password("{noop}" + u.getPassword())
                .roles(u.getRol().name())
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + id));
    }
}
