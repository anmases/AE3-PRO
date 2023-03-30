package org.ieschabas;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.ieschabas.clases.Alquiler;
import org.ieschabas.daos.AlquilerDAO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTest implements AppShellConfigurator {

@Test
 void prueba() {
      /**  List<Alquiler> alquileres =AlquilerDAO.listar();
        for(Alquiler valor: alquileres){
            Alquiler alquiler = valor;
            System.out.println(alquiler.toString());
        }**/

    }
}