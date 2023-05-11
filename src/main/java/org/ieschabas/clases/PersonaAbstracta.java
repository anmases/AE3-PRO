package org.ieschabas.clases;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serial;

/**
 * Clase abstracta que actúa como superclase de todas aquellas entidades que son personas.
 * @author Antonio Mas Esteve
 */
@MappedSuperclass
public abstract class PersonaAbstracta extends EntidadAbstracta {
    @Serial
    private static final long serialVersionUID = 3380202514637116591L;
    @Column(name = "nombre", nullable = false)
    protected String nombre;
    @Column(name = "apellidos", nullable = false)
    protected String apellidos;

    /**
     * Constructor vacío de la Superclase persona
     * @author Antonio Mas Esteve
     */
    public PersonaAbstracta() {
        super();
    }

    /**
     * Constructor con parámetros de la superclase Persona
     * @author Antonio Mas Esteve
     */
    public PersonaAbstracta(int id, String nombre, String apellidos) {
        super(id);
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    /**
     * Método para obtener el nombre de la persona.
     * @return String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para introducir el nombre de una persona
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para devolver los apellidos de una persona
     * @return String
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Método para introducir los apellidos de una persona
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
