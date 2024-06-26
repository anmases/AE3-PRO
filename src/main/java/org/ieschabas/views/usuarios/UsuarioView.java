package org.ieschabas.views.usuarios;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.ieschabas.backend.model.Usuario;
import org.ieschabas.backend.services.UserService;
import org.ieschabas.backend.enums.Rol;
import org.ieschabas.views.MainView;

import javax.annotation.security.RolesAllowed;
import java.io.Serial;

/**
 * Vista de los Usuarios
 *
 * @author Antonio Mas Esteve
 */
@PageTitle("Usuarios")
@Route(value = "usuarios", layout = MainView.class)
@RolesAllowed("ADMIN")
public class UsuarioView extends VerticalLayout {
    @Serial
    private static final long serialVersionUID = -2553389613089065660L;
    private final UserService userService;

    private Grid<Usuario> tabla;

    /**
     * Constructor principal de la vista de usuarios.
     * Aquí se inyectan las dependencias de UserService mediante SpringBoot IoC
     */
    public UsuarioView(UserService userService){
        this.userService = userService;
        setSizeFull();
        addClassName("Usuarios-View");
        add(crearTabla());

    }

    /**
     * Método que crea la tabla y el editor.
     * @author Antonio Mas Esteve
     * @return Component
     */
    private Component crearTabla(){
        tabla = new Grid<>(Usuario.class, false);
        Editor<Usuario> editor = tabla.getEditor();
        Binder<Usuario> binder = new Binder<>(Usuario.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        tabla.setAllRowsVisible(true);
        //Creamos los campos:
        Grid.Column<Usuario> campoId = tabla.addColumn(Usuario::getId).setHeader("Id").setAutoWidth(true);
        Grid.Column<Usuario> campoNombre = tabla.addColumn(Usuario::getNombre).setHeader("Nombre").setAutoWidth(true).setResizable(true);
        Grid.Column<Usuario> campoApellidos = tabla.addColumn(Usuario::getApellidos).setHeader("Apellidos").setAutoWidth(true).setResizable(true);
        Grid.Column<Usuario> campoDireccion = tabla.addColumn(Usuario::getDireccion).setHeader("Dirección").setAutoWidth(true).setResizable(true);
        Grid.Column<Usuario> campoActivo = tabla.addColumn(new ComponentRenderer<>(usuario -> {
            Span span = new Span(usuario.getActivo() ? "Activo":"Inactivo");
            span.getElement().setAttribute("theme", usuario.getActivo() ? "badge success" : "badge error");
            return span;
        })).setHeader("Estado").setAutoWidth(true).setResizable(true);
        Grid.Column<Usuario> campoFecha = tabla.addColumn(Usuario::getFecha_registro).setHeader("registro").setAutoWidth(true).setResizable(true);
        Grid.Column<Usuario> campoRol = tabla.addColumn(new ComponentRenderer<>(usuario -> {
            Span span = new Span();
            span.setText(usuario.getRol().toString());
            String estiloAdmin ="";
            if(usuario.getRol() == Rol.ADMIN){
                estiloAdmin = "primary";
            }
            span.getElement().setAttribute("theme", "badge "+estiloAdmin);
            return span;
        })).setHeader("Rol").setAutoWidth(true).setResizable(true);
        //Botón eliminar:
        tabla.addColumn(new ComponentRenderer<>(Button::new, ((button, usuario) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            button.setIcon(new Icon(VaadinIcon.TRASH));
            button.addClickListener(event -> {
                //Elimina la película y las relaciones asociadas;
                if (userService.remove(usuario.getId())) {
                    Notification notification = Notification.show("Usuario borrado correcamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification notification = Notification.show("El usuario no fue borrado");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
                rellenarTabla();

                refrescarTabla();
            });
        }))).setHeader("Borrar").setResizable(true).setAutoWidth(true);
        //Botón editar:
        Grid.Column<Usuario> botonEditar = tabla.addComponentColumn(usuario -> {
            Button editButton = new Button();
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(Event -> tabla.getEditor().editItem(usuario));
            return editButton;
        }).setHeader("Editar").setResizable(true).setWidth("150px");

//Configuramos el editor:
        IntegerField textoId = new IntegerField();
        textoId.setEnabled(false);
        textoId.setWidthFull();
        binder.forField(textoId).bind(Usuario::getId, Usuario::setId);
        campoId.setEditorComponent(textoId);

        TextField textoNombre = new TextField();
        textoNombre.setWidthFull();
        binder.forField(textoNombre).asRequired("El campo no puede estar vacío").bind(Usuario::getNombre, Usuario::setNombre);
        campoNombre.setEditorComponent(textoNombre);

        TextField textoApellidos = new TextField();
        textoApellidos.setWidthFull();
        binder.forField(textoApellidos).asRequired("El campo no puede estar vacío").bind(Usuario::getApellidos, Usuario::setApellidos);
        campoApellidos.setEditorComponent(textoApellidos);

        TextField textoDireccion = new TextField();
        textoDireccion.setWidthFull();
        binder.forField(textoDireccion).asRequired("El campo no puede estar vacío").bind(Usuario::getDireccion, Usuario::setDireccion);
        campoDireccion.setEditorComponent(textoDireccion);

        //Este es para un campo boolean:
        Checkbox textoActivo = new Checkbox();
        textoActivo.setWidthFull();
        binder.forField(textoActivo).asRequired("El campo no puede estar vacío").bind(Usuario::getActivo, Usuario::setActivo);
        campoActivo.setEditorComponent(textoActivo);
        textoActivo.setLabel("Activo");
        textoActivo.setValue(false);
        textoActivo.setIndeterminate(false);

        DatePicker textoFecha = new DatePicker();
        textoFecha.setEnabled(false);
        textoFecha.setWidthFull();
        binder.forField(textoFecha).asRequired("El campo no puede estar vacío").bind(Usuario::getFecha_registro, Usuario::setFecha_registro);
        campoFecha.setEditorComponent(textoFecha);

        ComboBox<Rol> textoRol = new ComboBox<>();
        textoRol.setEnabled(false);
        textoRol.setWidthFull();
        binder.forField(textoRol).asRequired("El campo no puede estar vacío").bind(Usuario::getRol, Usuario::setRol);
        campoRol.setEditorComponent(textoRol);
        textoRol.setItems(Rol.values());

        Button botonGuardar = new Button();
        botonGuardar.addClickListener(e -> {
            //Se rellenan todos los campos del objeto seleccionado:
            Usuario usuario = userService.findById(textoId.getValue());
            usuario.setNombre(textoNombre.getValue());
            usuario.setApellidos(textoApellidos.getValue());
            usuario.setDireccion(textoDireccion.getValue());
            usuario.setActivo(textoActivo.getValue());
           if (userService.update(usuario)) {
                Notification notification = Notification.show("Usuario modificado");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification notification = Notification.show("El usuario no fue modificado");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            //Forzamos el editor a cerrarse pase lo que pase:
            editor.cancel();
           rellenarTabla();
           refrescarTabla();
        });
        botonGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        botonGuardar.setIcon(new Icon(VaadinIcon.ADD_DOCK));

        Button botonCancelar = new Button();
        botonCancelar.addClickListener(e -> editor.cancel());
        botonCancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        botonCancelar.setIcon(new Icon(VaadinIcon.CLOSE));
        //La disposición con los dos botones:
        HorizontalLayout acciones = new HorizontalLayout(botonGuardar, botonCancelar);
        acciones.setWidth("400px");

        //Se añade el componente editor:
        botonEditar.setEditorComponent(acciones);
        editor.addCancelListener(e -> {
        });
        rellenarTabla();
        refrescarTabla();
        return tabla;
    }
    /**
     * Método para refrescar la tabla:
     *
     * @author Antonio Mas Esteve
     */
    public void refrescarTabla() {
        tabla.getDataProvider().refreshAll();
    }
    /**
     * Método que carga los datos que obtiene datos del backend desde
     * y rellena la tabla:
     *
     * @author Antonio Mas Esteve
     */
    public void rellenarTabla() {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(userService.findAll());
    }


}
