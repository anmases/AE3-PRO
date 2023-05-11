package org.ieschabas.backend.clases;

import org.ieschabas.backend.enums.Puesto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serial;

/**
 * Subclase de Equipo para crear ocurrencias de directores.
 * @author Antonio Mas Esteve
 */
@Entity
@DiscriminatorValue(value = "ACTOR")
public class Actor extends Equipo {
    @Serial
    private static final long serialVersionUID = -2570882599195143267L;
    /**
     * constructor vacío.
     */
    public Actor(){
        super();
    }

    /**
     * constructor sobrecargado con argumentos referente a la superclase.
     */
    public Actor (int id, String nombre, String apellidos, int anyoNacimiento, String pais){
       super(id, nombre, apellidos, anyoNacimiento,pais, Puesto.ACTOR);
    }
}

