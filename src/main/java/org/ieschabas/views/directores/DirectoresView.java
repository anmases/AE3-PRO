package org.ieschabas.views.directores;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.ieschabas.clases.Actor;
import org.ieschabas.clases.Director;
import org.ieschabas.librerias.GestorDirectores;
import org.ieschabas.views.MainLayout;
import java.io.IOException;

@PageTitle("Directores")
@Route(value = "Directores", layout = MainLayout.class)
public class DirectoresView extends VerticalLayout {
    //Campos
    private Grid<Director> tabla = new Grid<>(Director.class, false);
    private Button cancelar = new Button("Cancelar");
    private Button guardar = new Button("Guardar");
    private Button eliminar = new Button("Eliminar");

    //private BeanValidationBinder<Director> binder;

    private VerticalLayout anyadirTabla;
    private VerticalLayout anyadirEditor;

    private Actor actor;
    public DirectoresView() throws IOException {
        addClassName("Directores-view");
        SplitLayout splitLayout = new SplitLayout();

        splitLayout.addToPrimary(tablaLayout());
        splitLayout.addToSecondary(editorLayout());

        add(splitLayout);
    }

    /****************Vistas*************/
    public VerticalLayout editorLayout() {
        anyadirEditor = new VerticalLayout();
        anyadirEditor.setClassName("contenedor-editor");
        //Contenedor editor: Formulario + botones:
        anyadirEditor.add(crearFormulario(), botonLayout());
        return anyadirEditor;
    }
    public HorizontalLayout botonLayout() {
        //Disposición horizontal para los botones:
        HorizontalLayout vistaBoton = new HorizontalLayout();
        vistaBoton.setClassName("button-layout");
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        vistaBoton.add(guardar, cancelar, eliminar);
        return vistaBoton;
    }
    public VerticalLayout tablaLayout() throws IOException {
        anyadirTabla = new VerticalLayout();
        anyadirTabla.add(crearTabla());
        anyadirTabla.setVisible(true);
        return anyadirTabla;
    }
    /****************Componentes***************/
    public Component crearTabla() throws IOException {
        tabla.setAllRowsVisible(true);
        Grid.Column<Director> campoId = tabla.addColumn(Director::getId).setAutoWidth(true).setHeader("Id");
        Grid.Column<Director> campoNombre =tabla.addColumn(Director::getNombre).setAutoWidth(true).setHeader("Nombre");
        Grid.Column<Director> campoApellidos =tabla.addColumn(Director::getApellidos).setAutoWidth(true).setHeader("Apellidos");
        Grid.Column<Director> campoAnyo =tabla.addColumn(Director::getAnyoNacimiento).setAutoWidth(true).setHeader("Año nacimiento");
        Grid.Column<Director> campoPais =tabla.addColumn(Director::getPais).setAutoWidth(true).setHeader("Nacionalidad");
        rellenarTabla();
        refrescarTabla();
        return tabla;
    }
    public Component crearFormulario(){
        FormLayout formLayout = new FormLayout();
        IntegerField textId = new IntegerField("Id");
        textId.setEnabled(false);
        TextField textNombre = new TextField("Nombre");
        TextField textApellidos = new TextField("Apellidos");
        IntegerField textAnyo = new IntegerField("Año de nacimiento");
        TextField textPais = new TextField("Nacionalidad");
        formLayout.add(textId, textNombre, textApellidos, textAnyo, textPais);
        return formLayout;
    }

    /****************Métodos generales*************/

    public void rellenarTabla() throws IOException {
        //añadimos los valores tipo objeto lista a la tabla:
        tabla.setItems(GestorDirectores.listarActores().values());
    }
    public void refrescarTabla() {
        tabla.select(null);
        tabla.getDataProvider().refreshAll();
    }


}
