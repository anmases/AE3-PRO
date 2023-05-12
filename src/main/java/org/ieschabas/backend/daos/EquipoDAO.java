package org.ieschabas.backend.daos;

import org.ieschabas.backend.clases.Actor;
import org.ieschabas.backend.clases.Director;
import org.ieschabas.backend.clases.Equipo;
import org.ieschabas.backend.clases.EntidadAbstracta;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos de Equipo
 * @author Antonio Mas Esteve
 */
@Repository
public class EquipoDAO extends AbstractDAO {
    //definimos el logger:
    private final Logger LOGGER = Logger.getLogger(UsuarioDAO.class);
    private static final String CONSULTA = "SELECT e FROM Equipo e";

    /**
     * Constructor vacío
     */
    public EquipoDAO() {super();}

    @Override
    public boolean insertar(EntidadAbstracta ea) {
        if (ea instanceof Equipo) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Para añadir es con el método persist:
            em.persist(ea);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Añadido el miembro del equipo:" + ea.getId());
            close();
        }
        return true;
    }

    @Override
    public Equipo buscar(int id) {
        Equipo equipo;
        setUp();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para buscar es con el método find:
        equipo = em.find(Equipo.class, id);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("buscado el miembro equipo:" + equipo.getId());
        close();
        return equipo;
    }

    @Override
    public boolean eliminar(int id) {
        setUp();
        //Primero se busca en la BD:
        Equipo equipo = em.find(Equipo.class, id);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para eliminar es con remove:
        //em.merge(equipo);
        em.remove(equipo);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("Eliminado el miembro equipo:" + equipo.getId());
        close();
        return true;
    }

    @Override
    public boolean modificar(EntidadAbstracta ea) {
        if (ea instanceof Equipo) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Busca el objeto por ID y si lo encuentra, lo modifica y si no lo encuentra, lo añade:
            em.merge(ea);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Modificado el miembro del equipo:" + ea.getId());
            close();
        }
        return true;
    }

    @Override
    public List<Equipo> listar() {
        setUp();
        List<Equipo> equipos;
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Se listan con la consulta:
        equipos = em.createQuery(CONSULTA, Equipo.class).getResultList();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //Se configura el log:
        if (equipos.isEmpty()) {
            LOGGER.info("no hay datos");
        } else {

            for (Equipo equipo : equipos) {
                LOGGER.info("Listar: " + equipo.getId() + " -- " + equipo.getNombre() + " -- " + equipo.getApellidos() + " -- " + equipo.getAnyoNacimiento() + " -- " + equipo.getPuesto());
            }
        }
        close();
        return equipos;
    }
    ////////////////////////////Métodos para instancias hijas////////////////////////////////////////////

    /**
     * Devuelve la lista de actores
     *
     * @return List
     */
    public List<Actor> listarActores() {
        List<Actor> actores = new ArrayList<>();
        for (Equipo equipo : listar()) {
            if (equipo instanceof Actor) {
                actores.add((Actor) equipo);
            }
        }
        return actores;
    }

    /**
     * Devuelve la lista de directores
     *
     * @return List
     */
    public List<Director> listarDirectores() {
        List<Director> directores = new ArrayList<>();
        for (Equipo equipo : listar()) {
            if (equipo instanceof Director) {
                directores.add((Director) equipo);
            }
        }
        return directores;
    }
}
