package excepciones;

/**
 * Se lanza al intentar agregar un elemento a un arreglo que ya está lleno.
 */
public class AlmacenamientoLlenoException extends Exception {
    public AlmacenamientoLlenoException(String mensaje) {
        super(mensaje);
    }
}