package org.ieschabas.backend.daos;

import org.ieschabas.backend.clases.EntidadAbstracta;
import org.ieschabas.backend.clases.Usuario;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UsuarioDAO extends AbstractDAO {
    //definimos el logger:
    private final Logger LOGGER = Logger.getLogger(UsuarioDAO.class);
    private static final String CONSULTA = "SELECT e FROM Usuario e";
    @Override
    public boolean insertar(EntidadAbstracta ea) {
        if(ea instanceof Usuario) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Para añadir es con el método persist:
            em.persist(ea);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Añadido el usuario:" + ea.getId());
            close();
        }
        return true;
    }

    @Override
    public Usuario buscar(int id) {
        Usuario usuario;
        setUp();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para buscar es con el método find:
        usuario = em.find(Usuario.class, id);
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("buscado el usuario:" + usuario.getId());
        close();

        return usuario;
    }

    @Override
    public boolean eliminar(int id) {
        setUp();
        //Primero se busca en la BD:
        Usuario usuario = em.find(Usuario.class, id);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Para eliminar es con remove:
        //em.merge(usuario);
        em.remove(usuario);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //lo registramos en el logger:
        LOGGER.info("Eliminado el usuario:" + usuario.getId());
        close();
        return true;
    }

    @Override
    public boolean modificar(EntidadAbstracta ea) {
        if(ea instanceof Usuario) {
            setUp();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Busca el objeto por ID y si lo encuentra, lo modifica y si no lo encuentra, lo añade:
            em.merge(ea);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //lo registramos en el logger:
            LOGGER.info("Modificado el usuario:" + ea.getId());
            close();
        }
        return true;
    }


    @Override
    public List<Usuario> listar() {
        setUp();
        List<Usuario> usuarios;
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Se listan con la consulta:
        usuarios = em.createQuery(CONSULTA , Usuario.class).getResultList();
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //Se configura el log:
        if(usuarios.isEmpty()){
            LOGGER.info("no hay datos");
        }
        else{

            for (Usuario usuario : usuarios) {
                LOGGER.info("Listar: " + usuario.getId() + " -- " + usuario.getNombre() + " -- " + usuario.getApellidos() + " -- " + usuario.getActivo());
            }
        }
        close();
        return usuarios;
    }

    /**
     * Método que busca por email
     * @author Antonio Mas Esteve
     * @return Usuario
     */
    public Usuario buscarPorMail(String email){
        List<Usuario> usuarios = listar();
        for(Usuario usuario: usuarios){
            if(usuario.getEmail().equals(email)){
                return usuario;
            }
        }
        return null;
    }

}
