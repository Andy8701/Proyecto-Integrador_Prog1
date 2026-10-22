package excepciones;

/**
 * Se lanza ante una transición de estado de pedido no permitida.
 * Es unchecked: no obliga a declarar throws ni a capturarla.
 */
public class EstadoInvalidoException extends RuntimeException {
    public EstadoInvalidoException(String mensaje) {
        super(mensaje);
    }
}