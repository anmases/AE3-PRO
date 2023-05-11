package org.ieschabas.clases;

import javax.persistence.*;
import java.io.Serial;
import java.util.Objects;

/**
 * Clase para crear los pagos.
 * @author Antonio Mas Esteve
 * jsnbdvikcnksldmc
 */
@Entity
@Table(name = "pago")
public class Pago extends EntidadAbstracta{
    @Serial
    private static final long serialVersionUID = -7245171100469537038L;
    //Instancia de campos
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_alquiler", nullable = false)
    private Alquiler alquiler;
    @Column(name = "cantidad", nullable = false)
    private double cantidad;
    @Column(name = "fecha_pago", nullable = false)
    private String fechaPago;
    @Column(name = "completado", nullable = false)
    private boolean completado;

    /**
     * Constructor vacío
     */
    public Pago() {super();}

    /**
     * Constructor con argumentos
     */
    public Pago(int id, Cliente cliente, Alquiler alquiler, double cantidad, String fechaPago, boolean completado) {
        super(id);
       this.cliente = cliente;
       this.alquiler = alquiler;
       this.cantidad = cantidad;
       this.fechaPago = fechaPago;
       this.completado = completado;
    }
//Setters y getters


    /**
     * devuelve el id del cliente
     * @return int
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * devuelve la cantidad
     * @return double
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * devuelve el id del alquiler
     * @return int
     */
    public Alquiler getAlquiler() {
        return alquiler;
    }

    /**
     * devuelve la fecha de pago
     * @return String
     */
    public String getFechaPago() {
        return fechaPago;
    }

    /**
     * devuelve si ha sido o no pagado
     * @return boolean
     */
    public boolean isCompletado() {
        return completado;
    }

    /**
     * añade el "id" del cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * añade el "id" de alquiler
     */
    public void setAlquiler(Alquiler alquiler) {
        this.alquiler = alquiler;
    }

    /**
     * añade la cantidad
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * añade la comprobación
     */
    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    /**
     * añade la fecha del pago
     */
    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "cliente=" + cliente +
                ", alquiler=" + alquiler +
                ", cantidad=" + cantidad +
                ", fechaPago='" + fechaPago + '\'' +
                ", completado=" + completado +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pago pago)) return false;
        return Double.compare(pago.cantidad, cantidad) == 0 && completado == pago.completado && Objects.equals(cliente, pago.cliente) && Objects.equals(alquiler, pago.alquiler) && Objects.equals(fechaPago, pago.fechaPago);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, alquiler, cantidad, fechaPago, completado);
    }
}
