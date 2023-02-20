package org.ieschabas.views.películas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H3;
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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.enums.Categoria;
import org.ieschabas.enums.Formato;
import org.ieschabas.enums.Valoracion;
import org.ieschabas.librerias.GestorPeliculas;
import org.ieschabas.views.MainLayout;

import java.io.File;
import java.io.IOException;


@PageTitle("Películas")
@Route(value = "Peliculas", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class PeliculasView extends VerticalLayout {
    private Grid<Pelicula> tabla;
    private Button botonanyadir;
    private HorizontalLayout anyadirBoton = new HorizontalLayout();
    private VerticalLayout anyadirTabla = new VerticalLayout();
    private VerticalLayout anyadirPelicula = new VerticalLayout();

    /**
     * Constructor principal de la clase
     *
     * @throws IOException
     * @author Antonio Mas Esteve
     */
    public PeliculasView() throws IOException {
        setSizeFull();
        addClassName("Peliculas-View");

        add(tablaLayout(), formularioLayout());

    }

    /**
     * Crea la disposición de la tabla y sus distintos ecomponentes.
     *
     * @return
     * @throws IOException
     * @author Antonio Mas Esteve
     */
    public VerticalLayout tablaLayout() throws IOException {
        anyadirTabla.add(botonLayout(), crearTitulo(), crearBuscador(), crearTabla());

        anyadirTabla.setVisible(true);
        return anyadirTabla;
    }

    /**
     * Disposición horizontal del botón añadir película que abre el formulario
     *
     * @return
     * @author Antonio Mas Esteve
     */
    public HorizontalLayout botonLayout() {
        botonanyadir = new Button("Añadir Película");
        botonanyadir.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        botonanyadir.setIcon(new Icon(VaadinIcon.PLUS));
        botonanyadir.addClickListener(ClickEvent -> {
            anyadirPelicula.setVisible(true);
            anyadirTabla.setVisible(false);

        });
        anyadirBoton.setVisible(true);
        anyadirBoton.add(botonanyadir);
        return anyadirBoton;
    }

    /**
     * Método que buscará las películas.
     *
     * @return
     * @author Antonio Mas Esteve
     */
    public Component crearBuscador() {
        FormLayout panelBuscador = new FormLayout();
        IntegerField identificador = new IntegerField("id");
        TextField titulo = new TextField("Título");
        TextField anyo = new TextField("Año");
        ComboBox<String> categorias = new ComboBox<>("Categoría");
        ComboBox<String> formatos = new ComboBox<>("Formato");
        ComboBox<String> valoraciones = new ComboBox<>("Estrellas");

        setWidthFull();
        addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                LumoUtility.BoxSizing.BORDER);
        categorias.setItems("ACCION", "AVENTURAS", "CIENCIA FICCION", "COMEDIA", "DOCUMENTAL", "DRAMA", "FANTASIA", "MUSICAL", "SUSPENSE", "TERROR", "BÉLICA");
        formatos.setItems("XVID", "DIVX", "MP4", "H264", "FLV");
        valoraciones.setItems("UNA", "DOS", "TRES", "CUATRO", "CINCO");
        identificador.setPlaceholder("buscar por id");
        titulo.setPlaceholder("buscar por título");
        anyo.setPlaceholder("buscar por año");
        categorias.setPlaceholder("buscar por categoría");
        formatos.setPlaceholder("buscar por formato");
        valoraciones.setPlaceholder("buscar por valoración");
        identificador.setClearButtonVisible(true);
        titulo.setClearButtonVisible(true);
        anyo.setClearButtonVisible(true);
        categorias.setClearButtonVisible(true);
        formatos.setClearButtonVisible(true);
        valoraciones.setClearButtonVisible(true);

        Button reset = new Button("Cancelar");
        reset.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        reset.addClickListener(clickEvent -> {
            identificador.clear();
        });
        Button buscar = new Button("Buscar");
        buscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buscar.addClickListener(clickEvent -> {
            identificador.getValue();

        });

        panelBuscador.add(identificador, titulo, anyo, categorias, formatos, valoraciones, reset, buscar);
        return panelBuscador;
    }

    /**
     * Título de la lista
     *
     * @return
     */
    public Component crearTitulo() {
        return new H3("Lista de Películas");
    }

    /**
     * Método que crea la tabla y le da formato.
     *
     * @return tabla
     */
    public Component crearTabla() throws IOException {
        tabla = new Grid<>(Pelicula.class, false);
        Editor<Pelicula> editor = tabla.getEditor();
        Binder<Pelicula> binder = new Binder<>(Pelicula.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        tabla.setAllRowsVisible(true);

        Grid.Column<Pelicula> campoId = tabla.addColumn(Pelicula::getId).setHeader("Id").setAutoWidth(true);
        Grid.Column<Pelicula> campoTitulo = tabla.addColumn(Pelicula::getTitulo).setHeader("Título").setAutoWidth(true).setResizable(true);
        Grid.Column<Pelicula> campoDescripcion = tabla.addColumn(Pelicula::getDescripcion).setHeader("Descripción").setResizable(true);
        Grid.Column<Pelicula> campoAnyoPublicacion = tabla.addColumn(Pelicula::getAnyoPublicacion).setHeader("Año").setAutoWidth(true).setResizable(true);
        Grid.Column<Pelicula> campoDuracion = tabla.addColumn(Pelicula::getDuracion).setHeader("Duración").setAutoWidth(true).setResizable(true);
        Grid.Column<Pelicula> campoCategoria = tabla.addColumn(Pelicula::getCategoria).setHeader("Categoría").setAutoWidth(true).setResizable(true);
        Grid.Column<Pelicula> campoFormato = tabla.addColumn(Pelicula::getFormato).setHeader("Formato").setAutoWidth(true).setResizable(true);
        Grid.Column<Pelicula> campoValoracion = tabla.addColumn(Pelicula::getValoracion).setHeader("Estrellas").setAutoWidth(true).setResizable(true);
        Grid.Column<Pelicula> campoBotonEditar = tabla.addComponentColumn(pelicula -> {
            Button editButton = new Button();
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(Event -> {
                tabla.getEditor().editItem(pelicula);
            });
            return editButton;
        }).setHeader("Editar").setResizable(true).setWidth("150px");

        tabla.addColumn(new ComponentRenderer<>(Button::new, ((button, pelicula) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            button.setIcon(new Icon(VaadinIcon.TRASH));
            button.addClickListener(event -> {
                try {
                    GestorPeliculas.borrarPelicula(pelicula.getId());
                    if (GestorPeliculas.borrarPelicula(pelicula.getId()) == true) {
                        Notification notification = Notification.show("película borrada correcamente");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        Notification notification = Notification.show("película no fue borrada");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    rellenarTabla();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                refrescarTabla();
            });
        }))).setHeader("Eliminar").setResizable(true).setAutoWidth(true);


        //Creamos los campos de texto del editor:
        IntegerField textoId = new IntegerField();
        textoId.setEnabled(false);
        textoId.setWidthFull();
        binder.forField(textoId).bind(Pelicula::getId, Pelicula::setId);
        campoId.setEditorComponent(textoId);

        TextField textoTitulo = new TextField();
        textoTitulo.setWidthFull();
        binder.forField(textoTitulo).asRequired("El campo no puede estar vacío").bind(Pelicula::getTitulo, Pelicula::setTitulo);
        campoTitulo.setEditorComponent(textoTitulo);

        TextField textoDescripcion = new TextField();
        textoDescripcion.setWidthFull();
        binder.forField(textoDescripcion).asRequired("El campo no puede estar vacío").bind(Pelicula::getDescripcion, Pelicula::setDescripcion);
        campoDescripcion.setEditorComponent(textoDescripcion);

        IntegerField textoAnyoPublicacion = new IntegerField();
        textoAnyoPublicacion.setWidthFull();
        binder.forField(textoAnyoPublicacion).asRequired("El campo no puede estar vacío").bind(Pelicula::getAnyoPublicacion, Pelicula::setAnyoPublicacion);
        campoAnyoPublicacion.setEditorComponent(textoAnyoPublicacion);

        TextField textoDuracion = new TextField();
        textoDuracion.setWidthFull();
        binder.forField(textoDuracion).asRequired("El campo no puede estar vacío").bind(Pelicula::getDuracion, Pelicula::setDuracion);
        campoDuracion.setEditorComponent(textoDuracion);

        ComboBox<Categoria> textoCategoria = new ComboBox<>();
        textoDuracion.setWidthFull();
        binder.forField(textoCategoria).asRequired("El campo no puede estar vacío").bind(Pelicula::getCategoria, Pelicula::setCategoria);
        campoCategoria.setEditorComponent(textoCategoria);
        textoCategoria.setItems(Categoria.values());

        ComboBox<Formato> textoFormato = new ComboBox<>();
        textoDuracion.setWidthFull();
        binder.forField(textoFormato).asRequired("El campo no puede estar vacío").bind(Pelicula::getFormato, Pelicula::setFormato);
        campoFormato.setEditorComponent(textoFormato);
        textoFormato.setItems(Formato.values());

        ComboBox<Valoracion> textoValoracion = new ComboBox<>();
        textoDuracion.setWidthFull();
        binder.forField(textoValoracion).asRequired("El campo no puede estar vacío").bind(Pelicula::getValoracion, Pelicula::setValoracion);
        campoValoracion.setEditorComponent(textoValoracion);
        textoValoracion.setItems(Valoracion.values());


        Button botonGuardar = new Button();
        botonGuardar.addClickListener(e -> {
            Pelicula pelicula;
            editor.save();
            pelicula = new Pelicula(textoId.getValue(), textoTitulo.getValue(), textoDescripcion.getValue(), textoAnyoPublicacion.getValue(), textoDuracion.getValue(), textoCategoria.getValue(), textoFormato.getValue(), textoValoracion.getValue());
            try {
                GestorPeliculas.modificarPelicula(pelicula);
                if (GestorPeliculas.modificarPelicula(pelicula)){
                    Notification notification = Notification.show("película modificada");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification notification = Notification.show("película no fue modificada");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        botonGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        botonGuardar.setIcon(new Icon(VaadinIcon.ADD_DOCK));

        Button botonCancelar = new Button();
        botonCancelar.addClickListener(e->editor.cancel());
        botonCancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        botonCancelar.setIcon(new Icon(VaadinIcon.CLOSE));
        //La disposición con los dos botones:
        HorizontalLayout acciones = new HorizontalLayout(botonGuardar, botonCancelar);
        acciones.setWidth("400px");

        //Se añade el componente editor:
        campoBotonEditar.setEditorComponent(acciones);
        editor.addCancelListener(e -> {
        });
        //Rellenamos y refrescamos la tabla:
        rellenarTabla();
        refrescarTabla();
        tabla.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        return tabla;
    }

    /**
     * Método para refrescar la tabla:
     */
    public void refrescarTabla() {
        tabla.getDataProvider().refreshAll();
    }

    /**
     * Método que carga los datos que obtiene el backend desde
     * el fichero en una colección local y rellena la tabla:
     *
     * @throws IOException
     */
    public void rellenarTabla() throws IOException {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(GestorPeliculas.listarPeliculas().values());
    }


    /**
     * Creación de la disposición vertical formulario añadir película:
     *
     * @return
     * @author Antonio Mas Esteve
     */
    public VerticalLayout formularioLayout() {

        anyadirPelicula.setSizeFull();
        //se crea el título:
        H3 encabezado = new H3("Añadir nueva película");
        anyadirPelicula.add(encabezado);

        //Se crean los campos de texto para el formulario:
        TextField titulo = new TextField("Título");
        TextField descripcion = new TextField("Descripcion");
        IntegerField anyoPublicacion = new IntegerField("Año de estreno");
        TextField duracion = new TextField("Duración (min)");
        ComboBox<Categoria> categoria = new ComboBox<>("Categoría");
        ComboBox<Formato> formato = new ComboBox<>("Formato");
        ComboBox<Valoracion> valoracion = new ComboBox<>("Estrellas");
        //Se crean los botones:
        Button cancelar = new Button("Cancelar");
        Button guardar = new Button("Guardar");
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //Creamos el formulario:
        FormLayout formLayout = new FormLayout();

        categoria.setItems(Categoria.values());
        formato.setItems(Formato.values());
        valoracion.setItems(Valoracion.values());

        titulo.setPlaceholder("añada un título");
        descripcion.setPlaceholder("escriba una breve reseña");
        anyoPublicacion.setPlaceholder("introduzca el año de estreno");
        duracion.setPlaceholder("minutos de duración");
        categoria.setPlaceholder("seleccione la categoría");
        formato.setPlaceholder("seleccione el formato");
        valoracion.setPlaceholder("seleccione la clasificación");
        titulo.setClearButtonVisible(true);
        descripcion.setClearButtonVisible(true);
        anyoPublicacion.setClearButtonVisible(true);
        duracion.setClearButtonVisible(true);
        categoria.setClearButtonVisible(true);
        formato.setClearButtonVisible(true);
        valoracion.setClearButtonVisible(true);

        cancelar.addClickListener(e -> {
            //volver a la tabla:
            anyadirPelicula.setVisible(false);
            anyadirTabla.setVisible(true);
            //Se vacía el formulario
            titulo.clear();
            descripcion.clear();
            anyoPublicacion.clear();
            duracion.clear();
            categoria.clear();
            formato.clear();
            valoracion.clear();
            //Se muestra la notificación:
            Notification notification = Notification.show("No se ha añadido ninguna película");
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

        });
        guardar.addClickListener(e -> {

            if (titulo.getValue() != null && descripcion.getValue() != null && anyoPublicacion.getValue() != null && duracion.getValue() != null && categoria.getValue() != null && formato.getValue() != null && valoracion != null) {
                //Añade al fichero el contenido del formulario:
                try {
                    GestorPeliculas.anyadirPelicula(titulo.getValue(), descripcion.getValue(), anyoPublicacion.getValue(), duracion.getValue(), categoria.getValue(), formato.getValue(), valoracion.getValue());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //Se muestra la notificación:
                Notification notification = Notification.show("película guardada correcamente");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                //Se muestra la notificación:
                Notification notification = Notification.show("Error: Todos los campos deben rellenarse");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            //Se rellena la tabla:
            try {
                rellenarTabla();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //se refresca la tabla:
            refrescarTabla();
            //vuelve a la tabla:
            anyadirPelicula.setVisible(false);
            anyadirTabla.setVisible(true);
            //Se vacía el formulario:
            titulo.clear();
            descripcion.clear();
            anyoPublicacion.clear();
            duracion.clear();
            categoria.clear();
            formato.clear();
            valoracion.clear();

        });
        //Se añaden al formulario:
        formLayout.add(titulo, descripcion, anyoPublicacion, duracion, categoria, formato, valoracion, cancelar, guardar);
        //Lo añadimos a la vista:
        anyadirPelicula.add(formLayout);
        anyadirPelicula.setVisible(false);
        return anyadirPelicula;
    }
}
