package excepciones;

/**
 * Se lanza cuando el peso supera la capacidad del vehículo
 * o se intenta asignar un pedido frágil a una bicicleta.
 */
public class CapacidadExcedidaException extends Exception {
    public CapacidadExcedidaException(String mensaje) {
        super(mensaje);
    }
}