package org.ieschabas.daos;

import org.ieschabas.backend.model.Cliente;
import org.ieschabas.backend.model.Usuario;
import org.ieschabas.backend.daos.UsuarioDAO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la gestión de datos de los usuarios.
 * @author Antonio Mas Esteve
 */
class UsuarioDAOTest {
private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    @Test
    @Disabled
    void insertarTest() {
        boolean check;
        Usuario usuario = new Cliente(0,"Manuel", "Castro","manuel.castro@ieschabas.org", "3333", "Plaza Oeste", true, LocalDate.now());
        check = usuarioDAO.insertar(usuario);
        Usuario usuario1 = usuarioDAO.buscar(4);
        assertTrue(check);
        assertEquals(usuario, usuario1);
    }

    @Test
    @Disabled
    void buscarTest() {
        Usuario usuario;
        usuario = usuarioDAO.buscar(1);
        assertNotNull(usuario);
        assertEquals(1, usuario.getId());
        assertEquals("Antonio", usuario.getNombre());
    }

    @Test
    @Disabled
    void eliminarTest() {
        boolean check;
        check = usuarioDAO.eliminar(4);
        assertTrue(check);
        //Comprobamos que realmente se ha eliminado:
        assertThrows(IllegalArgumentException.class, ()->usuarioDAO.eliminar(13));
    }

    @Test
    @Disabled
    void modificarTest() {
        boolean check;
        Usuario usuario = new Cliente(2, "Alex", "Palacios Requena", "alejandro.palacios@ieschabas.org", "0000", "Calle Loreto", false, LocalDate.now());
        check = usuarioDAO.modificar(usuario);
        assertTrue(check);
        Usuario usuario1 = usuarioDAO.buscar(2);
        assertEquals(usuario, usuario1);
    }

    @Test
    @Disabled
    void listarTest() {
        List<Usuario> usuarios;
        usuarios= usuarioDAO.listar();
        assertNotNull(usuarios);
    }
}