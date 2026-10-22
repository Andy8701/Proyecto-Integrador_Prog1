package excepciones;

/**
 * Se lanza cuando el peso o la distancia no son positivos,
 * el destino está vacío o el código de pedido está repetido.
 */
public class PedidoInvalidoException extends Exception {
    public PedidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}