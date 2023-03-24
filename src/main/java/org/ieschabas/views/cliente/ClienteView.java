package org.ieschabas.views.cliente;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
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
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.ieschabas.clases.*;
import org.ieschabas.enums.Valoracion;
import org.ieschabas.librerias.GestorAlquileres;
import org.ieschabas.librerias.GestorPeliculas;
import org.ieschabas.librerias.GestorUsuarios;
import org.ieschabas.views.login.LoginView;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@PageTitle("Cliente")
@Route(value = "Cliente")
@RolesAllowed("USER")
public class ClienteView extends AppLayout {
      private ArrayList<Pelicula> listaPeliculas;
      private LocalDate fecha;
      private Div vistaPeliculas;
      private HorizontalLayout vistaAlquiler;
      private H4 titulo;
      private Image caratula;
      private Icon estrella;

    public ClienteView() {
        //setSizeFull();
        addClassName("Cliente-View");
        vistaAlquiler = new HorizontalLayout();
        vistaAlquiler.setVisible(false);
        Div contenido = new Div(listadoLayout(), vistaAlquiler);

       // DrawerToggle toggle = new DrawerToggle();
       // toggle.getElement().setAttribute("aria-label", "Menu toggle");
        Image logo = new Image("images/PRO_LOGO.png", "VideoClub");
        logo.setMaxWidth("140px");
        logo.setMaxHeight("90px");

        addToNavbar(true, logo, crearCabecera());
        setContent(contenido);

    }

    private VerticalLayout crearCabecera(){
        VerticalLayout cabecera = new VerticalLayout();
        HorizontalLayout usuario = new HorizontalLayout();
        cabecera.setWidthFull();
        cabecera.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        //Se coloca el usuario en este momento:
        int id = LoginView.comprobarIdUsuario();
        Usuario user = GestorUsuarios.buscarUsuario(id);

        Icon cliente = new Icon(VaadinIcon.USER);
        H4 nombreUsuario = new H4(user.getNombre()+" "+user.getApellidos());
        Button logout = new Button("Cerrar Sesión");
        usuario.add(cliente, nombreUsuario, logout);
        usuario.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        logout.addClickListener(e-> LoginView.cerrarSesion());
        cabecera.add(usuario);
        return cabecera;
    }
    public Div listadoLayout(){
        listaPeliculas = GestorPeliculas.listarPeliculas();

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
            titulo = new H4(pelicula.getTitulo()+" "+"("+pelicula.getAnyoPublicacion()+")");
            peliculaLayout.add(titulo);
            //Añade la valoración en estrellas:
            for (int i = 0; i < Valoracion.toInteger(pelicula.getValoracion()); i++) {

                estrella = new Icon(VaadinIcon.STAR);
                estrella.setColor("#FFFF00");
                estrella.setSize("s");
                valoracionLayout.add(estrella);
                peliculaLayout.add(valoracionLayout);
            }
            peliculaLayout.addClickListener(event->{
                vistaAlquiler = alquilerLayout(pelicula);
                vistaPeliculas.setVisible(false);
                vistaAlquiler.setVisible(true);
            });
            vistaPeliculas.add(peliculaLayout);
        }


           return vistaPeliculas;
    }
    public HorizontalLayout alquilerLayout(Pelicula pelicula){
        //Se inicializa el contenedor de la imagen:
        Div contenedorImagen = new Div();
        contenedorImagen.setMaxHeight("1000px");
        contenedorImagen.setWidth("750px");
        caratula = convertirImagenVaadin(pelicula);
        caratula.setSizeFull();
        contenedorImagen.add(caratula);

        //Creamos el cuadro de diálogo:
        Dialog dialogo = new Dialog();
        H3 texto = new H3("¿Desea realmente alquilar la película?");
        Paragraph devolucion = new Paragraph("La fecha de devolución será dentro de dos meses desde hoy");
        Button confirmar = new Button("Confirmar");
        confirmar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        Button cancelar = new Button("Cancelar");
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout confirmacion = new HorizontalLayout(confirmar, cancelar);
        dialogo.add(texto, devolucion, confirmacion);
        cancelar.addClickListener(e->{
            dialogo.close();
        });
        confirmar.addClickListener(e->{
            fecha = fecha.now();
            Alquiler alquiler = new Alquiler();
            alquiler.setId(0);
            alquiler.setFechaAlquiler(fecha);
            alquiler.setIdPelicula(pelicula.getId());
            alquiler.setIdCliente(LoginView.comprobarIdUsuario());
            //Se añaden 2 meses:
            alquiler.setFechaRetorno(fecha.plusMonths(2));
            //Se añade a la BD:
        if(GestorAlquileres.insertarAlquiler(alquiler)) {
            Notification notification = Notification.show("Se ha alquilado correctamente");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        dialogo.close();
        });
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
        Button alquilar = new Button("Alquilar");
        alquilar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        alquilar.setWidthFull();
        Button volver = new Button("Volver");
        volver.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        volver.setWidthFull();
        botones.setWidthFull();
        botones.add(alquilar,volver);
        volver.addClickListener(event->{
            vistaPeliculas.setVisible(true);
            vistaAlquiler.setVisible(false);
            vistaAlquiler.removeAll();
        });

        alquilar.addClickListener(event->dialogo.open());




        datosLayout.add(titulo, caracteristicas, descripcion, directores, reparto, botones);

        vistaAlquiler.add(contenedorImagen,datosLayout, dialogo);
        return vistaAlquiler;
    }

public Image convertirImagenVaadin(Pelicula pelicula){
        Image imagen = new Image();
        imagen.setAlt("Carátula de "+pelicula.getTitulo());
        if (pelicula.getCaratula() != null) {
            try {
                    InputStream in = pelicula.getCaratula().getBinaryStream();
                    byte [] array = in.readAllBytes();
                    StreamResource streamResource = new StreamResource("caratula.jpg", ()->new ByteArrayInputStream(array));
                    imagen.setSrc(streamResource);
                } catch(SQLException | IOException e){
                    throw new RuntimeException(e);
                }
            }

   return imagen;
}
public String obtenerNombresActores(Pelicula pelicula){
                String nombreActor ="Reparto: ";
        ArrayList<Actor> actores = pelicula.getActores();
        for(Actor actor:actores){
            nombreActor = nombreActor+" "+actor.getNombre()+" "+actor.getApellidos()+",";
        }
        nombreActor = nombreActor.substring(0, nombreActor.length()-1) + ".\n ";
        return nombreActor;
}
    public String obtenerNombresDirectores(Pelicula pelicula){
        String nombreDirector="Directores: ";
        ArrayList<Director> directores = pelicula.getDirectores();
        for(Director director:directores){
            nombreDirector = nombreDirector+" "+director.getNombre()+" "+director.getApellidos()+",";
        }
        nombreDirector = nombreDirector.substring(0, nombreDirector.length()-1) + ".\n ";
        return nombreDirector;
    }

}
