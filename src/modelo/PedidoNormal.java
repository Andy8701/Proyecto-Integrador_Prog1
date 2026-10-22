package modelo;

import java.time.LocalDateTime;

import excepciones.PedidoInvalidoException;

/**
 * Pedido normal: no tiene recargo.
 */
public class PedidoNormal extends Pedido {

    public PedidoNormal(int codigo, Cliente cliente, String destino,
                        double pesoKg, double distanciaKm) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm);
    }

    public PedidoNormal(int codigo, Cliente cliente, String destino, double pesoKg,
                        double distanciaKm, LocalDateTime fechaHora) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm, fechaHora);
    }

    @Override
    public double calcularCosto(Vehiculo vehiculo) {
        return calcularCostoBase(vehiculo);
    }
}