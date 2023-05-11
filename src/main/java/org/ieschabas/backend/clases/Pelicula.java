package org.ieschabas.backend.clases;

import org.ieschabas.backend.daos.EquipoDAO;
import org.ieschabas.backend.enums.Categoria;
import org.ieschabas.backend.enums.Formato;
import org.ieschabas.backend.enums.Valoracion;

import javax.persistence.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Esta clase es para instanciar objetos de tipo String, int o enum, que serán atributos de las ocurrencias de Películas.
 *  La clave primaria o identificador es el Id. Definida como Integer por si contiene caracteres no numéricos.
 *  @author Antonio Mas Esteve
 */
@Entity
@Table(name = "pelicula")
public class Pelicula extends EntidadAbstracta{
    @Serial
    private static final long serialVersionUID = -3234360035909220427L;
    //Instanciación de los campos
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "anyo_publicacion")
    private int anyoPublicacion;
    @Column(name = "duracion")
    private String duracion;
    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Column(name = "formato")
    @Enumerated(EnumType.STRING)
    private Formato formato;
    @Column(name = "valoracion")
    @Enumerated(EnumType.STRING)
    private Valoracion valoracion;
    @Column(name = "caratula")
    @Basic(fetch = FetchType.LAZY)
    private byte[] caratula;
    @Column(name = "url")
    private String url;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "pelicula_equipo", joinColumns = {@JoinColumn(name = "id_pelicula", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "id_equipo", referencedColumnName = "id")})
    private List<Equipo> equipos;

    //creación constructores
    /**
     * Constructor vacío
     */
    public Pelicula(){super();}

    /**
     * Constructor para peliculas no relacionadas.
     */
    public Pelicula(int id, String titulo, String descripcion, int anyoPublicacion, String duracion, Categoria categoria, Formato formato, Valoracion valoracion) {
        super(id);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.anyoPublicacion = anyoPublicacion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.formato = formato;
        this.valoracion = valoracion;
    }

    /**
     * Sobrecarga de la película con las relaciones.
     */
    public Pelicula(int id, String titulo, String descripcion, int anyoPublicacion, String duracion, Categoria categoria, Formato formato, Valoracion valoracion, byte[] caratula, String url, List<Equipo> equipos) {
        super(id);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.anyoPublicacion = anyoPublicacion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.formato = formato;
        this.valoracion = valoracion;
        this.equipos = equipos;
        this.url = url;
        this.caratula = caratula;

    }
    // creamos sets y gets

    /**
     * define el título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * define la descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * define el año de publicación
     */
    public void setAnyoPublicacion(int anyoPublicacion) {
        this.anyoPublicacion = anyoPublicacion;
    }

    /**
     * define la duración
     */
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    /**
     * define la categoría
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * define el formato
     */
    public void setFormato(Formato formato) {
        this.formato = formato;
    }

    /**
     * añade la valoración
     */
    public void setValoracion(Valoracion valoracion) {
        this.valoracion = valoracion;
    }

    /**
     * Añade relaciones con los distintos equipo
     */
    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    /**
     * Añade la portada de la película
     */
    public void setCaratula(byte[] caratula) {
        this.caratula = caratula;
    }


    /**
     * devuelve el título
     * @return titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * devuelve la descripción
     * @return String
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * devuelve el año de publicación
     * @return añoPublicacion
     */
    public int getAnyoPublicacion() {
        return anyoPublicacion;
    }

    /**
     * devuelve la duración
     * @return duracion
     */
    public String getDuracion() {
        return duracion;
    }

    /**
     * devuelve la categoría
     * @return categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * devuelve el formato
     * @return formato
     */
    public Formato getFormato() {
        return formato;
    }

    /**
     * devuelve la valoración
     * @return Valoracion
     */
    public Valoracion getValoracion() {
        return valoracion;
    }

    /**
     * Devuelve las relaciones de una película con sus equipo
     * @return ArrayList
     */
    public List<Equipo> getEquipos() {
        return equipos;
    }


    /**
     * Devuelve la portada de la película
     * @return Image
     */
    public byte[] getCaratula() {
        return caratula;
    }

    /**
     * Método que devuelve la URL del video en Mega
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Se añade el enlace al video embebido
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Devuelve la lista de equipo relacionados
     * @return List
     */
    public List<Actor> getActores(){
        List<Actor> actores = new ArrayList<>();
        for(Equipo equipo:equipos){
            if(equipo instanceof Actor){
                actores.add((Actor)equipo);
            }
        }
        return actores;
    }

    /**
     * Método para eliminar un miembro del equipo relacionado.
     * @return boolean
     */
    public boolean eliminarRelacion(Equipo equipo){
        return equipos.remove(equipo);
    }

    /**
     * Método para obtener los directores relacionados
     * @return List
     */
    public List<Director> getDirectores(){
        List<Director> directores = new ArrayList<>();
        for(Equipo equipo:equipos){
            if(equipo instanceof Director){
                directores.add((Director)equipo);
            }
        }
        return directores;
    }

    /**
     * Devuelve los equipo que no están relacionados
     * @return List
     */
    public List<Actor> getActoresRestantes(){
        EquipoDAO equipoDAO = new EquipoDAO();
        List<Actor> actoresRestantes = equipoDAO.listarActores();
        actoresRestantes.removeAll(getActores());
        return actoresRestantes;
    }

    /**
     * Devuelve los equipo que no están relacionados
     *@return List
     */
    public List<Director> getDirectoresRestantes(){
        EquipoDAO equipoDAO = new EquipoDAO();
        List<Director> directoresRestantes = equipoDAO.listarDirectores();
        directoresRestantes.removeAll(getDirectores());
        return directoresRestantes;
    }

    /**
     * Convierte la info de la película a cadena de caracteres.
     * @return String
     */
    @Override
    public String toString() {
        return "Pelicula{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", anyoPublicacion=" + anyoPublicacion +
                ", duracion='" + duracion + '\'' +
                ", categoria=" + categoria +
                ", formato=" + formato +
                ", valoracion=" + valoracion +
                ", caratula=" + Arrays.toString(caratula) +
                ", url='" + url + '\'' +
                ", equipos=" + equipos +
                ", id=" + id +
                '}';
    }

    /**
     * Compara si dos películas son iguales.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pelicula pelicula)) return false;
        return anyoPublicacion == pelicula.anyoPublicacion && Objects.equals(titulo, pelicula.titulo) && Objects.equals(descripcion, pelicula.descripcion) && Objects.equals(duracion, pelicula.duracion) && categoria == pelicula.categoria && formato == pelicula.formato && valoracion == pelicula.valoracion && Arrays.equals(caratula, pelicula.caratula) && Objects.equals(url, pelicula.url) && Objects.equals(equipos, pelicula.equipos);
    }

    /**
     * devuelve el hash del objeto.
     * @return int
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(titulo, descripcion, anyoPublicacion, duracion, categoria, formato, valoracion, url, equipos);
        result = 31 * result + Arrays.hashCode(caratula);
        return result;
    }
}

