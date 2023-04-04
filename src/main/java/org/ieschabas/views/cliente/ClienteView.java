package org.ieschabas.views.cliente;


import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.ieschabas.clases.*;
import org.ieschabas.daos.AlquilerDAO;
import org.ieschabas.daos.PeliculaDAO;
import org.ieschabas.daos.UsuarioDAO;
import org.ieschabas.enums.Valoracion;
import org.ieschabas.views.login.LoginView;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de los clientes. Aquí se pueden alquilar las películas.
 * @author Antonio mas Esteve
 */
@PageTitle("Cliente")
@Route(value = "Cliente")
@RolesAllowed("USER")
public class ClienteView extends AppLayout {
    @Serial
    private static final long serialVersionUID = -5482598882200796969L;
    //DAOS:
    private static final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final AlquilerDAO alquilerDAO = new AlquilerDAO();

    private LocalDate fecha;
      private Div vistaPeliculas;
      private HorizontalLayout vistaAlquiler;
    private Image caratula;

    /**
     * Constructor principal de la vista Clientes.
     * @author Antonio Mas Esteve
     */
    public ClienteView() {
        Cliente cliente = (Cliente) usuarioDAO.buscar(LoginView.comprobarIdUsuario());
        //setSizeFull();
        addClassName("Cliente-View");
        vistaAlquiler = new HorizontalLayout();
        vistaAlquiler.setVisible(false);

        Div contenido = new Div(listadoLayout(cliente), vistaAlquiler);


        Image logo = new Image("images/PRO_LOGO.png", "VideoClub");
        logo.setMaxWidth("140px");
        logo.setMaxHeight("90px");

        addToNavbar(true, logo, crearCabecera(cliente));
        setContent(contenido);

    }

    /**
     * Método que crea la cabecera de la página.
     * @author Antonio Mas Esteve.
     * @return VerticalLayout
     */
    private VerticalLayout crearCabecera(Cliente cliente){
        VerticalLayout cabecera = new VerticalLayout();
        HorizontalLayout usuario = new HorizontalLayout();
        cabecera.setWidthFull();
        cabecera.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);

        Icon user = new Icon(VaadinIcon.USER);
        H4 nombreUsuario = new H4(cliente.getNombre()+" "+cliente.getApellidos());
        Button logout = new Button("Cerrar Sesión");
        usuario.add(user, nombreUsuario, logout);
        usuario.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        logout.addClickListener(e-> LoginView.cerrarSesion());
        cabecera.add(usuario);
        return cabecera;
    }

    /**
     * Método que crea la vista principal de las películas disponibles.
     * @author Antonio Mas Esteve
     * @return Div
     */
    public Div listadoLayout(Cliente cliente){
        List<Pelicula> listaPeliculas = peliculaDAO.listar();

           vistaPeliculas = new Div();
           vistaPeliculas.setWidthFull();
           vistaPeliculas.setHeightFull();
           vistaPeliculas.getStyle().set("display", "grid");
        vistaPeliculas.getStyle().set("grid-template-columns", "repeat(auto-fit, minmax(200px, 1fr))");

        VerticalLayout peliculaLayout;
        Div imagenLayout;
        HorizontalLayout valoracionLayout;
        for(Pelicula pelicula: listaPeliculas) {
            imagenLayout = new Div();
            imagenLayout.setMaxWidth("180px");
            imagenLayout.setMaxHeight("200px");
            peliculaLayout = new VerticalLayout();
            valoracionLayout = new HorizontalLayout();

            caratula = convertirImagenVaadin(pelicula);
            caratula.setSizeFull();
            imagenLayout.add(caratula);
            peliculaLayout.add(imagenLayout);
            //Añadimos el título:
            H4 titulo = new H4(pelicula.getTitulo() + " " + "(" + pelicula.getAnyoPublicacion() + ")");
            peliculaLayout.add(titulo);
            //Añade la valoración en estrellas:
            for (int i = 0; i < Valoracion.toInteger(pelicula.getValoracion()); i++) {

                Icon estrella = new Icon(VaadinIcon.STAR);
                estrella.setColor("#FFFF00");
                estrella.setSize("s");
                valoracionLayout.add(estrella);
                peliculaLayout.add(valoracionLayout);
            }
            peliculaLayout.addClickListener(event->{
                vistaAlquiler = alquilerLayout(pelicula, cliente);
                vistaPeliculas.setVisible(false);
                vistaAlquiler.setVisible(true);
            });
            vistaPeliculas.add(peliculaLayout);
        }


           return vistaPeliculas;
    }

    /**
     * Método que crea la vista de la ficha de la película.
     * @author Antonio Mas Esteve
     * @return HorizontalLayout
     */
    public HorizontalLayout alquilerLayout(Pelicula pelicula, Cliente cliente){
        //Se inicializa el contenedor de la imagen:
        vistaAlquiler.removeAll();
        Div contenedorImagen = new Div();
        contenedorImagen.setMaxHeight("1000px");
        contenedorImagen.setWidth("750px");
        caratula = convertirImagenVaadin(pelicula);
        caratula.setSizeFull();
        contenedorImagen.add(caratula);
        //Se instancian los botones:
        Button alquilar = new Button("Alquilar");
        alquilar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        alquilar.setWidthFull();

        Button volver = new Button("Volver");
        volver.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        volver.setWidthFull();

        Button ver = new Button("Ver Película");
        ver.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        ver.setWidthFull();
        //Muestra uno u otro botón si está o no alquilada:
        ver.setVisible(estaAlquilada(pelicula, cliente));
        alquilar.setVisible(!estaAlquilada(pelicula, cliente));

        //Creamos el cuadro de diálogo del alquiler:
        Dialog dialogo = new Dialog();
        H3 texto = new H3("¿Desea realmente alquilar la película?");
        Paragraph devolucion = new Paragraph("La fecha de devolución será dentro de dos meses desde hoy");
        Button confirmar = new Button("Confirmar");
        confirmar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        Button cancelar = new Button("Cancelar");
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout confirmacion = new HorizontalLayout(confirmar, cancelar);
        dialogo.add(texto, devolucion, confirmacion);
        cancelar.addClickListener(e-> dialogo.close());
        confirmar.addClickListener(e->{
            fecha = LocalDate.now();
            Alquiler alquiler = new Alquiler();
            alquiler.setId(0);
            alquiler.setFechaAlquiler(fecha);
            alquiler.setPelicula(pelicula);
            alquiler.setCliente(cliente);
            //Se añaden 2 meses:
            alquiler.setFechaRetorno(fecha.plusMonths(2));
            //Se añade a la BD:
        if(alquilerDAO.insertar(alquiler)) {
            Notification notification = Notification.show("Se ha alquilado correctamente");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
            ver.setVisible(true);
            alquilar.setVisible(false);
            dialogo.close();
        });
        //Creamos la ventana del video:
        Dialog videoLayout = new Dialog();
        VerticalLayout disposicionVideo = new VerticalLayout();
        disposicionVideo.setHorizontalComponentAlignment(FlexComponent.Alignment.END);
        Button cerrar = new Button();
        cerrar.setIcon(new Icon(VaadinIcon.CLOSE));
        Html video = new Html("<video width=\"640\" height=\"360\" src=\""+pelicula.getUrl()+"\" controls></video>");
        //Html video = new Html("<iframe width=\"640\" height=\"360\" frameborder=\"0\" src=\""+pelicula.getUrl()+"\" allowfullscreen ></iframe>");
        disposicionVideo.add(cerrar, video);
        videoLayout.add(disposicionVideo);
        cerrar.addClickListener(e->videoLayout.close());


        //Creamos el contenedor de los datos:
        VerticalLayout datosLayout = new VerticalLayout();
        H1 titulo = new H1(pelicula.getTitulo()+" "+"("+pelicula.getAnyoPublicacion()+")");
        Paragraph descripcion = new Paragraph(pelicula.getDescripcion());
        Paragraph reparto = new Paragraph(obtenerNombresActores(pelicula));
        Paragraph directores = new Paragraph(obtenerNombresDirectores(pelicula));
        HorizontalLayout caracteristicas = new HorizontalLayout();
        Button categoria = new Button("Categoría: "+pelicula.getCategoria().toString());
        Button formato = new Button("Formato: "+pelicula.getFormato().toString());
        Button duracion = new Button("Duración : "+pelicula.getDuracion()+"min");
        caracteristicas.add(categoria, formato, duracion);
        HorizontalLayout botones = new HorizontalLayout();




        botones.setWidthFull();
        volver.addClickListener(event->{
            vistaPeliculas.setVisible(true);
            vistaAlquiler.setVisible(false);
            vistaAlquiler.removeAll();
        });
        alquilar.addClickListener(event->dialogo.open());
        ver.addClickListener(event->videoLayout.open());

        botones.add(ver,alquilar,volver);




        datosLayout.add(titulo, caracteristicas, descripcion, directores, reparto, botones);

        vistaAlquiler.add(contenedorImagen,datosLayout, dialogo);
        return vistaAlquiler;
    }

public Image convertirImagenVaadin(Pelicula pelicula){
        Image imagen = new Image();
        imagen.setAlt("Carátula de "+pelicula.getTitulo());
        if (pelicula.getCaratula() != null) {


                    StreamResource streamResource = new StreamResource("caratula.jpg", ()->new ByteArrayInputStream(pelicula.getCaratula()));
                    imagen.setSrc(streamResource);

            }

   return imagen;
}
public String obtenerNombresActores(Pelicula pelicula){
                String nombreActor ="Reparto: ";
        List<Actor> actores = pelicula.getActores();
        for(Actor actor:actores){
            nombreActor = nombreActor+" "+actor.getNombre()+" "+actor.getApellidos()+",";
        }
        nombreActor = nombreActor.substring(0, nombreActor.length()-1) + ".\n ";
        return nombreActor;
}
    public String obtenerNombresDirectores(Pelicula pelicula){
        String nombreDirector="Directores: ";
        List<Director> directores = pelicula.getDirectores();
        for(Director director:directores){
            nombreDirector = nombreDirector+" "+director.getNombre()+" "+director.getApellidos()+",";
        }
        nombreDirector = nombreDirector.substring(0, nombreDirector.length()-1) + ".\n ";
        return nombreDirector;
    }
    public boolean estaAlquilada(Pelicula pelicula, Cliente cliente){
        //Establecemos la lista de alquileres:
        List<Alquiler> alquileres = alquilerDAO.listar();
        //establecemos la fecha.
        fecha = LocalDate.now();
        //Buscaremos en la lista si cumple los requisitos de: 1. la fecha de retorno ser mayor que la actual, 2.el cliente, 3.la película:
        for(Alquiler alquiler : alquileres){
            if(alquiler !=null) {
                if(alquiler.getCliente().equals(cliente)){
                    if(alquiler.getPelicula().equals(pelicula)){
                       if(alquiler.getFechaRetorno().isAfter(fecha)){
                           return true;
                       }
                    }
                }
            }
        }
        return false;
    }

}
