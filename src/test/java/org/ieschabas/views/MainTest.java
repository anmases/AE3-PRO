package org.ieschabas.views;

import org.ieschabas.clases.Alquiler;
import org.ieschabas.daos.AlquilerDAO;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void buscarTest(){
        for(int i=1; AlquilerDAO.buscar(i)==null; i++) {
            Alquiler alquiler = AlquilerDAO.buscar(i);
            System.out.println(alquiler.toString());
        }
}
}