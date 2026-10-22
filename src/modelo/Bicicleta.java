package modelo;

/**
 * Bicicleta: 10 kg, 15 km/h, $50 por km, sin tiempo extra.
 * No puede transportar pedidos frágiles.
 */
public class Bicicleta extends Vehiculo {

    public static final double CAPACIDAD_KG = 10;
    public static final double VELOCIDAD_KMH = 15;
    public static final double COSTO_POR_KM = 50;

    /**
     * Crea una bicicleta con los valores de referencia.
     *
     * @param patente identificación de la bicicleta
     */
    public Bicicleta(String patente) {
        super(patente, CAPACIDAD_KG, VELOCIDAD_KMH, COSTO_POR_KM);
    }

    /**
     * Crea una bicicleta con valores personalizados.
     */
    public Bicicleta(String patente, double capacidadKg, double velocidadKmH, double costoPorKm) {
        super(patente, capacidadKg, velocidadKmH, costoPorKm);
    }

    @Override
    public double calcularTiempoEntrega(double km) {
        return calcularMinutosDeViaje(km);
    }

    /**
     * Además de la capacidad, rechaza los pedidos frágiles.
     */
    @Override
    public boolean puedeTransportar(Pedido pedido) {
        return !pedido.esFragil() && super.puedeTransportar(pedido);
    }
}