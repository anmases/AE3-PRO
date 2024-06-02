package org.ieschabas.security;

import org.ieschabas.backend.model.Usuario;
import org.ieschabas.backend.daos.UsuarioDAO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio que carga los usuarios cuando estos son requeridos.
 * @author Antonio Mas Esteve
 */
@Service
public class PersonalUserDetailsService implements UserDetailsService {
    private final UsuarioDAO usuarioDao;

    /**
     * Constructor de la clase PersonalUserDetailsService
     */
    public PersonalUserDetailsService(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    /**
     * Método que carga un usuario en un objeto UserDetails, usado para la autenticación.
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.buscarPorMail(email);
        if (usuario == null || !usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return User.withUsername(usuario.getEmail())
                .password(usuario.getContrasenya())
                .roles(usuario.getRol().toString())
                .build();
    }
}
