package org.ieschabas.clases;

import org.ieschabas.enums.Puesto;

import javax.persistence.*;
import java.io.Serial;
import java.util.Objects;

/**
 * SuperClase Equipo Servirá para instancias objetos de tipo persona o miembro de un equipo de subtipos actor o director.
 * @author Antonio Mas Esteve
 */
@Entity
@Table(name = "equipo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "puesto", discriminatorType = DiscriminatorType.STRING)
public class Equipo extends PersonaAbstracta {
    @Serial
    private static final long serialVersionUID = -3538416916498770369L;
    //Inicialización de los campos
    @Column(name = "anyo_nacimiento", nullable = false)
    protected int anyoNacimiento;
    @Column(name = "pais", nullable = false)
    protected String pais;
    @Column(name = "puesto", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected Puesto puesto;

    public Equipo(){super();}

    /**
     * Constructor de la superclase Equipo.
     */
    public Equipo(int id, String nombre, String apellidos, int anyoNacimiento, String pais, Puesto puesto) {
        super(id, nombre, apellidos);
        this.anyoNacimiento = anyoNacimiento;
        this.pais = pais;
        this.puesto = puesto;
    }

    /**
     * Definición del año de nacimiento
     */
    public void setAnyoNacimiento(int anyoNacimiento) {
        this.anyoNacimiento = anyoNacimiento;
    }

    /**
     * definición del país
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * devolver el año de nacimiento
     * @return int
     */
    public int getAnyoNacimiento() {
        return anyoNacimiento;
    }

    /**
     * devolver el país del actor
     * @return el valor del país
     */
    public String getPais() {
        return pais;
    }

    /**
     * Devuelve el puesto que ocupa.
     * @return Enum
     */
    public Puesto getPuesto() {
        return puesto;
    }

    /**
     * Añade el puesto que ocupa un miembro del equipo.
     */
    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    /**
     * Comprueba si son iguales en argumentos.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo equipo)) return false;
        return id == equipo.id && anyoNacimiento == equipo.anyoNacimiento && Objects.equals(nombre, equipo.nombre) && Objects.equals(apellidos, equipo.apellidos) && Objects.equals(pais, equipo.pais);
    }

    /**
     * Convierte todos los argumentos a String:
     * @return String
     */
    @Override
    public String toString() {
        return "Equipo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", anyoNacimiento=" + anyoNacimiento +
                ", pais='" + pais + '\'' +
                '}';
    }

}
