package modelo;

import java.time.LocalDateTime;

import excepciones.PedidoInvalidoException;

/**
 * Pedido frágil: suma un adicional fijo de $800 sobre el costo base.
 * No puede viajar en bicicleta.
 */
public class PedidoFragil extends Pedido {

    public static final double RECARGO_FIJO = 800;

    public PedidoFragil(int codigo, Cliente cliente, String destino,
                        double pesoKg, double distanciaKm) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm);
    }

    public PedidoFragil(int codigo, Cliente cliente, String destino, double pesoKg,
                        double distanciaKm, LocalDateTime fechaHora) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm, fechaHora);
    }

    @Override
    public double calcularCosto(Vehiculo vehiculo) {
        return calcularCostoBase(vehiculo) + RECARGO_FIJO;
    }

    @Override
    public boolean esFragil() {
        return true;
    }
}