package org.ieschabas.backend.model;

import org.ieschabas.backend.enums.Rol;

import javax.persistence.*;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Superclase para definir los usuarios de la aplicación.
 * @author Antonio Mas Esteve
 */
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rol", discriminatorType = DiscriminatorType.STRING)
public class Usuario extends PersonaAbstracta {
    @Serial
    private static final long serialVersionUID = 7315871660229738315L;
    //Campos:
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "contrasenya", nullable = false)
    private String contrasenya;
    @Column(name = "direccion", nullable = false)
    protected String direccion;
    @Column(name = "activo", nullable = false)
    protected boolean activo;
    @Column(name = "fecha_registro", nullable = false)
    protected LocalDate fecha_registro;
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, insertable = false, updatable = false)
    protected Rol rol;

    /**
     * Constructor vacío de Usuario
     */
    public Usuario(){super();}

    /**
     * Constructor con argumentos de Usuario
     */
    public Usuario(int id, String nombre, String apellidos, String email, String contrasenya, String direccion, boolean activo, LocalDate fecha_registro, Rol rol) {
        super(id, nombre, apellidos);
        this.email = email;
        this.contrasenya = contrasenya;
        this.direccion = direccion;
        this.activo = activo;
        this.fecha_registro = fecha_registro;
        this.rol = rol;
    }

    /**
     * Devuelve el email del usuario.
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * añade el email del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * devuelve la contraseña del usuario
     * @return String
     */
    public String getContrasenya() {
        return contrasenya;
    }

    /**
     * Añade la contraseña del usuario
     */
    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    /**
     * Devuelve la dirección de un usuario.
     * @return String
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Añade la dirección de un usuario.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve el estado de alta de un usuario.
     * @return boolean
     */
    public boolean getActivo() {
        return activo;
    }

    /**
     * Cambia el estado de alta de un usuario.
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Devuelve la fecha de registro de un usuario.
     * @return LocalDate
     */
    public LocalDate getFecha_registro() {
        return fecha_registro;
    }

    /**
     * Añade la fecha de registro de un usuario.
     */
    public void setFecha_registro(LocalDate fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    /**
     * Devuelve el rol del usuario
     * @return Rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Añade el rol de un usuario.
     * @param rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    //Generamos métodos de Object:

    /**
     * Método para convertir los campos de Usuario (o de sus sublases) a String.
     * @return String
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", direccion='" + direccion + '\'' +
                ", activo=" + activo +
                ", fecha_registro=" + fecha_registro +
                ", rol=" + rol +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", id=" + id +
                '}';
    }

    /**
     * Método que compara la igualdad de contenido de dos instancias distintas.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return activo == usuario.activo && Objects.equals(email, usuario.email) && Objects.equals(contrasenya, usuario.contrasenya) && Objects.equals(direccion, usuario.direccion) && Objects.equals(fecha_registro, usuario.fecha_registro) && rol == usuario.rol;
    }

    /**
     * Método que devuelve el hash de la clase.
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(email, contrasenya, direccion, activo, fecha_registro, rol);
    }
}
