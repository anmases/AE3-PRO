package org.ieschabas.views.equipo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import org.ieschabas.backend.model.Actor;
import org.ieschabas.backend.model.Equipo;
import org.ieschabas.backend.daos.EquipoDAO;
import org.ieschabas.backend.enums.Puesto;
import org.ieschabas.views.MainView;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista de los equipo
 * @author Antonio Mas Esteve
 */
@PageTitle("Equipo")
@Route(value = "equipo", layout = MainView.class)
@RolesAllowed("ADMIN")
public class EquipoView extends VerticalLayout {
    private final EquipoDAO equipoDao;
    //tabla:
    private Grid<Equipo> tabla;

    /**
     * Constructor de la vista equipo
     * Aquí se inyectan las dependencias de EquipoDAO mediante SpringBoot IoC
     */
    public EquipoView(EquipoDAO equipoDao) throws IOException {
        this.equipoDao = equipoDao;
        addClassName("equipo-view");
        SplitLayout splitLayout = new SplitLayout();

        splitLayout.addToPrimary(crearTabla());
        splitLayout.addToSecondary(crearMenuEdicion());

        add(splitLayout);
    }

    /**
     * Crea el componente tabla.
     * @return Grid
     * @author Antonio Mas Esteve
     */
    public Grid<Equipo> crearTabla() {
        tabla = new Grid<>(Equipo.class, false);
        tabla.setAllRowsVisible(true);
        tabla.addColumn(Equipo::getId).setAutoWidth(true).setHeader("Id").setVisible(false);
        tabla.addColumn(Equipo::getNombre).setAutoWidth(true).setHeader("Nombre");
        tabla.addColumn(Equipo::getApellidos).setAutoWidth(true).setHeader("Apellidos");
        tabla.addColumn(Equipo::getAnyoNacimiento).setAutoWidth(true).setHeader("Año nacimiento");
        tabla.addColumn(Equipo::getPais).setAutoWidth(true).setHeader("Nacionalidad");
        tabla.addColumn(Equipo::getPuesto).setAutoWidth(true).setHeader("Rol");
        rellenarTabla();
        refrescarTabla();
        return tabla;
    }

    /**
     * Crea el componente formulario
     * @return FormLayout
     * @author Antonio Mas Esteve
     */
    public FormLayout crearFormulario() {

        FormLayout formLayout = new FormLayout();
        Binder<Equipo> binder = new Binder<>();
        HorizontalLayout vistaBoton = new HorizontalLayout();
        vistaBoton.setClassName("button-layout");
        Button cancelar = new Button("Cancelar");
        Button guardar = new Button("Guardar");
        Button eliminar = new Button("Eliminar");

        //Creamos los campos de texto y los vinculamos al Binder:
        IntegerField textId = new IntegerField("Id");
        binder.forField(textId).bind(Equipo::getId, Equipo::setId);
        textId.setEnabled(false);
        textId.setVisible(false);
        TextField textNombre = new TextField("Nombre");
        binder.forField(textNombre).bind(Equipo::getNombre, Equipo::setNombre);
        TextField textApellidos = new TextField("Apellidos");
        binder.forField(textApellidos).bind(Equipo::getApellidos, Equipo::setApellidos);
        IntegerField textAnyo = new IntegerField("Año de nacimiento");
        binder.forField(textAnyo).bind(Equipo::getAnyoNacimiento, Equipo::setAnyoNacimiento);
        TextField textPais = new TextField("Nacionalidad");
        binder.forField(textPais).bind(Equipo::getPais, Equipo::setPais);
        //definimos el selector:
        tabla.setSelectionMode(Grid.SelectionMode.SINGLE);
        tabla.asSingleSelect().addValueChangeListener(e -> {
            Equipo equipo = e.getValue();
            //Vuelca los datos del ítem de la tabla (objeto) al formulario (que está correlacionado con binder).
            binder.setBean(equipo);

        });
        //Definimos los botones:
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelar.addClickListener(buttonClickEvent -> {
            refrescarTabla();
            textId.clear();
            textNombre.clear();
            textApellidos.clear();
            textAnyo.clear();
            textPais.clear();
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.addClickListener(e -> {
            //Volcamos los datos actualmente presentes en el formulario en on objeto ad-hoc:
            Equipo equipo = binder.getBean();
            //Aquí estableceremos que modifique o añada según el binder arroje un null o no:

            if (equipo == null) {
                //Añadir
                if (textNombre.getValue() != null && textApellidos.getValue() != null && textAnyo.getValue() != null && textPais.getValue() != null) {
                    //Se crea un actor con los campos del formuario como argumentos. El "id" es por defecto 0, luego la BD lo rellenará automáticamente.
                    equipo = new Actor(0, textNombre.getValue(), textApellidos.getValue(), textAnyo.getValue(), textPais.getValue());
                    if (equipoDao.insertar(equipo)) {
                        Notification notification = Notification.show("elemento añadido correcamente");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                } else {
                    //Se muestra la notificación:
                    Notification notification = Notification.show("Error: Todos los campos deben rellenarse");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                //Modificar:
                equipoDao.modificar(equipo);
                if (equipoDao.modificar(equipo)) {
                    Notification notification = Notification.show("elemento modificado correcamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
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
        eliminar.addClickListener(event -> {
            //Volcamos los datos actualmente presentes en el formulario en on objeto ad-hoc:
            Equipo equipo = binder.getBean();
            if(equipo != null) {
                //Como el método es booleano, si lo hace muestra un mensaje de aceptación y si no lo hace, de error.
                if (equipoDao.eliminar(equipo.getId())) {
                    Notification notification = Notification.show("Actor Borrado correctamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification notification = Notification.show("Actor no borrado");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
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
            }
        });
        vistaBoton.add(guardar, cancelar, eliminar);
        formLayout.add(textId, textNombre, textApellidos, textAnyo, textPais, vistaBoton);

        return formLayout;
    }

    /**
     * Se crea el buscador según el tipo de miembro.
     * @author Antonio Mas Esteve
     * @return Component
     */
    public Component crearBuscador(){
        ComboBox<Puesto> buscador = new ComboBox<>("Tipo");
        buscador.setItems(Puesto.values());
        buscador.setClearButtonVisible(true);
        buscador.addValueChangeListener(e->{
            List<Equipo> listaNueva = new ArrayList<>();
            if(e.getValue() != null){
                    if (e.getValue() == Puesto.ACTOR) {
                        listaNueva.addAll(equipoDao.listarActores());
                        tabla.setItems(listaNueva);
                        refrescarTabla();
                    }
                    if (e.getValue() == Puesto.DIRECTOR) {
                        listaNueva.addAll(equipoDao.listarDirectores());
                        tabla.setItems(listaNueva);
                        refrescarTabla();
                    }
            }
            else{
                rellenarTabla();
                refrescarTabla();
            }
        });
        return buscador;
    }

    /**
     * Crea el menú edición título+formulario
     * @return VeticalLayout
     * @author Antonio Mas Esteve
     */
    public VerticalLayout crearMenuEdicion() {
        VerticalLayout editor = new VerticalLayout();
        H3 titulo = new H3("Editar Actor");
        editor.add(crearBuscador(), crearFormulario());
        editor.setPadding(true);
        return editor;
    }

    /**
     * Método para rellenar la tabla desde el backend.
     * @author Antonio Mas Esteve
     */
    public void rellenarTabla() {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(equipoDao.listar());
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
