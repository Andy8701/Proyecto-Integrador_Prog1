package modelo;

import java.util.Arrays;

import excepciones.AlmacenamientoLlenoException;
import excepciones.CapacidadExcedidaException;
import excepciones.PedidoInvalidoException;

/**
 * Repartidor de CityLink. Tiene un vehículo y un arreglo de pedidos a su cargo.
 * Implementa Rastreable (su estado actual) y Facturable (su comisión).
 */
public class Repartidor implements Rastreable, Facturable {

    /** Cantidad máxima de pedidos que puede tener a cargo un repartidor. */
    public static final int MAX_PEDIDOS = 20;

    /** Porcentaje del importe entregado que cobra como comisión. */
    public static final double PORCENTAJE_COMISION = 0.10;

    private int legajo;
    private String nombre;
    private Vehiculo vehiculo;
    private Pedido[] pedidos = new Pedido[MAX_PEDIDOS];
    private int cantPedidos = 0;

    /**
     * Crea un repartidor sin pedidos asignados.
     *
     * @param legajo   legajo (mayor que cero)
     * @param nombre   nombre (no vacío)
     * @param vehiculo vehículo asignado (no nulo)
     * @throws IllegalArgumentException si algún dato no es válido
     */
    public Repartidor(int legajo, String nombre, Vehiculo vehiculo) {
        setLegajo(legajo);
        setNombre(nombre);
        setVehiculo(vehiculo);
    }

    /**
     * Asigna un pedido al repartidor usando su propio vehículo.
     *
     * @param pedido pedido a asignar
     * @throws PedidoInvalidoException       si el pedido es nulo, no está pendiente o ya fue asignado
     * @throws CapacidadExcedidaException    si el vehículo no puede transportarlo
     * @throws AlmacenamientoLlenoException  si el repartidor ya tiene el máximo de pedidos
     */
    public void asignarPedido(Pedido pedido)
            throws PedidoInvalidoException, CapacidadExcedidaException, AlmacenamientoLlenoException {
        asignarPedido(pedido, vehiculo);
    }

    /**
     * Asigna un pedido al repartidor usando un vehículo indicado
     * (por ejemplo, uno de reemplazo).
     *
     * @param pedido   pedido a asignar
     * @param vehiculo vehículo que lo va a transportar (no nulo)
     * @throws PedidoInvalidoException       si el pedido es nulo, no está pendiente o ya fue asignado
     * @throws CapacidadExcedidaException    si el vehículo no puede transportarlo
     * @throws AlmacenamientoLlenoException  si el repartidor ya tiene el máximo de pedidos
     * @throws IllegalArgumentException      si el vehículo es nulo
     */
    public void asignarPedido(Pedido pedido, Vehiculo vehiculo)
            throws PedidoInvalidoException, CapacidadExcedidaException, AlmacenamientoLlenoException {
        if (pedido == null) {
            throw new PedidoInvalidoException("No se puede asignar un pedido nulo.");
        }
        if (vehiculo == null) {
            throw new IllegalArgumentException("Se necesita un vehículo para asignar el pedido.");
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new PedidoInvalidoException(
                    "Solo se pueden asignar pedidos pendientes (el pedido "
                    + pedido.getCodigo() + " está " + pedido.getEstado() + ").");
        }
        if (pedido.getVehiculo() != null) {
            throw new PedidoInvalidoException(
                    "El pedido " + pedido.getCodigo() + " ya está asignado.");
        }
        if (cantPedidos == MAX_PEDIDOS) {
            throw new AlmacenamientoLlenoException(
                    "El repartidor " + legajo + " ya tiene " + MAX_PEDIDOS + " pedidos a su cargo.");
        }
        // Si el vehículo no puede transportarlo, lanza CapacidadExcedidaException
        // y el pedido NO se agrega al arreglo.
        pedido.asignarVehiculo(vehiculo);
        pedidos[cantPedidos] = pedido;
        cantPedidos++;
    }

    /**
     * Registra la entrega de un pedido a su cargo, pasándolo a ENTREGADO.
     * El pedido tiene que estar EN_CAMINO.
     *
     * @param pedido pedido entregado
     * @throws PedidoInvalidoException si el pedido no está a cargo de este repartidor
     * @throws excepciones.EstadoInvalidoException si el pedido no estaba EN_CAMINO
     */
    public void registrarEntrega(Pedido pedido) throws PedidoInvalidoException {
        if (pedido == null || buscarIndice(pedido) == -1) {
            throw new PedidoInvalidoException("El pedido no está asignado a este repartidor.");
        }
        pedido.cambiarEstado(EstadoPedido.ENTREGADO);
    }

    /**
     * Suma el importe de los pedidos que este repartidor ya entregó.
     *
     * @return el importe total entregado, en pesos
     */
    public double calcularImporteEntregado() {
        double total = 0;
        for (int i = 0; i < cantPedidos; i++) {
            if (pedidos[i].getEstado() == EstadoPedido.ENTREGADO) {
                total += pedidos[i].calcularImporte();
            }
        }
        return total;
    }

    /**
     * Comisión del repartidor: 10% del importe de los pedidos que entregó.
     */
    @Override
    public double calcularImporte() {
        return calcularImporteEntregado() * PORCENTAJE_COMISION;
    }

    /**
     * Describe el estado actual del repartidor: su vehículo y cuántos pedidos tiene.
     */
    @Override
    public String obtenerEstadoActual() {
        int pendientes = 0;
        int enCamino = 0;
        int entregados = 0;
        for (int i = 0; i < cantPedidos; i++) {
            switch (pedidos[i].getEstado()) {
                case PENDIENTE: pendientes++; break;
                case EN_CAMINO: enCamino++; break;
                case ENTREGADO: entregados++; break;
                default: break;
            }
        }
        return "Repartidor " + legajo + " (" + nombre + ") con " + vehiculo.getClass().getSimpleName()
                + " " + vehiculo.getPatente() + ": " + cantPedidos + " pedidos ("
                + pendientes + " pendientes, " + enCamino + " en camino, "
                + entregados + " entregados)";
    }

    private int buscarIndice(Pedido pedido) {
        for (int i = 0; i < cantPedidos; i++) {
            if (pedidos[i] == pedido) {
                return i;
            }
        }
        return -1;
    }

    public int getLegajo() {
        return legajo;
    }

    /**
     * @param legajo nuevo legajo, debe ser mayor que cero
     * @throws IllegalArgumentException si no es positivo
     */
    public void setLegajo(int legajo) {
        if (legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser mayor que cero.");
        }
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre nuevo nombre, no puede ser nulo ni vacío
     * @throws IllegalArgumentException si es nulo o vacío
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * @param vehiculo nuevo vehículo, no puede ser nulo
     * @throws IllegalArgumentException si es nulo
     */
    public void setVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El repartidor necesita un vehículo.");
        }
        this.vehiculo = vehiculo;
    }

    /**
     * Devuelve una copia de los pedidos a cargo, sin los casilleros vacíos.
     * Es una copia para que nadie de afuera pueda modificar el arreglo interno.
     */
    public Pedido[] getPedidos() {
        return Arrays.copyOf(pedidos, cantPedidos);
    }

    public int getCantPedidos() {
        return cantPedidos;
    }

    @Override
    public String toString() {
        return "Repartidor [legajo=" + legajo + ", nombre=" + nombre
                + ", vehículo=" + vehiculo + ", pedidos=" + cantPedidos + "]";
    }
}