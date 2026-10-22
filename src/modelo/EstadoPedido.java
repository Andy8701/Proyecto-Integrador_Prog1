package modelo;

/**
 * Estados posibles de un pedido.
 * Flujo válido: PENDIENTE → EN_CAMINO → ENTREGADO.
 * CANCELADO solo se alcanza desde PENDIENTE.
 */
public enum EstadoPedido {
    PENDIENTE,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO
}