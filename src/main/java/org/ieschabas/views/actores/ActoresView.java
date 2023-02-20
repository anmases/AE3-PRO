package org.ieschabas.views.actores;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.ieschabas.clases.Actor;

import org.ieschabas.librerias.GestorActores;

import org.ieschabas.views.MainLayout;
import java.io.IOException;

import static org.ieschabas.librerias.GestorActores.borrarActor;

@PageTitle("Actores")
@Route(value = "Actores", layout = MainLayout.class)
public class ActoresView extends VerticalLayout {

    //tabla:
    private Grid<Actor> tabla;
    //objeto actor:
    private Actor actorSeleccionado;
    //Disposición de la tabla:
    //private VerticalLayout anyadirTabla = new VerticalLayout();
    //Disposición del editor:
    //private VerticalLayout anyadirEditor = new VerticalLayout();
    //Vista principal partida:


    //private Actor actor;
    public ActoresView() throws IOException {
      addClassName("actores-view");
        SplitLayout splitLayout = new SplitLayout();

        splitLayout.addToPrimary(crearTabla());
        splitLayout.addToSecondary(crearFormulario());

        add(splitLayout);
    }

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
            try {
                rellenarTabla();
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
            refrescarTabla();
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


    public void rellenarTabla() throws IOException {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(GestorActores.listarActores().values());
    }
    public void refrescarTabla() {
        tabla.select(null);
        tabla.getDataProvider().refreshAll();
    }


}
