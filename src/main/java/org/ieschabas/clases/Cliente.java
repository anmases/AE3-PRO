package org.ieschabas.clases;

import org.ieschabas.enums.Rol;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serial;
import java.time.LocalDate;

/**
 * Subclase de Usuario, para instanciar los clientes.
 */
@Entity
@DiscriminatorValue(value = "USER")
public class Cliente extends Usuario{
    @Serial
    private static final long serialVersionUID = 4136881135296009295L;
    public Cliente(){}
    /**
     * Constructor de cliente.
     */
    public Cliente(int id, String nombre, String apellidos, String email, String contrasenya, String direccion, boolean activo, LocalDate fecha_registro){
        super(id, nombre, apellidos, email, contrasenya, direccion, activo, fecha_registro, Rol.USER);
    }


}
