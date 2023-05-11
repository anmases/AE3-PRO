package org.ieschabas.backend.daos;

import org.apache.log4j.PropertyConfigurator;
import org.ieschabas.backend.clases.EntidadAbstracta;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Data Access Object abstracto
 * @author Antonio Mas Esteve
 */
public abstract class AbstractDAO{
    //Definimos el EntityManager:
    protected EntityManager em;
    //Definimos la sentencia de búsqueda:
    protected EntityManagerFactory emf;

    /**
     * Constructor principal de la clase.
     * @author Antonio Mas Esteve
     */
    public AbstractDAO(){}

    /**
     * Método que inicia la conexión con la BD y la transacción.
     * @author Antonio Mas Esteve
     */
    protected void setUp(){
        //Abrimos la conexión con la base de datos:
        emf = Persistence.createEntityManagerFactory("videoclub_pro");
        em = emf.createEntityManager();
        //Compienza la transacción.
        em.getTransaction().begin();
        //indicamos al logger el fichero de propiedades:
        PropertyConfigurator.configure("src/main/resources/log4.properties");
    }

    /**
     * Método que confirma la transacción y cierra la conexión
     * @author Antonio Mas Esteve
     */
    protected void close(){
        //Confirma transacción y cierra la transacción y la conexión:
        em.getTransaction().commit();
        em.close();
    }
//////////////////////////////////////////////Métodos de CRUD//////////////////////////////////////////////////

    /**
     * Método para insertar un objeto en la base de datos.
     * @author Antonio Mas Esteve
     * @return boolean
     */
    public abstract boolean insertar(EntidadAbstracta ea);

    /**
     * Método para buscar una entidad en la base de datos.
     * @author Antonio Mas Esteve
     * @return instance of EntidadAbstracta
     */
    public abstract EntidadAbstracta buscar(int id);

    /**
     * Método para eliminar una entidad en la base de datos.
     * @author Antonio Mas Esteve
     * @return boolean
     */
    public abstract boolean eliminar(int id);

    /**
     * Método para modificar una entidad en la base de datos.
     * @author Antonio Mas Esteve
     * @return boolean
     */
    public abstract boolean modificar(EntidadAbstracta ea);

    /**
     * Método para listar todas las entidades de una tabla
     * @author Antonio Mas Esteve
     * @return List
     */
    public abstract List<?> listar();


}
