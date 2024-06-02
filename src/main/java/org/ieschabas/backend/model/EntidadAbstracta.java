package org.ieschabas.backend.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;

/**
 * SuperClase superior de todas las demás entidades que habrá en la base de datos.
 * @author Antonio Mas Esteve
 */
@MappedSuperclass
public abstract class EntidadAbstracta implements Serializable {
    @Serial
    private static final long serialVersionUID = 3363868490616917723L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int id;

    /**
     * Constructor vacío de la clase EntidadAbstracta
     */
    public EntidadAbstracta(){super();}

    /**
     * Constructor con argumentos de la clase EntidadAbstracta
     */
    public EntidadAbstracta(int id){
        super();
        this.id = id;
    }

    /**
     * Obtiene el id de cualquier entidad.
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Fija el id de cualquier entidad
     */
    public void setId(int id) {
        this.id = id;
    }
}
