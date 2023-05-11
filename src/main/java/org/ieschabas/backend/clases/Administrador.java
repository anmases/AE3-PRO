package org.ieschabas.backend.clases;

import org.ieschabas.backend.enums.Rol;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serial;
import java.time.LocalDate;

/**
 * Subclase de usuario para instanciar Adminsitradores.
 * @author Antonio Mas Esteve
 */
@Entity
@DiscriminatorValue(value = "ADMIN")
public class Administrador extends Usuario{
    @Serial
    private static final long serialVersionUID = -7661266229739179432L;

    public Administrador(){super();}

    /**
     * Constructor de Administrador
     */
    public Administrador(int id, String nombre, String apellidos, String email, String contrasenya, String direccion, boolean activo, LocalDate fecha_registro){
        super(id, nombre, apellidos,  email, contrasenya, direccion, activo, fecha_registro, Rol.ADMIN);
    }
}
