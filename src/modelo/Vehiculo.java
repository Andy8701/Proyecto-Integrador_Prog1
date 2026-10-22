package modelo;

/**
 * Vehículo de reparto. Es la raíz de la jerarquía: define los datos comunes
 * y obliga a cada subclase a calcular su tiempo de entrega.
 */
public abstract class Vehiculo {

    private String patente;
    private double capacidadKg;
    private double velocidadKmH;
    private double costoPorKm;

    /**
     * Constructor completo. Usa los setters para validar los datos.
     *
     * @param patente      patente del vehículo (no vacía)
     * @param capacidadKg  capacidad máxima en kg (mayor que cero)
     * @param velocidadKmH velocidad promedio en km/h (mayor que cero)
     * @param costoPorKm   costo por km en pesos (mayor que cero)
     * @throws IllegalArgumentException si algún dato no es válido
     */
    public Vehiculo(String patente, double capacidadKg, double velocidadKmH, double costoPorKm) {
        setPatente(patente);
        setCapacidadKg(capacidadKg);
        setVelocidadKmH(velocidadKmH);
        setCostoPorKm(costoPorKm);
    }

    /**
     * Calcula el tiempo estimado de entrega, incluyendo el tiempo extra
     * propio de cada tipo de vehículo.
     *
     * @param km distancia en km (mayor que cero)
     * @return el tiempo estimado en minutos
     */
    public abstract double calcularTiempoEntrega(double km);

    /**
     * Indica si el vehículo puede transportar el pedido: el peso no debe
     * superar la capacidad. Las subclases pueden agregar más restricciones.
     *
     * @param pedido pedido a transportar
     * @return true si puede transportarlo
     */
    public boolean puedeTransportar(Pedido pedido) {
        return pedido.getPesoKg() <= capacidadKg;
    }

    /**
     * Tiempo de viaje puro, sin tiempo extra: (distancia / velocidad) * 60.
     * Lo usan las subclases para no repetir la fórmula.
     *
     * @param km distancia en km (mayor que cero)
     * @return minutos de viaje
     * @throws IllegalArgumentException si la distancia no es positiva
     */
    protected double calcularMinutosDeViaje(double km) {
        if (km <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor que cero.");
        }
        return (km / velocidadKmH) * 60;
    }

    public String getPatente() {
        return patente;
    }

    /**
     * @param patente nueva patente, no puede ser nula ni vacía
     * @throws IllegalArgumentException si la patente es nula o vacía
     */
    public void setPatente(String patente) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new IllegalArgumentException("La patente no puede estar vacía.");
        }
        this.patente = patente.trim();
    }

    public double getCapacidadKg() {
        return capacidadKg;
    }

    /**
     * @param capacidadKg nueva capacidad, debe ser mayor que cero
     * @throws IllegalArgumentException si no es positiva
     */
    public void setCapacidadKg(double capacidadKg) {
        if (capacidadKg <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.capacidadKg = capacidadKg;
    }

    public double getVelocidadKmH() {
        return velocidadKmH;
    }

    /**
     * @param velocidadKmH nueva velocidad, debe ser mayor que cero
     * @throws IllegalArgumentException si no es positiva
     */
    public void setVelocidadKmH(double velocidadKmH) {
        if (velocidadKmH <= 0) {
            throw new IllegalArgumentException("La velocidad debe ser mayor que cero.");
        }
        this.velocidadKmH = velocidadKmH;
    }

    public double getCostoPorKm() {
        return costoPorKm;
    }

    /**
     * @param costoPorKm nuevo costo por km, debe ser mayor que cero
     * @throws IllegalArgumentException si no es positivo
     */
    public void setCostoPorKm(double costoPorKm) {
        if (costoPorKm <= 0) {
            throw new IllegalArgumentException("El costo por km debe ser mayor que cero.");
        }
        this.costoPorKm = costoPorKm;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [patente=" + patente
                + ", capacidad=" + capacidadKg + " kg"
                + ", velocidad=" + velocidadKmH + " km/h"
                + ", costo por km=$" + costoPorKm + "]";
    }
}