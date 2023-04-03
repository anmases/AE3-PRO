package org.ieschabas.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.ieschabas.clases.Usuario;
import org.ieschabas.login.Login;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Vista de login
 * @author Antonio Mas Esteve
 */
@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout {
    @Serial
    private static final long serialVersionUID = 272742160091975700L;
    private static File archivoConfig = new File("remember.properties");
    private static Properties estado;
    private static boolean logIn;
    private static boolean esAdmin;
    private static int idUsuario;
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

            inicioSesion(e.getUsername(),e.getPassword());
            if(!Login.login(e.getUsername(), e.getPassword())){
                vistaLogin.setError(true);
            }

        });

        return vistaLogin;
    }

    /**
     * Método que comprueba su existe el usuario o no.
     */
    public void inicioSesion(String usuario, String contrasenya){
        //Ponemos el estado como logeado:
        logIn = Login.login(usuario, contrasenya);
        esAdmin = Login.isAdmin(usuario,contrasenya);
        idUsuario = Login.obtenerIdUsuario(usuario, contrasenya);
        //Creamos una persistencia de datos como login para cuando se reinicie la página que recuerde tu sesión:
        recordarEstado(logIn, esAdmin, idUsuario);
        //Esto refresca la página
        UI.getCurrent().getPage().reload();
    }
    public void cierreSesion(){
        //Borramos el fichero, para que no almacene cada movimiento de login/logout.

        try {
            FileWriter borrador = new FileWriter(archivoConfig, false);
            borrador.write("");
            borrador.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Ponemos el estado como logeado:
        logIn = false;
        esAdmin = false;
        idUsuario = 0;
        //Creamos una persistencia de datos como logout para cuando se reinicie la página que recuerde tu sesión:
        recordarEstado(logIn, esAdmin, idUsuario);
        //Esto refresca la página
        UI.getCurrent().getPage().reload();
    }
public void recordarEstado(boolean logIn, boolean esAdmin, int idUsuario){
//Se crea el objeto properties y se rellena con datos, que son 3: Iniciado, Admin e id de usuario.
    estado = new Properties();
    //Guardamos las propiedades:
    estado.setProperty("logIn", logIn+"");
    estado.setProperty("esAdmin", esAdmin+"");
    estado.setProperty("idUsuario", idUsuario+"");
    //Lo escribimos en el fichero:
    try {
        FileOutputStream output = new FileOutputStream(archivoConfig, true);
        estado.store(output, "Info de sesión");
        output.close();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
////////////Métodos para ser usados por otras clases para comprobar el estado o modificarlo///////////////////////////////////
public static boolean comprobarLogIn(){
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
public static boolean comprobarAdmin(){
    estado = new Properties();
    //Leemos el estado actual del fichero properties:
    if(archivoConfig.exists()){
        try {
            FileInputStream input = new FileInputStream(archivoConfig);
            estado.load(input);
            esAdmin = Boolean.parseBoolean(estado.getProperty("esAdmin"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    return esAdmin;
}
public static int comprobarIdUsuario(){
    estado = new Properties();
    //Leemos el estado actual del fichero properties:
    if(archivoConfig.exists()){
        try {
            FileInputStream input = new FileInputStream(archivoConfig);
            estado.load(input);
            idUsuario = Integer.parseInt(estado.getProperty("idUsuario"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    return idUsuario;
}


public static void cerrarSesion(){
        LoginView loginView = new LoginView();
        loginView.cierreSesion();
}

}
