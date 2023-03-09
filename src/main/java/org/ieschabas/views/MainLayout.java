package org.ieschabas.views;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.ieschabas.components.appnav.AppNav;
import org.ieschabas.components.appnav.AppNavItem;
import org.ieschabas.views.actores.ActoresView;
import org.ieschabas.views.alquileres.AlquileresView;
import org.ieschabas.views.directores.DirectoresView;
import org.ieschabas.views.películas.PeliculasView;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    /**
     * Constructor principal de la vista principal.
     */
    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    /**
     * Crea el Header
     */
    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
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
     * @return
     */
    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        nav.addItem(new AppNavItem("Películas", PeliculasView.class, "la la-film"));
        nav.addItem(new AppNavItem("Actores", ActoresView.class, "la la-user"));
        nav.addItem(new AppNavItem("Directores", DirectoresView.class, "la la-user-tie"));
        nav.addItem(new AppNavItem("Alquileres", AlquileresView.class, "la la-file"));

        return nav;
    }

    /**
     * Crea el footer (no en uso)
     * @return
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
     * @return
     */
    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
