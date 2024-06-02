package org.ieschabas.daos;

import org.ieschabas.backend.model.Director;
import org.ieschabas.backend.model.Equipo;
import org.ieschabas.backend.daos.EquipoDAO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la gestión de datos del Equipo.
 * @author Antonio Mas Esteve
 */
class EquipoDAOTest {
    private static EquipoDAO equipoDAO = new EquipoDAO();

    @Test
    @Disabled
    void insertar() {
        boolean check;
        Equipo director = new Director(0, "vsdvvdz", "fdvszv", 1959, "España");
        check = equipoDAO.insertar(director);
        Equipo director1 = equipoDAO.buscar(26);
        assertTrue(check);
        assertEquals(director, director1);

    }

    @Test
    @Disabled
    void buscar() {
        Equipo equipo;
        equipo = equipoDAO.buscar(1);
        assertNotNull(equipo);
        assertEquals(1, equipo.getId());
    }

    @Test
    @Disabled
    void eliminar() {
        boolean check;
        check = equipoDAO.eliminar(25);
        assertTrue(check);
        //Comprobamos que realmente se ha eliminado:
        assertThrows(IllegalArgumentException.class, ()->equipoDAO.eliminar(25));
    }

    @Test
    @Disabled
    void modificar() {
        boolean check;
        Equipo director = new Director(26, "vsdv", "fdvszv", 1959, "España");
        check = equipoDAO.modificar(director);
        assertTrue(check);
        Equipo director1 = equipoDAO.buscar(26);
        assertEquals(director, director1);

    }

    @Test
    @Disabled
    void listar() {
        List<Equipo> equipos;
        equipos= equipoDAO.listar();
        assertNotNull(equipos);
    }
}