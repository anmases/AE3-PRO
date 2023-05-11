package org.ieschabas;

import org.ieschabas.backend.clases.Equipo;
import org.ieschabas.backend.daos.EquipoDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTest {
    @Autowired
    private EquipoDAO equipoDAO;


    @Test
    void pruebaInyeccion() {
        //Se comprueba que se inyectan las dependencias correctamente:
        assertNotNull(equipoDAO);
        //Se comprueba que funciona:
        List<Equipo> equipos = equipoDAO.listar();
        assertNotNull(equipos);

    }
}