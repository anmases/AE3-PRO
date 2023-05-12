package org.ieschabas.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.ieschabas.backend.clases.Usuario;
import org.ieschabas.backend.daos.UsuarioDAO;
import org.ieschabas.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración Spring que configura el entorno de seguridad y autenticación de la app.
 * @author Antonio Mas Esteve
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {
    private final UsuarioDAO usuarioDao;
    private final PersonalUserDetailsService personalUserDetailsService;

    /**
     * Constructor de la clase SecurityConfig, donde se inyectan las dependencias Spring de los usuarios.
     */
    public SecurityConfig(UsuarioDAO usuarioDao, PersonalUserDetailsService personalUserDetailsService) {
        this.usuarioDao = usuarioDao;
        this.personalUserDetailsService = personalUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(personalUserDetailsService);
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

}
