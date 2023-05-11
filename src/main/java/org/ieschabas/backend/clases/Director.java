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
@DiscriminatorValue(value = "DIRECTOR")
public class Director extends Equipo{
    @Serial
    private static final long serialVersionUID = -7700628729900861829L;
    //Creación de los constructores:
    /**
     * constructor vacío.
     */
    public Director(){
        super();
    }

    /**
     * constructor sobrecargado que Extiende de la superclase Equipo.
     */
    public Director(int id, String nombre, String apellidos, int anyoNacimiento, String pais){
        super(id, nombre, apellidos, anyoNacimiento, pais, Puesto.DIRECTOR);
    }
}
