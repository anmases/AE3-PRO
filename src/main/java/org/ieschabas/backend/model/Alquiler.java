package org.ieschabas.backend.model;

import org.ieschabas.backend.daos.PeliculaDAO;
import org.ieschabas.backend.daos.UsuarioDAO;

import javax.persistence.*;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase para crear los alquileres
 * @author Antonio Mas Esteve
 */
@Entity
@Table(name = "alquiler")
public class Alquiler extends EntidadAbstracta{
    @Serial
    private static final long serialVersionUID = 9072242108979489252L;
    //instanciar campos:
    @Column(name="fecha_alquiler", nullable = false)
    private LocalDate fechaAlquiler;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="id_cliente", nullable = false)
    private Cliente cliente;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="id_pelicula", nullable = false)
    private Pelicula pelicula;
    @Column(name="fecha_retorno", nullable = false)
    private LocalDate fechaRetorno;

    /**
     * Constructor vacío alquiler
     */
    public Alquiler(){super();}

    /**
     * Constructor con argumentos de la clase alquiler
     * @author Antonio Mas Esteve
     */
    public Alquiler(int id, LocalDate fechaAlquiler, Cliente cliente, Pelicula pelicula, LocalDate fechaRetorno) {
        super(id);
        this.fechaAlquiler = fechaAlquiler;
        this.pelicula = pelicula;
        this.cliente = cliente;
        this.fechaRetorno = fechaRetorno;
    }

//Métodos Setters y getters:


    /**
     * devuelve fecha del alquiler
     * @return LocalDate
     */
    public LocalDate getFechaAlquiler() {
        return fechaAlquiler;
    }

    /**
     * devuelve id cliente
     * @return int
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * devuelve id película
     * @return int
     */
    public Pelicula getPelicula() {
        return pelicula;
    }

    /**
     * devuelve la fecha de devolución
     * @return LocalDate
     */
    public LocalDate getFechaRetorno() {
        return fechaRetorno;
    }


    /**
     * Añade la fecha
     */
    public void setFechaAlquiler(LocalDate fechaAlquiler) {
        this.fechaAlquiler = fechaAlquiler;
    }

    /**
     * añade el id de la película.
     */
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    /**
     * añade el id del cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * añade la fecha de devolución
     */
    public void setFechaRetorno(LocalDate fechaRetorno) {
        this.fechaRetorno = fechaRetorno;
    }

    /**
     * Método para obtener un get, del título de la película al cual corresponde el IdPelicula
     * @author Antonio Mas Esteve.
     * @return string
     */
    public String getTituloPelicula () {
        PeliculaDAO peliculaDAO = new PeliculaDAO();
        Pelicula pelicula;
        pelicula = peliculaDAO.buscar(this.pelicula.getId());
        return pelicula.getTitulo();
    }

    /**
     * Método para obtener el nombre del cliente que tiene asociado el alquiler
     * @return string
     */
    public String getNombreCliente(){
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario;
          usuario = usuarioDAO.buscar(this.cliente.getId());
    return usuario.getNombre()+" "+usuario.getApellidos();
    }

    /**
     * devuelve la información del alquiler en forma de texto.
     * @return String
     */
    @Override
    public String toString() {
        return "Alquiler{" +
                "id=" + id +
                ", fechaAlquiler='" + fechaAlquiler + '\'' +
                ", nombreCliente=" + getNombreCliente()+
                ", idCliente=" + cliente.getId() +
                ", tituloPelicula="+ getTituloPelicula() +
                ", idPelicula=" + pelicula.getId() +
                ", fechaRetorno='" + fechaRetorno + '\'' +
                '}';
    }

    /**
     * Compara dos alquileres, si sus propiedades son idénticas.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alquiler alquiler)) return false;
        return Objects.equals(fechaAlquiler, alquiler.fechaAlquiler) && Objects.equals(cliente, alquiler.cliente) && Objects.equals(pelicula, alquiler.pelicula) && Objects.equals(fechaRetorno, alquiler.fechaRetorno);
    }

    /**
     * devuelve el código hash de un objeto.
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(fechaAlquiler, cliente, pelicula, fechaRetorno);
    }
}
