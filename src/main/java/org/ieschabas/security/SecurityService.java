package org.ieschabas.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.ieschabas.clases.Usuario;
import org.ieschabas.daos.UsuarioDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final AuthenticationContext authenticationContext;
    private final UsuarioDAO usuarioDao;

    public SecurityService(AuthenticationContext authenticationContext, UsuarioDAO usuarioDao) {
        this.authenticationContext = authenticationContext;
        this.usuarioDao = usuarioDao;
    }
    public Usuario getUsuarioAutenticado() {
       UserDetails userDetails = authenticationContext.getAuthenticatedUser(UserDetails.class).get();
       Usuario usuario = usuarioDao.buscarPorMail(userDetails.getUsername());
       return usuario;
    }
    public void cerrarSesion() {
        authenticationContext.logout();
    }
}
