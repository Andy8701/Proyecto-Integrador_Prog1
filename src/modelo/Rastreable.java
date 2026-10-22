package modelo;

/**
 * Contrato para los objetos cuyo estado actual se puede consultar.
 * Lo implementan Pedido y Repartidor.
 */
public interface Rastreable {

    /**
     * Describe el estado actual del objeto.
     *
     * @return un texto con el estado actual
     */
    String obtenerEstadoActual();
}