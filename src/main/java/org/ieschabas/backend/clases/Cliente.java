package org.ieschabas.backend.clases;

import org.ieschabas.backend.enums.Rol;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serial;
import java.time.LocalDate;

/**
 * Subclase de Usuario, para instanciar los clientes.
 * @author Antonio Mas Esteve
 */
@Entity
@DiscriminatorValue(value = "USER")
public class Cliente extends Usuario{
    @Serial
    private static final long serialVersionUID = 4136881135296009295L;

    /**
     * Constructor vacío
     */
    public Cliente(){super();}
    /**
     * Constructor de cliente.
     */
    public Cliente(int id, String nombre, String apellidos, String email, String contrasenya, String direccion, boolean activo, LocalDate fecha_registro){
        super(id, nombre, apellidos, email, contrasenya, direccion, activo, fecha_registro, Rol.USER);
    }


}
