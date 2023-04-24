package org.ieschabas.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.ieschabas.clases.Usuario;
import org.ieschabas.daos.UsuarioDAO;
import org.ieschabas.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {
    private final UsuarioDAO usuarioDao;

    public SecurityConfig(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }
    @Bean
    public UserDetailsService users(){
        List<Usuario> usuarios = usuarioDao.listar();
        List<UserDetails> users = new ArrayList<>();
        for(Usuario usuario: usuarios) {
            UserDetails user = User.withUsername(usuario.getEmail()).password("{noop}"+usuario.getContrasenya()).roles(usuario.getRol().toString()).build();
            users.add(user);
        }
      return new InMemoryUserDetailsManager(users);
    }
}
