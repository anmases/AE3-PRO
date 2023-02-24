package org.ieschabas.views.actores;

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
import org.ieschabas.clases.Actor;
import org.ieschabas.librerias.GestorActores;
import org.ieschabas.views.MainLayout;
import java.io.IOException;

/**
 * Vista de los actores
 * @author Antonio Mas Esteve
 */
@PageTitle("Actores")
@Route(value = "Actores", layout = MainLayout.class)
public class ActoresView extends VerticalLayout {

    //tabla:
    private Grid<Actor> tabla;

    /**
     * Constructor de la vista actores
     * @throws IOException
     */
    public ActoresView() throws IOException {
      addClassName("actores-view");
        SplitLayout splitLayout = new SplitLayout();

        splitLayout.addToPrimary(crearTabla());
        splitLayout.addToSecondary(crearMenuEdicion());

        add(splitLayout);
    }

    /**
     * Crea el componente tabla.
     * @author Antonio Mas Esteve
     * @return
     * @throws IOException
     */
    public Grid<Actor> crearTabla() throws IOException {
        tabla = new Grid<>(Actor.class, false);
        tabla.setAllRowsVisible(true);
        tabla.addColumn(Actor::getId).setAutoWidth(true).setHeader("Id");
        tabla.addColumn(Actor::getNombre).setAutoWidth(true).setHeader("Nombre");
        tabla.addColumn(Actor::getApellidos).setAutoWidth(true).setHeader("Apellidos");
        tabla.addColumn(Actor::getAnyoNacimiento).setAutoWidth(true).setHeader("Año nacimiento");
        tabla.addColumn(Actor::getPais).setAutoWidth(true).setHeader("Nacionalidad");
        rellenarTabla();
        refrescarTabla();
        return tabla;
    }

    /**
     * Crea el componente formulario
     * @author Antonio Mas Esteve
     * @return
     * @throws IOException
     */
   public FormLayout crearFormulario() throws IOException {

        FormLayout formLayout = new FormLayout();
        Binder<Actor> binder = new Binder<>();
        HorizontalLayout vistaBoton = new HorizontalLayout();
        vistaBoton.setClassName("button-layout");
        Button cancelar = new Button("Cancelar");
        Button guardar = new Button("Guardar");
        Button eliminar = new Button("Eliminar");

        //Creamos los campos de texto y los vinculamos al Binder:
        IntegerField textId = new IntegerField("Id");
        binder.forField(textId).bind(Actor::getId, Actor::setId);
        textId.setEnabled(false);
        TextField textNombre = new TextField("Nombre");
        binder.forField(textNombre).bind(Actor::getNombre, Actor::setNombre);
        TextField textApellidos = new TextField("Apellidos");
        binder.forField(textApellidos).bind(Actor::getApellidos, Actor::setApellidos);
        IntegerField textAnyo = new IntegerField("Año de nacimiento");
        binder.forField(textAnyo).bind(Actor::getAnyoNacimiento, Actor::setAnyoNacimiento);
        TextField textPais = new TextField("Nacionalidad");
        binder.forField(textPais).bind(Actor::getPais, Actor::setPais);
        //definimos el selector:
        tabla.setSelectionMode(Grid.SelectionMode.SINGLE);
       tabla.asSingleSelect().addValueChangeListener(e->{
            Actor actor = e.getValue();
            //Vuelca los datos del ítem de la tabla (objeto) al formulario (que está correlacionado con binder).
                binder.setBean(actor);

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
            Notification notification = Notification.show("Se ha hecho!");
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.addClickListener(e->{
            //Volcamos los datos actualmente presentes en el formulario en on objeto ad-hoc:
            Actor actor = binder.getBean();
            //Aquí estableceremos que modifique o añada según el binder arroje un null o no:

            if(actor == null){
                //Añadir
                if(textNombre.getValue() != null && textApellidos.getValue() != null && textAnyo.getValue() != null && textPais.getValue() != null) {
                    try {
                        GestorActores.anyadirActor(textNombre.getValue(), textApellidos.getValue(), textAnyo.getValue(), textPais.getValue());

                            Notification notification = Notification.show("actor añadido correcamente");
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    //Se muestra la notificación:
                    Notification notification = Notification.show("Error: Todos los campos deben rellenarse");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
            else{
                //Modificar:
                try {
                    GestorActores.modificarActor(actor);
                    if(GestorActores.modificarActor(actor)){
                        Notification notification = Notification.show("actor modificado correcamente");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                    else{
                        Notification notification = Notification.show("Error: Todos los campos deben rellenarse");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            //Volvemos a rellenar la tabla:
            try {
                rellenarTabla();
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
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
            Actor actor = binder.getBean();
            try {
                GestorActores.borrarActor(actor.getId());
                //Como el método es booleano, si lo hace muestra un mensaje de aceptación y si no lo hace, de error.
                if (GestorActores.borrarActor(actor.getId())){
                    Notification notification = Notification.show("Actor Borrado correctamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification notification = Notification.show("Actor no borrado");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Volvemos a rellenar la tabla:
            try {
                rellenarTabla();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
     * Crea el menú edición título+formulario
     * @author Antonio Mas Esteve
     * @return
     * @throws IOException
     */
    public VerticalLayout crearMenuEdicion() throws IOException {
        VerticalLayout editor = new VerticalLayout();
        H3 titulo = new H3("Editar Actor");
        editor.add(titulo, crearFormulario());
        editor.setPadding(true);
        return editor;
    }

    /**
     * Método para rellenar la tabla desde el backend.
     * @author Antonio Mas Esteve
     * @throws IOException
     */
    public void rellenarTabla() throws IOException {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(GestorActores.listarActores().values());
    }

    /**
     * Método para refrescar la tabla
     * @author Antonio Mas Esteve
     */
    public void refrescarTabla() {
        tabla.select(null);
        tabla.getDataProvider().refreshAll();
    }


}
