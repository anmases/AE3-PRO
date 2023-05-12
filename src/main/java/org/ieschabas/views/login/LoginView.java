package org.ieschabas.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.ieschabas.backend.clases.Cliente;
import org.ieschabas.backend.clases.Usuario;
import org.ieschabas.backend.daos.UsuarioDAO;
import org.ieschabas.mailServices.ServicioCorreo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.time.LocalDate;
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
    private final UsuarioDAO usuarioDao;
    private final ServicioCorreo servicioCorreo;
    private LoginForm vistaLogin;
    private Dialog dialogoCliente;

    /**
     * Constructor de la Vista login.
     */
    public LoginView(UsuarioDAO usuarioDao, ServicioCorreo servicioCorreo) {
        this.usuarioDao = usuarioDao;
        this.servicioCorreo = servicioCorreo;
        dialogoCliente = new Dialog();
        dialogoCliente.setResizable(true);
        setAlignItems(Alignment.CENTER);
      add(dialogoCliente, vistaLogin(), crearUsuarioBoton());
    }

    /**
     * Crea el botón de registro de nuevo usuario
     * @author Antonio Mas Esteve
     * @return Button
     */
    public Button crearUsuarioBoton(){
        Button nuevoUsuario = new Button("Registrarse");
        nuevoUsuario.addClickListener(e->{
            dialogoCliente.add(crearFormularioCliente());
            dialogoCliente.open();
        });
        return nuevoUsuario;
    }

    /**
     * Método que crea el formulario de registro de nuevo cliente.
     * Si se quiere crear un administrador se hará añadiendo un cliente y modificando su rol en la base de datos directamente.
     * @author Antonio Mas Esteve
     * @return Dialog
     */
    public VerticalLayout crearFormularioCliente(){
        FormLayout formulario = new FormLayout();
        H3 titulo = new H3("Crear nuevo usuario");
        TextField nombre = new TextField("nombre");
        TextField apellidos = new TextField("Apellidos");
        TextField direccion = new TextField("Dirección");
        EmailField email = new EmailField("Correo electrónico");
        PasswordField contrasenya = new PasswordField("Contraseña");

        Button guardar = new Button("Guardar");
        guardar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.addClickListener(e->{
            if(nombre.getValue() != null && apellidos.getValue()!= null && direccion.getValue() != null && email.getValue() != null && contrasenya.getValue() != null) {
                //Se encripta la contraseña:
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
                String encodedPassword = encoder.encode(contrasenya.getValue());
                Usuario cliente = new Cliente(0, nombre.getValue(), apellidos.getValue(), email.getValue(), encodedPassword, direccion.getValue(), true, LocalDate.now());
                if (usuarioDao.insertar(cliente)) {
                    Notification notification = Notification.show("Usuario creado correcamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                dialogoCliente.removeAll();
                dialogoCliente.close();
            }
            else{
                Notification notification = Notification.show("Todos los campos deben rellenarse");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        Button cancelar = new Button("Cancelar");
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelar.addClickListener(e->{
            dialogoCliente.removeAll();
            dialogoCliente.close();

        });
        dialogoCliente.addDialogCloseActionListener(e -> {
            dialogoCliente.close();
            dialogoCliente.removeAll();
        });
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar);
        botones.setWidthFull();
        Style layoutStyle = botones.getStyle();
        layoutStyle.set("justify-content", "flex-end");
        formulario.add(nombre, apellidos, direccion, email, contrasenya);
      return new VerticalLayout(titulo, formulario, botones);
    }

    /**
     * Método que crea el formulario Login
     * @return LoginForm
     * @author Antonio Mas Esteve
     */
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
        vistaLogin.setForgotPasswordButtonVisible(true);
        vistaLogin.addForgotPasswordListener(event->{
            recuperarContrasenya();
        });

        return vistaLogin;
    }

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
            //Se encripta la contraseña:
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
            String encodedPassword = encoder.encode(contrasenya);
            usuarioNuevo.setContrasenya(encodedPassword);
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
