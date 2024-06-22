package org.ieschabas;

import org.ieschabas.backend.model.Equipo;
import org.ieschabas.backend.services.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test bajo el contexto de la aplicación SpringBoot
 * @author Antonio Mas Esteve
 */
@SpringBootTest
class ApplicationTest {
    @Autowired
    private TeamService equipoDAO;

    /**
     * Prueba de inyección de dependencias SpringBoot
     */
    @Test
    void pruebaInyeccion() {
        //Se comprueba que se inyectan las dependencias correctamente:
        assertNotNull(equipoDAO);
        //Se comprueba que funciona:
        List<Equipo> equipos = equipoDAO.findAll();
        assertNotNull(equipos);

    }
}