package org.ieschabas.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.ieschabas.clases.Cliente;
import org.ieschabas.clases.Usuario;
import org.ieschabas.daos.UsuarioDAO;
import org.ieschabas.services.ServicioCorreo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;

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
    @Autowired
    private UsuarioDAO usuarioDao;
    @Autowired
    private ServicioCorreo servicioCorreo;
    private static File archivoConfig = new File("remember.properties");
    private static Properties estado;
    private static boolean logIn;
    private static boolean esAdmin;
    private static int idUsuario;
    private LoginForm vistaLogin;
    private Dialog formularioCliente;
    public LoginView() {
        setAlignItems(Alignment.CENTER);
      add(crearFormularioCliente(), vistaLogin(), crearUsuarioBoton());
    }
    public Button crearUsuarioBoton(){
        Button nuevoUsuario = new Button("Registrarse");
        nuevoUsuario.addClickListener(e->formularioCliente.open());
        return nuevoUsuario;
    }
    public Dialog crearFormularioCliente(){
        formularioCliente = new Dialog();
        FormLayout formulario = new FormLayout();
        TextField nombre = new TextField("nombre");
        TextField apellidos = new TextField("Apellidos");
        TextField direccion = new TextField("Dirección");
        EmailField email = new EmailField("Correo electrónico");
        PasswordField contrasenya = new PasswordField("Contraseña");

        Button guardar = new Button("Guardar");
        guardar.addClickListener(e->{
            if(nombre.getValue() != null && apellidos.getValue()!= null && direccion.getValue() != null && email.getValue() != null && contrasenya.getValue() != null) {
                String rawPassword = contrasenya.getValue();
                Usuario cliente = new Cliente(nombre.getValue(), apellidos.getValue(), email.getValue(), rawPassword, direccion.getValue(), true, LocalDate.now());
                if (usuarioDao.insertar(cliente)) {
                    Notification notification = Notification.show("Usuario creado correcamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                formularioCliente.removeAll();
                formularioCliente.close();
            }
            else{
                Notification notification = Notification.show("Todos los campos deben rellenarse");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        Button cancelar = new Button("Cancelar");
        cancelar.addClickListener(e->{
            formularioCliente.removeAll();
            formularioCliente.close();

        });
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar);
        formulario.add(nombre, apellidos, direccion, email, contrasenya, botones);
        formularioCliente.add(formulario);
      return formularioCliente;
    }

    public LoginForm vistaLogin(){
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form form = i18n.getForm();
        form.setTitle("VideoClub PRO");
        form.setUsername("Correo electrónico");
        form.setPassword("Contraseña");
        form.setSubmit("Validar");
        form.setForgotPassword("¿Olvidó su contraseña?");
        i18n.setForm(form);
        //Mensaje de error:
        LoginI18n.ErrorMessage mensaje = i18n.getErrorMessage();
        mensaje.setTitle("Email o contraseña incorrectos");
        mensaje.setMessage("Aviso: Se distingue entre mayúsculas y minúsculas, caracteres y símbolos especiales");
        i18n.setErrorMessage(mensaje);
        vistaLogin = new LoginForm();
        vistaLogin.setAction("login");
        vistaLogin.setI18n(i18n);
       /** vistaLogin.addLoginListener(e-> {
            inicioSesion(e.getUsername(),e.getPassword());
            if(!Login.login(e.getUsername(), e.getPassword())){
                vistaLogin.setError(true);
            }});**/
        vistaLogin.setForgotPasswordButtonVisible(true);
        vistaLogin.addForgotPasswordListener(event->{
            recuperarContrasenya();
        });

        return vistaLogin;
    }

/**
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
**/
    /**
     * Método que crea una nueva contraseña y la envía por email.
     * @author Antonio Mas Esteve
     */
    public void recuperarContrasenya(){
    Random random = new Random();
    int numero = random.nextInt(10000);
    String contrasenya = numero+"";
    Dialog dialogo = new Dialog();
    H3 titulo = new H3("Enviar nueva contraseña por Email");
    EmailField emailCampo = new EmailField("Escriba su Email:");
    Button send = new Button("Enviar");
    send.addClickListener(e->{
        if(usuarioDao.buscarPorMail(emailCampo.getValue()) != null){
        if(servicioCorreo.enviar(emailCampo.getValue(), contrasenya)){
            Usuario usuarioNuevo = usuarioDao.buscarPorMail(emailCampo.getValue());
            usuarioNuevo.setContrasenya(contrasenya);
            usuarioDao.modificar(usuarioNuevo);
            Notification notification = Notification.show("Contraseña cambiada correctamente. Revise su bandeja de entrada");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        }else{
            Notification notification = Notification.show("El Email no existe");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        dialogo.close();
    });
    dialogo.add(titulo, emailCampo, send);
    dialogo.open();
}

}
