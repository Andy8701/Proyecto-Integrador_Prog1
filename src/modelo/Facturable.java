package modelo;

/**
 * Contrato para los objetos que generan un importe en pesos.
 * Lo implementan Pedido (costo del envío) y Repartidor (comisión).
 */
public interface Facturable {

    /**
     * Calcula el importe del objeto.
     *
     * @return el importe en pesos
     */
    double calcularImporte();
}