package modelo;

/**
 * Camioneta: 500 kg, 30 km/h, $300 por km, 10 minutos extra por carga y descarga.
 */
public class Camioneta extends Vehiculo {

    public static final double CAPACIDAD_KG = 500;
    public static final double VELOCIDAD_KMH = 30;
    public static final double COSTO_POR_KM = 300;
    public static final double TIEMPO_EXTRA_MIN = 10;

    /**
     * Crea una camioneta con los valores de referencia.
     *
     * @param patente patente de la camioneta
     */
    public Camioneta(String patente) {
        super(patente, CAPACIDAD_KG, VELOCIDAD_KMH, COSTO_POR_KM);
    }

    /**
     * Crea una camioneta con valores personalizados.
     */
    public Camioneta(String patente, double capacidadKg, double velocidadKmH, double costoPorKm) {
        super(patente, capacidadKg, velocidadKmH, costoPorKm);
    }

    @Override
    public double calcularTiempoEntrega(double km) {
        return calcularMinutosDeViaje(km) + TIEMPO_EXTRA_MIN;
    }
}