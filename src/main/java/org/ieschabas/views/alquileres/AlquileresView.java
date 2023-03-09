package org.ieschabas.views.alquileres;


import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.ieschabas.clases.Alquiler;
import org.ieschabas.clases.Cliente;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.enums.Categoria;
import org.ieschabas.enums.Formato;
import org.ieschabas.enums.Valoracion;
import org.ieschabas.librerias.GestorPeliculas;
import org.ieschabas.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Vista del historial de alquileres.
 * @author Antonio Mas Esteve
 */
@PageTitle("Alquileres")
@Route(value = "Alquileres", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AlquileresView extends VerticalLayout {

    private Grid <Alquiler> tabla;

    /**
     * Constructor principal de la vista de alquileres.
     * @throws IOException
     */
    public AlquileresView() throws IOException {
        setSizeFull();
        addClassName("Alquileres-View");
      H3 titulo = new H3("Historial de alquileres");
      add(titulo, crearBuscador(), crearTabla());
    }

    /**
     * Crea la tabla con los datos del alquiler.
     * @author Antonio Mas Esteve
     * @return
     * @throws IOException
     */
    public Grid<Alquiler> crearTabla() throws IOException {
        tabla = new Grid<>(Alquiler.class, false);
        tabla.setAllRowsVisible(true);
        tabla.addColumn(Alquiler::getId).setAutoWidth(true).setHeader("Id");
        //tabla.addColumn().setAutoWidth(true).setHeader("Cliente");
        tabla.addColumn(Alquiler::getTituloPelicula).setAutoWidth(true).setHeader("Película");
        tabla.addColumn(Alquiler::getFechaAlquiler).setAutoWidth(true).setHeader("Fecha Alquiler");
        tabla.addColumn(Alquiler::getFechaAlquiler).setAutoWidth(true).setHeader("Fecha Devolución");
        rellenarTabla();
        refrescarTabla();
        return tabla;
    }

    /**
     * Crea los campos de búsqueda.
     * @author Antonio Mas Esteve
     * @return
     */
    public FormLayout crearBuscador() {
        FormLayout panelBuscador = new FormLayout();
        //Creamos los campos:
        TextField textPelicula = new TextField("Pelicula");
        TextField textCliente = new TextField("Cliente");
        TextField textAlquilado = new TextField("Fecha de alquiler");
        TextField textDevolucion = new TextField("Fecha de devolución");
        textPelicula.setValue("");
        textCliente.setValue("");
        textAlquilado.setValue("");
        textDevolucion.setValue("");

        //Añadimos los títulos:
        textPelicula.setPlaceholder("buscar por título");
        textCliente.setPlaceholder("buscar por nombre");
        textAlquilado.setPlaceholder("dd/mm/aaaa");
        textDevolucion.setPlaceholder("dd/mm/aaaa");
        //Añadimos a cada uno botón para borrar lo escrito:
        textPelicula.setClearButtonVisible(true);
        textCliente.setClearButtonVisible(true);
        textAlquilado.setClearButtonVisible(true);
        textDevolucion.setClearButtonVisible(true);

       /** //Agregamos un escuchador de escritura para los campos:
        textPelicula.setValueChangeMode(ValueChangeMode.EAGER);
        textPelicula.addValueChangeListener(e-> {
            String tituloPelicula = e.getValue().toLowerCase();
            ArrayList<Pelicula> listaActualizada = new ArrayList<>();
            Collection<Pelicula> lista;
            try {
                lista = GestorPeliculas.listarPeliculas().values();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Pelicula pelicula;
            Iterator<Pelicula> iterador = lista.iterator();
            while(iterador.hasNext()){
                pelicula = iterador.next();
                if(pelicula.getTitulo().toLowerCase().contains(tituloPelicula)){
                    listaActualizada.add(pelicula);
                }
            }
            tabla.setItems(listaActualizada);
            refrescarTabla();
        });



        textAnyo.setValueChangeMode(ValueChangeMode.EAGER);
        textAnyo.addValueChangeListener(e-> {
            if(e.getValue() != null) {
                //Es necesario convertirlo a String porque solo así se comprueba si "contiene" un caracter.
                String anyoEstreno = e.getValue().toString();
                ArrayList<Pelicula> listaActualizada = new ArrayList<>();
                Collection<Pelicula> lista;
                try {
                    lista = GestorPeliculas.listarPeliculas().values();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Pelicula pelicula;
                Iterator<Pelicula> iterador = lista.iterator();
                while (iterador.hasNext()) {
                    pelicula = iterador.next();
                    String getAnyo = String.valueOf(pelicula.getAnyoPublicacion());
                    if (getAnyo.contains(anyoEstreno)) {
                        listaActualizada.add(pelicula);
                    }
                }
                tabla.setItems(listaActualizada);
                refrescarTabla();
            }else{
                try {
                    rellenarTabla();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                refrescarTabla();
            }

        });**/

        panelBuscador.add(textPelicula, textCliente, textAlquilado, textDevolucion);
        return panelBuscador;
    }

    /**
     * Método para refrescar la tabla:
     * @author Antonio Mas Esteve
     */
    public void refrescarTabla() {
        tabla.getDataProvider().refreshAll();
    }

    /**
     * Método que carga los datos que obtiene el backend desde
     * el fichero en una colección local y rellena la tabla:
     * @author Antonio Mas Esteve
     * @throws IOException
     */
    public void rellenarTabla() throws IOException {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems();
    }


}
