package org.ieschabas.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ServicioCorreo {
    private static JavaMailSender javaMailSender;

    public ServicioCorreo(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public boolean enviar(String destinatario, String contenido){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Restablecimiento de la contraseña");
        mensaje.setText("Su nueva contraseña es:\t"+ contenido);
        javaMailSender.send(mensaje);
        return true;
    }
}
