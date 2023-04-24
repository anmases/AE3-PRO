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
       // List<Usuario> usuarios = usuarioDao.listar();
        List<UserDetails> users = new ArrayList<>();
        UserDetails user1 = User.withUsername("anmases").password("{noop}0385").roles("ADMIN").build();
        UserDetails user2 = User.withUsername("alex").password("{noop}0000").roles("USER").build();
        users.add(user1);
        users.add(user2);
        return new InMemoryUserDetailsManager(users);
    }
}
