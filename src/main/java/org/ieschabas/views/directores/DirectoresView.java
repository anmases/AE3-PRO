package org.ieschabas.views.directores;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.ieschabas.clases.Director;
import org.ieschabas.librerias.GestorDirectores;
import org.ieschabas.librerias.GestorPeliculas;
import org.ieschabas.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;

/**
 * Vista de los directores
 * @author Antonio Mas Esteve
 */
@PageTitle("Directores")
@Route(value = "Directores", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class DirectoresView extends VerticalLayout {

    //tabla:
    private Grid<Director> tabla;

    /**
     * Constructor de la vista de directores
     */
    public DirectoresView() {
        addClassName("directores-view");
        SplitLayout splitLayout = new SplitLayout();

        splitLayout.addToPrimary(crearTabla());
        splitLayout.addToSecondary(crearMenuEdicion());

        add(splitLayout);
    }

    /**
     * Creación de la tabla.
     * @author Antonio Mas Esteve
     * @return Grid
     */
    public Grid<Director> crearTabla() {
        tabla = new Grid<>(Director.class, false);
        tabla.setAllRowsVisible(true);
        tabla.addColumn(Director::getId).setAutoWidth(true).setHeader("Id");
        tabla.addColumn(Director::getNombre).setAutoWidth(true).setHeader("Nombre");
        tabla.addColumn(Director::getApellidos).setAutoWidth(true).setHeader("Apellidos");
        tabla.addColumn(Director::getAnyoNacimiento).setAutoWidth(true).setHeader("Año nacimiento");
        tabla.addColumn(Director::getPais).setAutoWidth(true).setHeader("Nacionalidad");
        rellenarTabla();
        refrescarTabla();
        return tabla;
    }

    /**
     * Creación del formulario lateral
     * @author Antonio Mas Esteve
     * @return FormLayout
     */
    public FormLayout crearFormulario() {

        FormLayout formLayout = new FormLayout();
        Binder<Director> binder = new Binder<>();
        HorizontalLayout vistaBoton = new HorizontalLayout();
        vistaBoton.setClassName("button-layout");
        Button cancelar = new Button("Cancelar");
        Button guardar = new Button("Guardar");
        Button eliminar = new Button("Eliminar");

        //Creamos los campos de texto y los vinculamos al Binder:
        IntegerField textId = new IntegerField("Id");
        binder.forField(textId).bind(Director::getId, Director::setId);
        textId.setEnabled(false);
        TextField textNombre = new TextField("Nombre");
        binder.forField(textNombre).bind(Director::getNombre, Director::setNombre);
        TextField textApellidos = new TextField("Apellidos");
        binder.forField(textApellidos).bind(Director::getApellidos, Director::setApellidos);
        IntegerField textAnyo = new IntegerField("Año de nacimiento");
        binder.forField(textAnyo).bind(Director::getAnyoNacimiento, Director::setAnyoNacimiento);
        TextField textPais = new TextField("Nacionalidad");
        binder.forField(textPais).bind(Director::getPais, Director::setPais);
        //definimos el selector:
        tabla.setSelectionMode(Grid.SelectionMode.SINGLE);
        tabla.asSingleSelect().addValueChangeListener(e->{
            Director director = e.getValue();
            //Vuelca los datos del ítem de la tabla (objeto) al formulario (que está correlacionado con binder).
            binder.setBean(director);

        });
        //Definimos los botones:
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelar.addClickListener(buttonClickEvent->{
            refrescarTabla();
            textId.clear();
            textNombre.clear();
            textApellidos.clear();
            textAnyo.clear();
            textPais.clear();
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.addClickListener(e->{
            //Volcamos los datos actualmente presentes en el formulario en on objeto ad-hoc:
            Director director = binder.getBean();
            //Aquí estableceremos que modifique o añada según el binder arroje un null o no:

            if(director == null){
                //Añadir
                if(textNombre.getValue() != null && textApellidos.getValue() != null && textAnyo.getValue() != null && textPais.getValue() != null) {
                        GestorDirectores.insertarDirector(director);
                        if(GestorDirectores.insertarDirector(director)) {
                            Notification notification = Notification.show("Director añadido correcamente");
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        }

                }else {
                    //Se muestra la notificación:
                    Notification notification = Notification.show("Error: Todos los campos deben rellenarse");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
            else{
                //Modificar:

                    GestorDirectores.modificarDirector(director);
                    if(GestorDirectores.modificarDirector(director)){
                        Notification notification = Notification.show("Director modificado correcamente");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                    else{
                        Notification notification = Notification.show("Error: Todos los campos deben rellenarse");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
            }
            //Volvemos a rellenar la tabla:
                rellenarTabla();
            //Refrescamos la tabla
            refrescarTabla();
            //Vaciamos el formulario:
            textId.clear();
            textNombre.clear();
            textApellidos.clear();
            textAnyo.clear();
            textPais.clear();
        });
        eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        eliminar.addClickListener(event->{
            //Volcamos los datos actualmente presentes en el formulario en on objeto ad-hoc:
            Director director = binder.getBean();
            try {
                //Borramos el director y todas sus relaciones asociadas:
                GestorPeliculas.desvincularDirector(director);
                GestorDirectores.eliminarDirector(director.getId());
                //Como el método es booleano, si lo hace muestra un mensaje de aceptación y si no lo hace, de error.
                if (GestorDirectores.eliminarDirector(director.getId())){
                    Notification notification = Notification.show("Director Borrado correctamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification notification = Notification.show("Director no borrado");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Volvemos a rellenar la tabla:
                rellenarTabla();
            //Refrescamos la tabla
            refrescarTabla();
            //Vaciamos el formulario:
            textId.clear();
            textNombre.clear();
            textApellidos.clear();
            textAnyo.clear();
            textPais.clear();
        });
        vistaBoton.add(guardar, cancelar, eliminar);
        formLayout.add(textId, textNombre, textApellidos, textAnyo, textPais, vistaBoton);

        return formLayout;
    }

    /**
     * Menú edición: título+formulario
     * @return VerticalLayout
     */
    public VerticalLayout crearMenuEdicion() {
        VerticalLayout editor = new VerticalLayout();
        H3 titulo = new H3("Editar Director");
        editor.add(titulo, crearFormulario());
        editor.setPadding(true);
        return editor;
    }

    /**
     * Método para rellenar la tabla.
     */
    public void rellenarTabla() {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(GestorDirectores.listarDirectores());
    }

    /**
     * Método para refrescar la tabla.
     */
    public void refrescarTabla() {
        tabla.select(null);
        tabla.getDataProvider().refreshAll();
    }



}
