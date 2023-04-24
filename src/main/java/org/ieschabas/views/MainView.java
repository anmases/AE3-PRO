package org.ieschabas.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.ieschabas.clases.Usuario;
import org.ieschabas.components.appnav.AppNav;
import org.ieschabas.components.appnav.AppNavItem;
import org.ieschabas.daos.UsuarioDAO;
import org.ieschabas.views.equipo.EquipoView;
import org.ieschabas.views.alquileres.AlquileresView;
import org.ieschabas.views.cliente.ClienteView;
import org.ieschabas.views.login.LoginView;
import org.ieschabas.views.peliculas.PeliculasView;
import org.ieschabas.views.usuarios.UsuarioView;

import java.io.Serial;


/**
 * The main view is a top-level placeholder for other views.
 */
@Route("main")
@AnonymousAllowed
public class MainView extends AppLayout implements BeforeEnterObserver {
    @Serial
    private static final long serialVersionUID = 6046822281493064403L;
    private H2 viewTitle;

    /**
     * Constructor principal de la vista principal.
     */
    public MainView() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    /**
     * Crea el Header
     */
    private void addHeaderContent() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        VerticalLayout header = new VerticalLayout();
        HorizontalLayout usuario = new HorizontalLayout();
        header.setWidthFull();

        int id = LoginView.comprobarIdUsuario();
        if(id != 0) {
            Usuario user = usuarioDAO.buscar(id);
            Icon admin = new Icon(VaadinIcon.USER_STAR);
            H4 nombreUsuario = new H4(user.getNombre() + " " + user.getApellidos());
            header.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
            Button logout = new Button("Cerrar Sesión");
            usuario.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            usuario.add(admin, nombreUsuario, logout);
            header.add(usuario);
            logout.addClickListener(e -> LoginView.cerrarSesion());
        }
        addToNavbar(true, toggle,viewTitle, header);
    }

    /**
     * Crea el contenido de la página.
     */
    private void addDrawerContent() {

        Image logo = new Image("images/PRO_LOGO.png", "VideoClub");
        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(logo, scroller, createFooter());
    }

    /**
     * Crea el menú de navegación
     * @return AppNav
     */
    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        nav.addItem(new AppNavItem("Películas", PeliculasView.class, "la la-film"));
        nav.addItem(new AppNavItem("Equipo", EquipoView.class, "la la-user-tie"));
        nav.addItem(new AppNavItem("Alquileres", AlquileresView.class, "la la-file"));
        nav.addItem(new AppNavItem("Usuarios", UsuarioView.class, "la la-users"));


        return nav;
    }

    /**
     * Crea el footer (no en uso)
     * @return Footer
     */
    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    /**
     *
     */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    /**
     * Devuelve el título de la página actual.
     * @return String
     */
    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    /**    if(LoginView.comprobarLogIn()){
            if(!LoginView.comprobarAdmin()){
                event.rerouteTo(ClienteView.class);
            }

        }else {
            event.rerouteTo(LoginView.class);
        }**/
    }
}
