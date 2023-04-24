package org.ieschabas.views.alquileres;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.ieschabas.clases.Alquiler;
import org.ieschabas.daos.AlquilerDAO;
import org.ieschabas.views.MainView;

import javax.annotation.security.RolesAllowed;
import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista del historial de alquileres.
 * @author Antonio Mas Esteve
 */
@PageTitle("Alquileres")
@Route(value = "alquileres", layout = MainView.class)
@RolesAllowed("ADMIN")
public class AlquileresView extends VerticalLayout {
    @Serial
    private static final long serialVersionUID = -7652397079794250749L;
    private Grid <Alquiler> tabla;
    private final AlquilerDAO alquilerDao;

    /**
     * Constructor principal de la vista de alquileres.
     * Aquí se inyectan las dependencias de AlquilerDAO mediante SpringBoot IoC
     */
    public AlquileresView(AlquilerDAO alquilerDao) {
        this.alquilerDao = alquilerDao;
        setSizeFull();
        addClassName("Alquileres-View");
      H3 titulo = new H3("Historial de alquileres");
      add(titulo, crearBuscador(), crearTabla());
    }

    /**
     * Crea la tabla con los datos del alquiler.
     * @author Antonio Mas Esteve
     * @return Grid
     */
    public Component crearTabla() {
        tabla = new Grid<>(Alquiler.class, false);
        tabla.setAllRowsVisible(true);
        tabla.addColumn(Alquiler::getId).setAutoWidth(true).setResizable(true).setHeader("Id");
        tabla.addColumn(Alquiler::getNombreCliente).setAutoWidth(true).setHeader("Cliente");
        tabla.addColumn(Alquiler::getTituloPelicula).setAutoWidth(true).setResizable(true).setHeader("Película");
        tabla.addColumn(Alquiler::getFechaAlquiler).setAutoWidth(true).setResizable(true).setHeader("Fecha Alquiler");
        tabla.addColumn(Alquiler::getFechaRetorno).setAutoWidth(true).setResizable(true).setHeader("Fecha Devolución");
        rellenarTabla();
        refrescarTabla();
        return tabla;
    }

    /**
     * Crea los campos de búsqueda.
     * @author Antonio Mas Esteve
     * @return FormLayout
     */
    public FormLayout crearBuscador() {
        FormLayout panelBuscador = new FormLayout();
        //Creamos los campos:
        TextField textPelicula = new TextField("Pelicula");
        TextField textCliente = new TextField("Cliente");
        DatePicker textAlquilado = new DatePicker("Fecha de alquiler");
        DatePicker textDevolucion = new DatePicker("Fecha de devolución");
        textPelicula.setValue("");
        textCliente.setValue("");
        textAlquilado.setValue(null);
        textDevolucion.setValue(null);

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

 //Agregamos un escuchador de escritura para los campos:
        textPelicula.setValueChangeMode(ValueChangeMode.EAGER);
        textPelicula.addValueChangeListener(e-> {
            String tituloPelicula = e.getValue().toLowerCase();
            ArrayList<Alquiler> listaActualizada = new ArrayList<>();
            List<Alquiler> lista = alquilerDao.listar();
            for (Alquiler alquiler : lista) {
                if (alquiler.getTituloPelicula().toLowerCase().contains(tituloPelicula)) {
                    listaActualizada.add(alquiler);
                }
            }
            tabla.setItems(listaActualizada);
            refrescarTabla();
        });
        textCliente.setValueChangeMode(ValueChangeMode.EAGER);
        textCliente.addValueChangeListener(e->{
            String nombreCliente = e.getValue().toLowerCase();
            ArrayList<Alquiler> listaActualizada = new ArrayList<>();
            List<Alquiler> lista = alquilerDao.listar();
            for (Alquiler alquiler : lista) {
                if (alquiler.getNombreCliente().toLowerCase().contains(nombreCliente)) {
                    listaActualizada.add(alquiler);
                }
            }
            tabla.setItems(listaActualizada);
            refrescarTabla();
        });
        textAlquilado.addValueChangeListener(e->{
            LocalDate fechaAlquiler = e.getValue();
            ArrayList<Alquiler> listaActualizada = new ArrayList<>();
            List<Alquiler> lista = alquilerDao.listar();
            //Si no está vacío, que reescriba la tabla.
            if(!textAlquilado.isEmpty()) {
                for (Alquiler alquiler : lista) {
                    if (alquiler.getFechaAlquiler().equals(fechaAlquiler)) {
                        listaActualizada.add(alquiler);
                    }
                }
                tabla.setItems(listaActualizada);
                refrescarTabla();
            }
            else{
                tabla.setItems(lista);
                refrescarTabla();
            }
        });
        textDevolucion.addValueChangeListener(e->{
            LocalDate fechaDevolucion = e.getValue();
            ArrayList<Alquiler> listaActualizada = new ArrayList<>();
            List<Alquiler> lista = alquilerDao.listar();
            //Si no está vacío, que reescriba la tabla.
            if(!textDevolucion.isEmpty()) {
                for (Alquiler alquiler : lista) {
                    if (alquiler.getFechaRetorno().equals(fechaDevolucion)) {
                        listaActualizada.add(alquiler);
                    }
                }
                tabla.setItems(listaActualizada);
                refrescarTabla();
            }
            else{
                tabla.setItems(lista);
                refrescarTabla();
            }
        });


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
     */
    public void rellenarTabla() {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(alquilerDao.listar());
    }

}
