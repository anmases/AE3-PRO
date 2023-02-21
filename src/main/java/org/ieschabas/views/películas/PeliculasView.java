package org.ieschabas.views.películas;

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
import org.ieschabas.librerias.GestorActores;
import org.ieschabas.librerias.GestorDirectores;
import org.ieschabas.librerias.GestorPeliculas;
import org.ieschabas.views.MainLayout;
import java.io.IOException;
import java.util.*;



@PageTitle("Películas")
@Route(value = "Peliculas", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class PeliculasView extends VerticalLayout {
    private Grid<Pelicula> tabla;
    private Button botonanyadir;
    private Dialog anyadirVentana;
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

        add(tablaLayout(), formularioLayout(), ventanaLayout());

    }

    /**
     * Aquí se crea la ventana vacía, que por defecto es invisible.
     * @return
     */
    public Dialog ventanaLayout(){
        anyadirVentana = new Dialog();
        anyadirVentana.addDialogCloseActionListener(e->{
            anyadirVentana.close();
            anyadirVentana.removeAll();
        });
        anyadirVentana.setResizable(true);
        return anyadirVentana;
    }

    /**
     * Aquí se rellena la ventana emergente con tablas y botones:
     * @param pelicula
     * @return
     */
    public VerticalLayout rellenarVentana(Pelicula pelicula) throws IOException {
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
        cancelar.addClickListener(click->{
                anyadirVentana.removeAll();
                anyadirVentana.close();
        });
        anyadir.addClickListener(event ->{
            ventanaFormulario.setVisible(true);
            ventanaTabla.setVisible(false);
        });
        //Tabla de actores:
        Grid<Actor> tablaActores = new Grid<>(Actor.class, false);
        tablaActores.addColumn(Actor::getNombre).setHeader("Nombre").setAutoWidth(true);
        tablaActores.addColumn(Actor::getApellidos).setHeader("apellidos").setAutoWidth(true);
        tablaActores.addColumn(new ComponentRenderer<>(Button::new, ((button, actor) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            button.setIcon(new Icon(VaadinIcon.TRASH));
            button.addClickListener(event -> {
                try {
                    GestorPeliculas.desvincularActor(pelicula.getId(), actor.getId());
                    //Rellenar la tabla:
                    tablaActores.setItems(GestorPeliculas.buscarActoresRelacionados(pelicula));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //Refrescar la tabla:
                tablaActores.getDataProvider().refreshAll();
            });
        }))).setHeader("Eliminar").setResizable(true).setAutoWidth(true);
        tablaActores.setItems(GestorPeliculas.buscarActoresRelacionados(pelicula));
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
                try {
                    GestorPeliculas.desvincularDirector(pelicula.getId(), director.getId());
                    //Rellenar la tabla:
                    tablaDirectores.setItems(GestorPeliculas.buscarDirectoresRelacionados(pelicula));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //Refrescar la tabla:
                tablaDirectores.getDataProvider().refreshAll();
            });
        }))).setHeader("Eliminar").setResizable(true).setAutoWidth(true);
        tablaDirectores.setItems(GestorPeliculas.buscarDirectoresRelacionados(pelicula));
        tablaDirectores.recalculateColumnWidths();
        tablaDirectores.setHeightByRows(true);
        /**************Creación del formulario Añadir*****************/
        //Información para los actores:
        ArrayList<Actor> actoresSeleccionados = new ArrayList<>();   //Actores seleccionados en el Multiselect:
        Collection<Actor> listaActores = GestorActores.listarActores().values();   //Todos los actores:
        ArrayList<Actor> actoresPresentes = GestorPeliculas.buscarActoresRelacionados(pelicula); //Actores que ya tiene
        //Que elimine de la lista los actores que ya están:
        Iterator<Actor> iteradorActores;
        Iterator<Actor> iteradorPresentes = actoresPresentes.iterator();
        Actor actorGeneral;
        Actor actorPresente;
        while(iteradorPresentes.hasNext()){
            iteradorActores = listaActores.iterator();
           actorPresente = iteradorPresentes.next();
          while(iteradorActores.hasNext()){
              actorGeneral = iteradorActores.next();
              if(actorPresente.getId() == actorGeneral.getId()){
                  //Si no lo contiene, lo elimina:
                  iteradorActores.remove();
              }
          }
        }

        //Información para los directores:
        //Información para los actores:
        ArrayList<Director> directoresSeleccionados = new ArrayList<>();   //Directores seleccionados en el Multiselect:
        Collection<Director> listaDirectores = GestorDirectores.listarDirectores().values();   //Todos los directores:
        ArrayList<Director> directoresPresentes = GestorPeliculas.buscarDirectoresRelacionados(pelicula); //Directores que ya tiene
        //Que elimine de la lista los actores que ya están:
        Iterator<Director> iteradorDirectores;
        Iterator<Director> iteradorDirPresentes = directoresPresentes.iterator();
        Director directorGeneral;
        Director directorPresente;
        while(iteradorDirPresentes.hasNext()){
            iteradorDirectores = listaDirectores.iterator();
            directorPresente = iteradorDirPresentes.next();
            while(iteradorDirectores.hasNext()){
                directorGeneral = iteradorDirectores.next();
                if(directorPresente.getId() == directorGeneral.getId()){
                    //Si no lo contiene, lo elimina:
                    iteradorDirectores.remove();
                }
            }
        }

        HorizontalLayout botones2 = new HorizontalLayout();
        //Creamos los MultiSelect:
        MultiSelectComboBox<Actor> opcionActor = new MultiSelectComboBox<>("Añadir actores a la película");
        opcionActor.setPlaceholder("Añada los actores");
        MultiSelectComboBox<Director> opcionDirector = new MultiSelectComboBox<>("Añadir directores a la película");
        opcionDirector.setPlaceholder("Añada los actores");
        opcionActor.setItems(listaActores);
        opcionActor.setItemLabelGenerator(actor -> actor.getNombre()+" "+actor.getApellidos());
        opcionDirector.setItems(listaDirectores);
        opcionDirector.setItemLabelGenerator(director -> director.getNombre()+" "+director.getApellidos());



        Button guardar = new Button("Guardar");
        Button atras = new Button("Atrás");
        atras.setWidthFull();
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.setWidthFull();
        guardar.addClickListener(event->{
            if(opcionDirector != null){
                directoresSeleccionados.addAll(opcionDirector.getSelectedItems());
                try {
                    GestorPeliculas.anyadirRelDirector(directoresSeleccionados, pelicula);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(opcionActor != null) {
                actoresSeleccionados.addAll(opcionActor.getSelectedItems());
                try {
                    GestorPeliculas.anyadirRelActor(actoresSeleccionados, pelicula);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                //Rellenar la tabla:
                tablaActores.setItems(GestorPeliculas.buscarActoresRelacionados(pelicula));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                tablaDirectores.setItems(GestorPeliculas.buscarDirectoresRelacionados(pelicula));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Refrescar la tabla:
            tablaActores.getDataProvider().refreshAll();
            tablaDirectores.getDataProvider().refreshAll();
            //Limpiamos el campo:
            opcionActor.clear();
            opcionDirector.clear();
        });
        atras.addClickListener(click ->{
            //Aquí irán los clear:
            opcionActor.clear();
            opcionDirector.clear();
            try {
                //Rellenar la tabla:
                tablaActores.setItems(GestorPeliculas.buscarActoresRelacionados(pelicula));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                tablaDirectores.setItems(GestorPeliculas.buscarDirectoresRelacionados(pelicula));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Refrescar la tabla:
            tablaActores.getDataProvider().refreshAll();
            tablaDirectores.getDataProvider().refreshAll();
            //Volvemos a la tabla:
            ventanaFormulario.setVisible(false);
            ventanaTabla.setVisible(true);
        });



        botones.add(anyadir, cancelar);
        botones2.add(guardar, atras);
        ventanaFormulario.add(opcionActor, opcionDirector, botones2);
        ventanaTabla.add(tituloActores, tablaActores, tituloDirectores, tablaDirectores, botones);
        ventana.add(ventanaTabla,ventanaFormulario);
        return ventana;
    }

    /**
     * Crea la disposición de la tabla y sus distintos ecomponentes.
     *
     * @return
     * @throws IOException
     * @author Antonio Mas Esteve
     */
    public VerticalLayout tablaLayout() throws IOException {
        /***********Se crea el botón añadir y el título***********/
        HorizontalLayout anyadirBoton = new HorizontalLayout();
        botonanyadir = new Button("Añadir Película");
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
     * Método que buscará las películas.
     *
     * @return
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
       textTitulo.addValueChangeListener(e-> {
           String tituloPelicula = e.getValue();
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
               if(pelicula.getTitulo().contains(tituloPelicula)){
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

        });
        textCategoria.addValueChangeListener(e-> {
            if(e.getValue() != null) {
                Categoria categoria = e.getValue();
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
                    if(pelicula.getCategoria() == categoria){
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
        });
        textFormato.addValueChangeListener(e-> {
            if(e.getValue() != null) {
                Formato formato = e.getValue();
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
                    if (pelicula.getFormato() == formato) {
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
        });
        textValoracion.addValueChangeListener(e-> {
            if(e.getValue() != null) {
                Valoracion valoracion = e.getValue();
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
                    if (pelicula.getValoracion() == valoracion) {
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
        });

        panelBuscador.add(textTitulo, textAnyo, textCategoria, textFormato, textValoracion);
        return panelBuscador;
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
        tabla.addColumn(new ComponentRenderer<>(Button::new, ((boton, pelicula) -> {
            boton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
            boton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
            boton.addClickListener(event -> {
                //Aquí es donde se rellena la ventana emergente y además se hace visible:
                try {
                    anyadirVentana.add(rellenarVentana(pelicula));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                anyadirVentana.open();
            });
        }))).setHeader("Equipo").setResizable(true).setAutoWidth(true);
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
