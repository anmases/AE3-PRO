package org.ieschabas.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.ieschabas.backend.model.Usuario;
import org.ieschabas.backend.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Clase de servicio para manejo de credenciales y control de sesión.
 * @author Antonio Mas Esteve
 */
@Service
public class SecurityService {
    private final AuthenticationContext authenticationContext;
    private final UserService userService;
    /**
     * Constructor de la clase SecurityService
     */
    public SecurityService(AuthenticationContext authenticationContext, UserService userService) {
        this.authenticationContext = authenticationContext;
        this.userService = userService;
    }

    /**
     * Método que devuelve qué usuario está autenticado.
     * @return Usuario
     */
    public Usuario getUsuarioAutenticado() {
       UserDetails userDetails = authenticationContext.getAuthenticatedUser(UserDetails.class).get();
       Usuario usuario = userService.findByEmail(userDetails.getUsername());
       return usuario;
    }

    /**
     * Método que cierra la sesión
     */
    public void cerrarSesion() {authenticationContext.logout();}
}
