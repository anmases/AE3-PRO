package org.ieschabas;

import org.ieschabas.views.usuarios.UsuarioView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTest {
    private final UsuarioView usuarioView;

    ApplicationTest(UsuarioView usuarioView) {
        this.usuarioView = usuarioView;
    }

    @Test
    void prueba() {
        assertNotNull(usuarioView);
    }
}