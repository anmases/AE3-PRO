package org.ieschabas.enums;

/**
 * Clase que sirve para crear los enumerados de la valoración
 * @author Antonio Mas Esteve.
 */
public enum Valoracion {
UNA,DOS,TRES,CUATRO,CINCO;

    /**
     * Método que convierte el enumerado a valores enteros.
     * @return int
     * @author Antonio Mas Esteve
     */
    public static int toInteger(Valoracion valoracion) {
        switch (valoracion){
            case UNA:return 1;
            case DOS:return 2;
            case TRES:return 3;
            case CUATRO:return 4;
            case CINCO: return 5;
            default:return 0;
        }
    }

}

