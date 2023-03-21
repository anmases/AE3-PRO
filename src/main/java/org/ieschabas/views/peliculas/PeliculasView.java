package org.ieschabas.views.peliculas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.ieschabas.clases.Actor;
import org.ieschabas.clases.Director;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.enums.Categoria;
import org.ieschabas.enums.Formato;
import org.ieschabas.enums.Valoracion;
import org.ieschabas.librerias.GestorPeliculas;
import org.ieschabas.views.MainView;

import javax.annotation.security.RolesAllowed;
import java.util.*;


/**
 * Vista de las peliculas
 *
 * @author Antonio Mas Esteve
 */
@PageTitle("Películas")
@Route(value = "Peliculas", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@RolesAllowed("ADMIN")
public class PeliculasView extends VerticalLayout {
    private Grid<Pelicula> tabla;
    private Dialog anyadirVentana;
    private VerticalLayout anyadirTabla = new VerticalLayout();
    private VerticalLayout anyadirPelicula = new VerticalLayout();

    /**
     * Constructor principal de la clase Películas.
     *
     * @author Antonio Mas Esteve
     */
    public PeliculasView() {
        setSizeFull();
        addClassName("Peliculas-View");

        add(tablaLayout(), formularioLayout(), ventanaLayout());

    }

    /**
     * Aquí se crea la ventana vacía emergente, que por defecto es invisible.
     *
     * @return Dialog
     * @author Antonio Mas Esteve
     */
    public Dialog ventanaLayout() {
        anyadirVentana = new Dialog();
        anyadirVentana.addDialogCloseActionListener(e -> {
            anyadirVentana.close();
            anyadirVentana.removeAll();
        });
        anyadirVentana.setResizable(true);
        return anyadirVentana;
    }

    /**
     * Aquí se rellena la ventana emergente con tablas y botones:
     *
     * @return VerticalLayout
     * @author Antonio Mas Esteve
     */
    public VerticalLayout rellenarVentana(Pelicula pelicula) {
        VerticalLayout ventana = new VerticalLayout();
        VerticalLayout ventanaTabla = new VerticalLayout();
        FormLayout ventanaFormulario = new FormLayout();
        ventanaFormulario.setVisible(false);
        HorizontalLayout botones = new HorizontalLayout();
        H4 tituloActores = new H4("Actores:");
        H4 tituloDirectores = new H4("Directores:");
        Button anyadir = new Button("Añadir");
        Button cancelar = new Button("Cancelar");
        anyadir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelar.addClickListener(click -> {
            anyadirVentana.removeAll();
            anyadirVentana.close();
        });
        anyadir.addClickListener(event -> {
            ventanaFormulario.setVisible(true);
            ventanaTabla.setVisible(false);
        });


/////////////////////////////////////////Creación del formulario Añadir/////////////////////////////////////////////////
        //Información para los actores:
        ArrayList<Actor> actoresSeleccionados = new ArrayList<>();   //Actores seleccionados en el Multiselect:
        ArrayList<Actor> listaActoresRestantes = GestorPeliculas.listarActoresNoRelacionados(pelicula.getId());   //Actores restantes:


        //Información para los directores:
        ArrayList<Director> directoresSeleccionados = new ArrayList<>();   //Directores seleccionados en el Multiselect:
        Collection<Director> listaDirectoresRestantes = GestorPeliculas.listarDirectoresNoRelacionados(pelicula.getId());   //Directores restantes:


        //Creamos los MultiSelect:
        HorizontalLayout botones2 = new HorizontalLayout();
        MultiSelectComboBox<Actor> opcionActor = new MultiSelectComboBox<>("Añadir actores a la película");
        opcionActor.setPlaceholder("Añada los actores");
        MultiSelectComboBox<Director> opcionDirector = new MultiSelectComboBox<>("Añadir directores a la película");
        opcionDirector.setPlaceholder("Añada los actores");
        opcionActor.setItems(listaActoresRestantes);
        opcionActor.setItemLabelGenerator(actor -> actor.getNombre() + " " + actor.getApellidos());
        opcionDirector.setItems(listaDirectoresRestantes);
        opcionDirector.setItemLabelGenerator(director -> director.getNombre() + " " + director.getApellidos());
///////////////////////////////////////Creamos Las tablas//////////////////////////////////////////////////////////////
        //Tabla de actores:
        Grid<Actor> tablaActores = new Grid<>(Actor.class, false);
        tablaActores.addColumn(Actor::getNombre).setHeader("Nombre").setAutoWidth(true);
        tablaActores.addColumn(Actor::getApellidos).setHeader("apellidos").setAutoWidth(true);
        tablaActores.addColumn(new ComponentRenderer<>(Button::new, ((button, actor) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            button.setIcon(new Icon(VaadinIcon.TRASH));
            button.addClickListener(event -> {
                GestorPeliculas.eliminarActorRelacionado(pelicula.getId(), actor.getId());
                //Rellenar la tabla:
                tablaActores.setItems(GestorPeliculas.listarActoresRelacionados(pelicula.getId()));
                //Refrescar la tabla:
                tablaActores.getDataProvider().refreshAll();
                //Rellenamos el multiselect;
                opcionActor.setItems(GestorPeliculas.listarActoresNoRelacionados(pelicula.getId()));
                //Refrescamos el multiSelect:
                opcionActor.getDataProvider().refreshAll();
            });
        }))).setHeader("Eliminar").setResizable(true).setAutoWidth(true);
        tablaActores.setItems(GestorPeliculas.listarActoresRelacionados(pelicula.getId()));
        tablaActores.recalculateColumnWidths();
        tablaActores.setHeightByRows(true);

        //Tabla de directores:
        Grid<Director> tablaDirectores = new Grid<>(Director.class, false);
        tablaDirectores.addColumn(Director::getNombre).setHeader("Nombre").setAutoWidth(true);
        tablaDirectores.addColumn(Director::getApellidos).setHeader("apellidos").setAutoWidth(true);
        tablaDirectores.addColumn(new ComponentRenderer<>(Button::new, ((button, director) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            button.setIcon(new Icon(VaadinIcon.TRASH));
            button.addClickListener(event -> {
                GestorPeliculas.eliminarDirectorRelacionado(pelicula.getId(), director.getId());
                //Rellenar la tabla:
                tablaDirectores.setItems(GestorPeliculas.listarDirectoresRelacionados(pelicula.getId()));
                //Refrescar la tabla:
                tablaDirectores.getDataProvider().refreshAll();
                //Rellenamos el multiselect;
                opcionDirector.setItems(GestorPeliculas.listarDirectoresNoRelacionados(pelicula.getId()));
                //Refrescamos el multiSelect:
                opcionDirector.getDataProvider().refreshAll();
            });
        }))).setHeader("Eliminar").setResizable(true).setAutoWidth(true);
        tablaDirectores.setItems(GestorPeliculas.listarDirectoresRelacionados(pelicula.getId()));
        tablaDirectores.recalculateColumnWidths();
        tablaDirectores.setHeightByRows(true);

        Button guardar = new Button("Guardar");
        Button atras = new Button("Atrás");
        atras.setWidthFull();
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.setWidthFull();
        guardar.addClickListener(event -> {
            if (opcionDirector.isEmpty() == false) {
                directoresSeleccionados.addAll(opcionDirector.getSelectedItems());
                GestorPeliculas.insertarDirectoresRelacionados(directoresSeleccionados, pelicula.getId());
            }
            if (opcionActor.isEmpty() == false) {
                actoresSeleccionados.addAll(opcionActor.getSelectedItems());
                GestorPeliculas.insertarActoresRelacionados(actoresSeleccionados, pelicula.getId());
            }
            //Rellenar la tabla:
            tablaActores.setItems(GestorPeliculas.listarActoresRelacionados(pelicula.getId()));
            tablaDirectores.setItems(GestorPeliculas.listarDirectoresRelacionados(pelicula.getId()));
            //Notificación:
            Notification notification = Notification.show("Se ha guardado correctamente");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            //Refrescar la tabla:
            tablaActores.getDataProvider().refreshAll();
            tablaDirectores.getDataProvider().refreshAll();
            //Rellenamos el multiselect;
            opcionActor.setItems(GestorPeliculas.listarActoresNoRelacionados(pelicula.getId()));
            opcionDirector.setItems(GestorPeliculas.listarDirectoresNoRelacionados(pelicula.getId()));
            //Refrescamos el multiSelect:
            opcionActor.getDataProvider().refreshAll();
            opcionDirector.getDataProvider().refreshAll();
            //Limpiamos el campo:
            opcionActor.clear();
            opcionDirector.clear();
        });
        atras.addClickListener(click -> {
            //Aquí irán los clear:
            opcionActor.clear();
            opcionDirector.clear();
            //Rellenar la tabla:
            tablaActores.setItems(GestorPeliculas.listarActoresRelacionados(pelicula.getId()));
            tablaDirectores.setItems(GestorPeliculas.listarDirectoresRelacionados(pelicula.getId()));
            //Refrescar la tabla:
            tablaActores.getDataProvider().refreshAll();
            tablaDirectores.getDataProvider().refreshAll();
            //Rellenamos el multiselect;
            opcionActor.setItems(GestorPeliculas.listarActoresNoRelacionados(pelicula.getId()));
            opcionDirector.setItems(GestorPeliculas.listarDirectoresNoRelacionados(pelicula.getId()));
            //Refrescamos el multiSelect:
            opcionActor.setItems(listaActoresRestantes);
            opcionDirector.setItems(listaDirectoresRestantes);
            //Volvemos a la tabla:
            ventanaFormulario.setVisible(false);
            ventanaTabla.setVisible(true);
        });

        botones.add(anyadir, cancelar);
        botones2.add(guardar, atras);
        ventanaFormulario.add(opcionActor, opcionDirector, botones2);
        ventanaTabla.add(tituloActores, tablaActores, tituloDirectores, tablaDirectores, botones);
        ventana.add(ventanaTabla, ventanaFormulario);
        return ventana;
    }

    /**
     * Crea la disposición de la tabla y sus distintos ecomponentes.
     *
     * @return VerticalLayout
     * @author Antonio Mas Esteve
     */
    public VerticalLayout tablaLayout() {
//////////////////////////////////////Se crea el botón añadir y el título////////////////////////////////////////////////
        HorizontalLayout anyadirBoton = new HorizontalLayout();
        Button botonanyadir = new Button("Añadir Película");
        botonanyadir.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        botonanyadir.setIcon(new Icon(VaadinIcon.PLUS));
        botonanyadir.addClickListener(ClickEvent -> {
            anyadirPelicula.setVisible(true);
            anyadirTabla.setVisible(false);
        });
        anyadirBoton.setVisible(true);
        anyadirBoton.add(botonanyadir);
        H3 tituloVista = new H3("Lista de Películas");
        anyadirTabla.add(anyadirBoton, tituloVista, crearBuscador(), crearTabla());
        anyadirTabla.setVisible(true);
        return anyadirTabla;
    }


    /**
     * Método que crea el formulario que buscará las peliculas.
     *
     * @return Component
     * @author Antonio Mas Esteve
     */
    public Component crearBuscador() {
        FormLayout panelBuscador = new FormLayout();
        //Creamos los campos:
        TextField textTitulo = new TextField("Título");
        textTitulo.setValue("");
        IntegerField textAnyo = new IntegerField("Año");
        ComboBox<Categoria> textCategoria = new ComboBox<>("Categoría");
        ComboBox<Formato> textFormato = new ComboBox<>("Formato");
        ComboBox<Valoracion> textValoracion = new ComboBox<>("Estrellas");
        addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                LumoUtility.BoxSizing.BORDER);
        //Rellenamos los comboBox:
        textCategoria.setItems(Categoria.values());
        textFormato.setItems(Formato.values());
        textValoracion.setItems(Valoracion.values());
        //Añadimos los títulos:
        textTitulo.setPlaceholder("buscar por título");
        textAnyo.setPlaceholder("buscar por año");
        textCategoria.setPlaceholder("buscar por categoría");
        textFormato.setPlaceholder("buscar por formato");
        textValoracion.setPlaceholder("buscar por valoración");
        //Añadimos a cada uno botón para borrar lo escrito:
        textTitulo.setClearButtonVisible(true);
        textAnyo.setClearButtonVisible(true);
        textCategoria.setClearButtonVisible(true);
        textFormato.setClearButtonVisible(true);
        textValoracion.setClearButtonVisible(true);

        //Agregamos un escuchador de escritura para los campos:
        textTitulo.setValueChangeMode(ValueChangeMode.EAGER);
        textTitulo.addValueChangeListener(e -> {
            String tituloPelicula = e.getValue().toLowerCase();
            ArrayList<Pelicula> listaActualizada = new ArrayList<>();
            Collection<Pelicula> lista;
            lista = GestorPeliculas.listarPeliculas();
            Pelicula pelicula;
            for (Pelicula valor : lista) {
                pelicula = valor;
                if (pelicula.getTitulo().toLowerCase().contains(tituloPelicula)) {
                    listaActualizada.add(pelicula);
                }
            }
            tabla.setItems(listaActualizada);
            refrescarTabla();
        });
        textAnyo.setValueChangeMode(ValueChangeMode.EAGER);
        textAnyo.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                //Es necesario convertirlo a String porque solo así se comprueba si "contiene" un caracter.
                String anyoEstreno = e.getValue().toString();
                ArrayList<Pelicula> listaActualizada = new ArrayList<>();
                Collection<Pelicula> lista;
                lista = GestorPeliculas.listarPeliculas();
                Pelicula pelicula;
                for (Pelicula valor : lista) {
                    pelicula = valor;
                    String getAnyo = String.valueOf(pelicula.getAnyoPublicacion());
                    if (getAnyo.contains(anyoEstreno)) {
                        listaActualizada.add(pelicula);
                    }
                }
                tabla.setItems(listaActualizada);
                refrescarTabla();
            } else {
                rellenarTabla();
                refrescarTabla();
            }

        });
        textCategoria.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                Categoria categoria = e.getValue();
                ArrayList<Pelicula> listaActualizada = new ArrayList<>();
                Collection<Pelicula> lista;
                lista = GestorPeliculas.listarPeliculas();
                Pelicula pelicula;
                for (Pelicula valor : lista) {
                    pelicula = valor;
                    if (pelicula.getCategoria() == categoria) {
                        listaActualizada.add(pelicula);
                    }
                }
                tabla.setItems(listaActualizada);
                refrescarTabla();
            } else {
                rellenarTabla();
                refrescarTabla();
            }
        });
        textFormato.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                Formato formato = e.getValue();
                ArrayList<Pelicula> listaActualizada = new ArrayList<>();
                Collection<Pelicula> lista;
                lista = GestorPeliculas.listarPeliculas();
                Pelicula pelicula;
                for (Pelicula valor : lista) {
                    pelicula = valor;
                    if (pelicula.getFormato() == formato) {
                        listaActualizada.add(pelicula);
                    }
                }
                tabla.setItems(listaActualizada);
                refrescarTabla();
            } else {
                rellenarTabla();
                refrescarTabla();
            }
        });
        textValoracion.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                Valoracion valoracion = e.getValue();
                ArrayList<Pelicula> listaActualizada = new ArrayList<>();
                Collection<Pelicula> lista;
                lista = GestorPeliculas.listarPeliculas();
                Pelicula pelicula;
                for (Pelicula valor : lista) {
                    pelicula = valor;
                    if (pelicula.getValoracion() == valoracion) {
                        listaActualizada.add(pelicula);
                    }
                }
                tabla.setItems(listaActualizada);
                refrescarTabla();
            } else {
                rellenarTabla();
                refrescarTabla();
            }
        });

        panelBuscador.add(textTitulo, textAnyo, textCategoria, textFormato, textValoracion);
        return panelBuscador;
    }


    /**
     * Método que crea la tabla y le da formato.
     *
     * @return tabla
     * @author antonio Mas Esteve
     */
    public Component crearTabla() {
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
        tabla.addColumn(new ComponentRenderer<>(Button::new, ((boton, pelicula) -> {
            boton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
            boton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
            boton.addClickListener(event -> {
                //Aquí es donde se rellena la ventana emergente y además se hace visible:
                anyadirVentana.add(rellenarVentana(pelicula));
                anyadirVentana.open();
            });
        }))).setHeader("Equipo").setResizable(true).setAutoWidth(true);
        Grid.Column<Pelicula> campoBotonEditar = tabla.addComponentColumn(pelicula -> {
            Button editButton = new Button();
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(Event -> tabla.getEditor().editItem(pelicula));
            return editButton;
        }).setHeader("Editar").setResizable(true).setWidth("150px");

        tabla.addColumn(new ComponentRenderer<>(Button::new, ((button, pelicula) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            button.setIcon(new Icon(VaadinIcon.TRASH));
            button.addClickListener(event -> {
                //Elimina la película y las relaciones asociadas;
                GestorPeliculas.eliminarPelicula(pelicula.getId());
                if (GestorPeliculas.eliminarPelicula(pelicula.getId())) {
                    Notification notification = Notification.show("película borrada correcamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification notification = Notification.show("película no fue borrada");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
                rellenarTabla();

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
            GestorPeliculas.modificarPelicula(pelicula);
            if (GestorPeliculas.modificarPelicula(pelicula)) {
                Notification notification = Notification.show("película modificada");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification notification = Notification.show("película no fue modificada");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
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
     *
     * @author Antonio Mas Esteve
     */
    public void refrescarTabla() {
        tabla.getDataProvider().refreshAll();
    }

    /**
     * Método que carga los datos que obtiene el backend desde
     * el fichero en una colección local y rellena la tabla:
     *
     * @author Antonio Mas Esteve
     */
    public void rellenarTabla() {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(GestorPeliculas.listarPeliculas());
    }


    /**
     * Creación de la disposición vertical formulario añadir película:
     *
     * @return VerticalLayout
     * @author Antonio Mas Esteve
     */
    public VerticalLayout formularioLayout() {
        HorizontalLayout botones = new HorizontalLayout();
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

            if (titulo.getValue() != null && descripcion.getValue() != null && anyoPublicacion.getValue() != null && duracion.getValue() != null && categoria.getValue() != null && formato.getValue() != null) {
                //Añade al fichero el contenido del formulario:
                Pelicula pelicula = new Pelicula(0, titulo.getValue(), descripcion.getValue(), anyoPublicacion.getValue(), duracion.getValue(), categoria.getValue(), formato.getValue(), valoracion.getValue());
                GestorPeliculas.insertarPelicula(pelicula);
                //Se muestra la notificación:
                Notification notification = Notification.show("película guardada correcamente");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                //Se muestra la notificación:
                Notification notification = Notification.show("Error: Todos los campos deben rellenarse");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            //Se rellena la tabla:
            rellenarTabla();
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
        botones.add(guardar, cancelar);
        //Se añaden al formulario:
        formLayout.add(titulo, descripcion, anyoPublicacion, duracion, categoria, formato, valoracion, botones);
        //Lo añadimos a la vista:
        anyadirPelicula.add(formLayout);
        anyadirPelicula.setVisible(false);
        return anyadirPelicula;
    }
}
