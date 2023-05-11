package org.ieschabas.backend.daos;

import org.ieschabas.backend.clases.EntidadAbstracta;
import org.ieschabas.backend.clases.Pelicula;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class PeliculaDAO extends AbstractDAO{
    //definimos el logger:
    private final Logger LOGGER = Logger.getLogger(PeliculaDAO.class);
    private static final String CONSULTA = "SELECT e FROM Pelicula e";
    public PeliculaDAO(){super();}
    @Override
    public boolean insertar(EntidadAbstracta ea) {
        if(ea instanceof Pelicula) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Para añadir es con el método persist:
            em.persist(ea);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Añadida la película:" + ea.getId());
            close();
        }
        return true;
    }

    @Override
    public Pelicula buscar(int id) {
        Pelicula pelicula;
        setUp();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para buscar es con el método find:
        pelicula = em.find(Pelicula.class, id);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("buscado la pelicula:" + pelicula.getId());
        close();
        return pelicula;
    }

    @Override
    public boolean eliminar(int id) {
        setUp();
        //Primero se busca en la BD:
        Pelicula Pelicula = em.find(Pelicula.class, id);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para eliminar es con remove:
        //em.merge(Pelicula);
        em.remove(Pelicula);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("Eliminado la Pelicula:" + Pelicula.getId());
        close();
        return true;
    }

    @Override
    public boolean modificar(EntidadAbstracta ea) {
        if(ea instanceof Pelicula) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Busca el objeto por ID y si lo encuentra, lo modifica y si no lo encuentra, lo añade:
            em.merge(ea);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Modificado la película:" + ea.getId());
            close();
        }
        return true;
    }

    @Override
    public List<Pelicula> listar() {
        setUp();
        List<Pelicula> peliculas;
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Se listan con la consulta:
        peliculas = em.createQuery(CONSULTA , Pelicula.class).getResultList();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //Se configura el log:
        if(peliculas.isEmpty()){
            LOGGER.info("no hay datos");
        }
        else{

            for (Pelicula pelicula : peliculas) {
                LOGGER.info("Listar: " + pelicula.getId() + " -- " + pelicula.getTitulo());
            }
        }
        close();
        return peliculas;
    }
}
