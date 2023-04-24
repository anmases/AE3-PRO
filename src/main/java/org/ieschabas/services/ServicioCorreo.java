package org.ieschabas.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar el envío de correos de recuperación
 * @author Antonio Mas Esteve
 */
@Service
public class ServicioCorreo {
    private final JavaMailSender javaMailSender;

    /**
     * Constructor principal de la clase donde se inyectan las dependencias de JavaMailSender
     */
    public ServicioCorreo(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Método que envía un correo.
     * @return boolean
     * @author Antonio Mas Esteve
     */
    public boolean enviar(String destinatario, String contenido){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Restablecimiento de la contraseña");
        mensaje.setText("Su nueva contraseña es:\t"+ contenido);
        javaMailSender.send(mensaje);
        return true;
    }
}
