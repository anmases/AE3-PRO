package org.ieschabas.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.ieschabas.clases.Administrador;
import org.ieschabas.clases.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout {
    private static File archivoConfig = new File("remember.properties");
    private static Properties estado;
    private static boolean logIn;
    private LoginForm vistaLogin;
    private ArrayList<Usuario> usuarios;
    public LoginView() {
        setAlignItems(Alignment.CENTER);
      add(vistaLogin());
    }

    public LoginForm vistaLogin(){
        vistaLogin = new LoginForm();
        vistaLogin.setForgotPasswordButtonVisible(true);
        vistaLogin.addLoginListener(e-> {
            inicioSesion();
        });

        Usuario usuario = new Usuario();
        Administrador admin = new Administrador();

        usuarios = new ArrayList<>();
        return vistaLogin;
    }
    public void inicioSesion(){
        //Ponemos el estado como logeado:
        logIn = true;
        //Creamos una persistencia de datos como login para cuando se reinicie la página que recuerde tu sesión:
        recordarEstado(logIn);
        //Esto refresca la página
        UI.getCurrent().getPage().reload();
    }
    public void cierreSesion(){
        //Borramos el fichero, para que no almacene cada movimiento de login/logout.
        archivoConfig.delete();
        //Ponemos el estado como logeado:
        logIn = false;
        //Creamos una persistencia de datos como logout para cuando se reinicie la página que recuerde tu sesión:
        recordarEstado(logIn);
        //Esto refresca la página
        UI.getCurrent().getPage().reload();
    }
public void recordarEstado(boolean logIn){
//Se crea el objeto properties y se rellena con datos.
    estado = new Properties();
    //Guardamos las propiedades:
    estado.setProperty("logIn", logIn+"");
    //Lo escribimos en el fichero:
    try {
        FileOutputStream output = new FileOutputStream(archivoConfig, true);
        estado.store(output, "Info de sesión");
        output.close();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
public static boolean comprobarEstado(){
        estado = new Properties();
        //Leemos el estado actual del fichero properties:
if(archivoConfig.exists()){
    try {
        FileInputStream input = new FileInputStream(archivoConfig);
        estado.load(input);
        logIn = Boolean.parseBoolean(estado.getProperty("logIn"));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
    return logIn;
}
public static void cerrarSesion(){
        LoginView loginView = new LoginView();
        loginView.cierreSesion();
}

}
