package org.ieschabas.daos;

import org.ieschabas.backend.clases.Actor;
import org.ieschabas.backend.clases.Equipo;
import org.ieschabas.backend.clases.Pelicula;
import org.ieschabas.backend.daos.EquipoDAO;
import org.ieschabas.backend.daos.PeliculaDAO;
import org.ieschabas.backend.enums.Categoria;
import org.ieschabas.backend.enums.Formato;
import org.ieschabas.backend.enums.Valoracion;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la gestión de datos de las películas
 * @author Antonio Mas Esteve
 */
class PeliculaDAOTest {
    private static final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private static final EquipoDAO equipoDAO = new EquipoDAO();

    @Test
    @Disabled
    void insertar() {
        //Introducir una nueva película:
        String titulo = "Lo que el viento se llevó";
        boolean check;
        Pelicula pelicula = new Pelicula(0, "Lo que el viento se llevó", "jksdnklcnkc", 1978, "200", Categoria.COMEDIA, Formato.FLV, Valoracion.CINCO);
        //Comprueba que se ha hecho:
        check = peliculaDAO.insertar(pelicula);
        //Busca la misma película y la compara para ver si se ha guardado correctamente:
        Pelicula pelicula1 = peliculaDAO.buscar(8);
        assertTrue(check);
        assertEquals(pelicula, pelicula1);
        //Comprueba si las cadenas de caracteres se recuperan en el mismo formato de texto:
        assertEquals(titulo, pelicula1.getTitulo());
    }

    @Test
    @Disabled
    void buscar() {Pelicula pelicula;
        String primeraPeli = "El Padrino";
        pelicula = peliculaDAO.buscar(1);
        assertNotNull(pelicula);
        assertEquals(1, pelicula.getId());
        assertEquals(primeraPeli, pelicula.getTitulo());
    }

    @Test
    @Disabled
    void eliminar() {
        boolean check;
        check = peliculaDAO.eliminar(8);
        assertTrue(check);
        //Comprobamos que realmente se ha eliminado:
        assertThrows(IllegalArgumentException.class, ()->peliculaDAO.eliminar(8));
    }

    @Test
    @Disabled
    void modificar() {
        //Se a�ade una pel�cula igual con otro título:
        boolean check;
        Pelicula pelicula = new Pelicula(8, "Terminator", "jksdnklcnkc", 1978, "200", Categoria.COMEDIA, Formato.FLV, Valoracion.CINCO);
        check = peliculaDAO.modificar(pelicula);
        //Se comprueba que se ha hecho:
        assertTrue(check);
        Pelicula pelicula1 = peliculaDAO.buscar(8);
        assertEquals(pelicula, pelicula1);
    }

    @Test
    @Disabled
    void listar() {
        List<Pelicula> peliculas;
        peliculas= peliculaDAO.listar();
        assertNotNull(peliculas);
    }
    @Test
    @Disabled
    void relacionarActor(){
        boolean check;
        //Buscamos una película y un actor
        Pelicula pelicula = peliculaDAO.buscar(1);
        Equipo equipo = equipoDAO.buscar(10);
        List<Equipo> actores = new ArrayList<>();
        actores.add(equipo);
        pelicula.setEquipos(actores);
        check = peliculaDAO.modificar(pelicula);
        assertTrue(check);
    }
    @Test
    @Disabled
    void listarActores(){
        Pelicula pelicula = peliculaDAO.buscar(1);
       assertNotNull(pelicula.getActores());
    }
    @Test
    @Disabled
    void eliminarActor(){
        //Se busca la relación:
        Pelicula pelicula = peliculaDAO.buscar(1);
        Actor actor = pelicula.getActores().get(0);
        //Se elimina del objeto en memoria:
        assertTrue(pelicula.eliminarRelacion(actor));
        //Se persiste en la bd:
        assertTrue(peliculaDAO.modificar(pelicula));
    }
}