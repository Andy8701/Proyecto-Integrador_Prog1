package modelo;

import java.time.LocalDateTime;

import excepciones.PedidoInvalidoException;

/**
 * Pedido urgente: suma un 50% sobre el costo base.
 */
public class PedidoUrgente extends Pedido {

    public static final double RECARGO = 0.5;

    public PedidoUrgente(int codigo, Cliente cliente, String destino,
                         double pesoKg, double distanciaKm) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm);
    }

    public PedidoUrgente(int codigo, Cliente cliente, String destino, double pesoKg,
                         double distanciaKm, LocalDateTime fechaHora) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm, fechaHora);
    }

    @Override
    public double calcularCosto(Vehiculo vehiculo) {
        return calcularCostoBase(vehiculo) * (1 + RECARGO);
    }

    @Override
    public boolean esUrgente() {
        return true;
    }
}