package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import excepciones.CapacidadExcedidaException;
import excepciones.EstadoInvalidoException;
import excepciones.PedidoInvalidoException;

/**
 * Pedido de envío. Es la raíz de la jerarquía: define los datos comunes,
 * valida las transiciones de estado y obliga a cada subclase a calcular
 * su costo. Implementa Rastreable y Facturable.
 */
public abstract class Pedido implements Rastreable, Facturable {

    /** Costo base de cualquier envío, en pesos. */
    public static final double COSTO_BASE = 1000;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int codigo;
    private Cliente cliente;
    private String destino;
    private double pesoKg;
    private double distanciaKm;
    private LocalDateTime fechaHora;
    private EstadoPedido estado;
    private Vehiculo vehiculo; // null hasta que se asigna a un repartidor

    /**
     * Crea un pedido con la fecha y hora actuales, en estado PENDIENTE.
     *
     * @param codigo      código único (mayor que cero)
     * @param cliente     cliente que hace el pedido (no nulo)
     * @param destino     zona o punto de la ciudad (no vacío)
     * @param pesoKg      peso en kg (mayor que cero)
     * @param distanciaKm distancia en km (mayor que cero)
     * @throws PedidoInvalidoException si algún dato no es válido
     */
    public Pedido(int codigo, Cliente cliente, String destino,
                  double pesoKg, double distanciaKm) throws PedidoInvalidoException {
        this(codigo, cliente, destino, pesoKg, distanciaKm, LocalDateTime.now());
    }

    /**
     * Crea un pedido con una fecha y hora indicadas, en estado PENDIENTE.
     * Sirve para los datos de prueba y para ordenar por fecha.
     *
     * @throws PedidoInvalidoException si algún dato no es válido
     */
    public Pedido(int codigo, Cliente cliente, String destino,
                  double pesoKg, double distanciaKm,
                  LocalDateTime fechaHora) throws PedidoInvalidoException {
        if (codigo <= 0) {
            throw new PedidoInvalidoException("El código del pedido debe ser mayor que cero.");
        }
        if (cliente == null) {
            throw new PedidoInvalidoException("El pedido necesita un cliente.");
        }
        if (fechaHora == null) {
            throw new PedidoInvalidoException("El pedido necesita una fecha y hora.");
        }
        this.codigo = codigo;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.estado = EstadoPedido.PENDIENTE;
        setDestino(destino);
        setPesoKg(pesoKg);
        setDistanciaKm(distanciaKm);
    }

    /**
     * Calcula el costo del envío con el vehículo indicado.
     * Cada subclase aplica su propio recargo.
     *
     * @param vehiculo vehículo que transporta el pedido
     * @return el costo en pesos
     */
    public abstract double calcularCosto(Vehiculo vehiculo);

    /**
     * Costo sin recargos: $1000 + (distancia × costo por km del vehículo).
     * Lo usan las subclases para no repetir la fórmula.
     *
     * @param vehiculo vehículo que transporta el pedido (no nulo)
     * @return el costo base en pesos
     * @throws IllegalArgumentException si el vehículo es nulo
     */
    protected double calcularCostoBase(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Se necesita un vehículo para calcular el costo.");
        }
        return COSTO_BASE + distanciaKm * vehiculo.getCostoPorKm();
    }

    /**
     * Indica si el pedido es frágil. Por defecto es false; PedidoFragil lo redefine.
     * Permite a Vehiculo decidir sin usar instanceof.
     */
    public boolean esFragil() {
        return false;
    }

    /**
     * Indica si el pedido es urgente. Por defecto es false; PedidoUrgente lo redefine.
     */
    public boolean esUrgente() {
        return false;
    }

    /**
     * Cambia el estado del pedido validando la transición.
     * Válidas: PENDIENTE → EN_CAMINO, EN_CAMINO → ENTREGADO y PENDIENTE → CANCELADO.
     *
     * @param nuevoEstado estado al que se quiere pasar
     * @throws EstadoInvalidoException si la transición no está permitida
     */
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        boolean valida =
                (estado == EstadoPedido.PENDIENTE
                        && (nuevoEstado == EstadoPedido.EN_CAMINO
                        || nuevoEstado == EstadoPedido.CANCELADO))
                || (estado == EstadoPedido.EN_CAMINO
                        && nuevoEstado == EstadoPedido.ENTREGADO);
        if (!valida) {
            throw new EstadoInvalidoException(
                    "Transición inválida: " + estado + " → " + nuevoEstado);
        }
        estado = nuevoEstado;
    }

    /**
     * Asigna el vehículo que va a transportar el pedido, si puede hacerlo.
     *
     * @param vehiculo vehículo a asignar
     * @throws CapacidadExcedidaException si el vehículo no puede transportarlo
     *                                    (peso excedido o frágil en bicicleta)
     */
    public void asignarVehiculo(Vehiculo vehiculo) throws CapacidadExcedidaException {
        if (!vehiculo.puedeTransportar(this)) {
            throw new CapacidadExcedidaException(
                    "El vehículo " + vehiculo.getPatente() + " no puede transportar el pedido "
                    + codigo + " (peso " + pesoKg + " kg, capacidad "
                    + vehiculo.getCapacidadKg() + " kg"
                    + (esFragil() ? ", pedido frágil" : "") + ").");
        }
        this.vehiculo = vehiculo;
    }

    /**
     * Describe el estado actual del pedido.
     */
    @Override
    public String obtenerEstadoActual() {
        return "Pedido " + codigo + " hacia " + destino + ": " + estado;
    }

    /**
     * Importe del pedido: el costo del envío con el vehículo asignado.
     * Si todavía no tiene vehículo o está cancelado, el importe es 0.
     */
    @Override
    public double calcularImporte() {
        if (vehiculo == null || estado == EstadoPedido.CANCELADO) {
            return 0;
        }
        return calcularCosto(vehiculo);
    }

    public int getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getDestino() {
        return destino;
    }

    /**
     * @param destino nuevo destino, no puede ser nulo ni vacío
     * @throws PedidoInvalidoException si el destino es nulo o vacío
     */
    public void setDestino(String destino) throws PedidoInvalidoException {
        if (destino == null || destino.trim().isEmpty()) {
            throw new PedidoInvalidoException("El destino no puede estar vacío.");
        }
        this.destino = destino.trim();
    }

    public double getPesoKg() {
        return pesoKg;
    }

    /**
     * @param pesoKg nuevo peso, debe ser mayor que cero
     * @throws PedidoInvalidoException si no es positivo
     */
    public void setPesoKg(double pesoKg) throws PedidoInvalidoException {
        if (pesoKg <= 0) {
            throw new PedidoInvalidoException("El peso debe ser mayor que cero.");
        }
        this.pesoKg = pesoKg;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    /**
     * @param distanciaKm nueva distancia, debe ser mayor que cero
     * @throws PedidoInvalidoException si no es positiva
     */
    public void setDistanciaKm(double distanciaKm) throws PedidoInvalidoException {
        if (distanciaKm <= 0) {
            throw new PedidoInvalidoException("La distancia debe ser mayor que cero.");
        }
        this.distanciaKm = distanciaKm;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [código=" + codigo
                + ", cliente=" + cliente.getNombre()
                + ", destino=" + destino
                + ", peso=" + pesoKg + " kg"
                + ", distancia=" + distanciaKm + " km"
                + ", fecha=" + fechaHora.format(FORMATO_FECHA)
                + ", estado=" + estado + "]";
    }
}