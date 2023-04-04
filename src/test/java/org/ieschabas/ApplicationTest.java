package org.ieschabas;

import org.ieschabas.daos.EquipoDAO;
import org.ieschabas.views.usuarios.UsuarioView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTest {
    @Autowired
    private EquipoDAO equipoDAO;


    @Test
    void prueba() {
        assertNotNull(equipoDAO);
    }
}