package org.ieschabas.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.ieschabas.clases.Usuario;
import org.ieschabas.daos.UsuarioDAO;
import org.ieschabas.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de configuración Spring que configura el entorno de seguridad y autenticación de la app.
 * @author Antonio Mas Esteve
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {
    private final UsuarioDAO usuarioDao;

    /**
     * Constructor de la clase SecurityConfig, donde se inyectan las dependencias Spring de los usuarios.
     * @param usuarioDao
     */
    public SecurityConfig(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(users());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        http.authenticationProvider(authenticationProvider);
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    /**
     * Método que carga el codificador en el sistema de seguridad de la aplicación.
     * @author Antonio Mas Esteve
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

    /**
     * Método que carga los usuarios existentes en el sistema de seguridad de la aplicación para posterior comprobación.
     * @return UserDetailsManager in Memory.
     * @author Antonio Mas Esteve
     */
    @Bean
    public UserDetailsService users(){
        List<Usuario> usuarios = usuarioDao.listar();
        List<UserDetails> users = new ArrayList<>();
        for(Usuario usuario: usuarios) {
            UserDetails user = User.withUsername(usuario.getEmail()).password(usuario.getContrasenya()).roles(usuario.getRol().toString()).build();
            users.add(user);
        }
      return new InMemoryUserDetailsManager(users);
    }
}
