package org.ieschabas.daos;

import org.ieschabas.backend.clases.Alquiler;
import org.ieschabas.backend.clases.Cliente;
import org.ieschabas.backend.clases.Pelicula;
import org.ieschabas.backend.daos.AlquilerDAO;
import org.ieschabas.backend.daos.PeliculaDAO;
import org.ieschabas.backend.daos.UsuarioDAO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la gestión de datos de los alquileres.
 * @author Antonio Mas Esteve
 */
class AlquilerDAOTest {
private static final AlquilerDAO alquilerDAO = new AlquilerDAO();
private static final UsuarioDAO usuarioDao = new UsuarioDAO();
private static final PeliculaDAO peliculaDao = new PeliculaDAO();
    @Test
    @Disabled
    void buscarTest() {
        Alquiler alquiler;
        alquiler = alquilerDAO.buscar(1);
        assertNotNull(alquiler);
        assertEquals(1, alquiler.getId());
    }
    @Test
    @Disabled
    void listarTest() {
        List<Alquiler> alquileres;
        alquileres= alquilerDAO.listar();
        assertNotNull(alquileres);
    }
    @Test
    @Disabled
    void insertarTest() {
        boolean check;
        //Debemos buscar un cliente y una película:
        Cliente cliente = (Cliente) usuarioDao.buscar(3);
        Pelicula pelicula =peliculaDao.buscar(1);
        Alquiler alquiler = new Alquiler(0, LocalDate.now(), cliente, pelicula, LocalDate.now());
        check = alquilerDAO.insertar(alquiler);
        Alquiler alquiler2 = alquilerDAO.buscar(2);
        assertTrue(check);
        assertEquals(alquiler, alquiler2);
    }
    @Test
    @Disabled
    void modificarTest() {
        boolean check;
        //Debemos buscar un cliente y una película:
        Cliente cliente = (Cliente) usuarioDao.buscar(3);
        Pelicula pelicula =peliculaDao.buscar(4);
        Alquiler alquiler = new Alquiler(2, LocalDate.now().plusDays(5), cliente, pelicula, LocalDate.now().plusDays(8));
        check = alquilerDAO.modificar(alquiler);
        assertTrue(check);
        Alquiler alquiler2 = alquilerDAO.buscar(2);
        assertEquals(alquiler, alquiler2);
    }
    @Test
    @Disabled
    void eliminarTest() {
        boolean check;
        check = alquilerDAO.eliminar(2);
        assertTrue(check);
    }
}