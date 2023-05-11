package org.ieschabas.daos;

import org.ieschabas.clases.Alquiler;
import org.ieschabas.clases.EntidadAbstracta;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AlquilerDAO extends AbstractDAO {
    //definimos el logger:
    private final Logger LOGGER = Logger.getLogger(AlquilerDAO.class);
    private static final String CONSULTA = "SELECT e FROM Alquiler e";
    public AlquilerDAO(){super();}

    @Override
    public boolean insertar(EntidadAbstracta ea) {
        if(ea instanceof Alquiler) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Para añadir es con el método persist:
            em.persist(ea);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Añadido el alquiler:" + ea.getId());
            close();
        }
        return true;
    }

    @Override
    public Alquiler buscar(int id) {
        Alquiler alquiler;
        setUp();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para buscar es con el método find:
        alquiler = em.find(Alquiler.class, id);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("buscado el alquiler:" + alquiler.getId());
        close();
        return alquiler;
    }

    @Override
    public boolean eliminar(int id) {
        setUp();
        //Primero se busca en la BD:
        Alquiler alquiler = em.find(Alquiler.class, id);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para eliminar es con remove:
        //em.merge(alquiler);
        em.remove(alquiler);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("Eliminado el alquiler:" + alquiler.getId());
        close();
        return true;
    }

    @Override
    public boolean modificar(EntidadAbstracta ea) {
        if(ea instanceof Alquiler) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Busca el objeto por ID y si lo encuentra, lo modifica y si no lo encuentra, lo añade:
            em.merge(ea);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Modificado el alquiler:" + ea.getId());
            close();
        }
        return true;
    }

    @Override
    public List<Alquiler> listar() {
        setUp();
        List<Alquiler> alquileres;
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Se listan con la consulta:
        alquileres = em.createQuery(CONSULTA , Alquiler.class).getResultList();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //Se configura el log:
        if(alquileres.isEmpty()){
            LOGGER.info("no hay datos");
        }
        else{

            for (Alquiler alquiler : alquileres) {
                LOGGER.info("Listar: " + alquiler.getId() + " -- " + alquiler.getFechaAlquiler() + " -- " + alquiler.getCliente().getId() + " -- " + alquiler.getPelicula().getId());
            }
        }
        close();
        return alquileres;
    }

}
