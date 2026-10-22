package excepciones;

/**
 * Se lanza al buscar un cliente que no existe.
 */
public class ClienteNoEncontradoException extends Exception {
    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}