package modelo;

/**
 * Moto: 30 kg, 40 km/h, $120 por km, 5 minutos extra por estacionamiento.
 */
public class Moto extends Vehiculo {

    public static final double CAPACIDAD_KG = 30;
    public static final double VELOCIDAD_KMH = 40;
    public static final double COSTO_POR_KM = 120;
    public static final double TIEMPO_EXTRA_MIN = 5;

    /**
     * Crea una moto con los valores de referencia.
     *
     * @param patente patente de la moto
     */
    public Moto(String patente) {
        super(patente, CAPACIDAD_KG, VELOCIDAD_KMH, COSTO_POR_KM);
    }

    /**
     * Crea una moto con valores personalizados.
     */
    public Moto(String patente, double capacidadKg, double velocidadKmH, double costoPorKm) {
        super(patente, capacidadKg, velocidadKmH, costoPorKm);
    }

    @Override
    public double calcularTiempoEntrega(double km) {
        return calcularMinutosDeViaje(km) + TIEMPO_EXTRA_MIN;
    }
}