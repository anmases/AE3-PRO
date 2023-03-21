package org.ieschabas.views.cliente;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import org.ieschabas.clases.Actor;
import org.ieschabas.clases.Alquiler;
import org.ieschabas.clases.Director;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.enums.Valoracion;
import org.ieschabas.librerias.GestorPeliculas;
import org.ieschabas.views.MainView;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@PageTitle("Cliente")
@Route(value = "Cliente", layout = MainView.class)
@RolesAllowed("USER")
public class ClienteView extends Div {

      private ArrayList<Pelicula> listaPeliculas;
      private LocalDate fecha;
      private Div vistaPeliculas;
      private HorizontalLayout vistaAlquiler;

      private H4 titulo;
      private Paragraph descripcion;
      private Image caratula;
      private Icon estrella;

    public ClienteView() {
        setSizeFull();
        addClassName("Cliente-View");
        vistaAlquiler = new HorizontalLayout();
        vistaAlquiler.setVisible(false);

        add(listadoLayout(), vistaAlquiler);

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
        Div contenedorImagen = new Div();
        contenedorImagen.setMaxHeight("1000px");
        contenedorImagen.setWidth("750px");
        caratula = convertirImagenVaadin(pelicula);
        caratula.setSizeFull();
        contenedorImagen.add(caratula);
        VerticalLayout datosLayout = new VerticalLayout();
        H1 titulo = new H1(pelicula.getTitulo()+" "+"("+pelicula.getAnyoPublicacion()+")");
        Paragraph descripcion = new Paragraph(pelicula.getDescripcion());
        Paragraph reparto = new Paragraph(obtenerNombresActores(pelicula));
        Paragraph directores = new Paragraph(obtenerNombresDirectores(pelicula));
        HorizontalLayout caracteristicas = new HorizontalLayout();
        Button categoria = new Button(pelicula.getCategoria().toString());
        Button formato = new Button(pelicula.getFormato().toString());
        caracteristicas.add(categoria, formato);
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

        alquilar.addClickListener(event->{
            fecha = fecha.now();
            Alquiler alquiler = new Alquiler();
            alquiler.setId(0);
            alquiler.setFechaAlquiler(fecha);
            alquiler.setIdPelicula(pelicula.getId());
            //Se añaden 2 meses:
            alquiler.setFechaRetorno(fecha.plusMonths(2));
            //alquiler.setIdCliente();
        });




        datosLayout.add(titulo, caracteristicas, descripcion, directores, reparto, botones);

        vistaAlquiler.add(contenedorImagen,datosLayout);
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
        ArrayList<Director> directores = pelicula.getDirectores();
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
