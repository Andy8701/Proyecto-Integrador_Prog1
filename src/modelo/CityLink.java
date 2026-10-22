package modelo;

import java.util.Arrays;

import excepciones.AlmacenamientoLlenoException;
import excepciones.ClienteNoEncontradoException;
import excepciones.PedidoInvalidoException;

/**
 * Gestor del sistema CityLink. Guarda clientes, pedidos y repartidores en
 * arreglos de tamaño fijo y ofrece las operaciones del negocio.
 * No lee por teclado ni imprime: los errores se informan con excepciones
 * y los captura la capa de menú.
 */
public class CityLink {

    /** Capacidad máxima de clientes. */
    public static final int MAX_CLIENTES = 50;
    /** Capacidad máxima de pedidos. */
    public static final int MAX_PEDIDOS = 100;
    /** Capacidad máxima de repartidores. */
    public static final int MAX_REPARTIDORES = 10;

    private Cliente[] clientes = new Cliente[MAX_CLIENTES];
    private int cantClientes = 0;

    private Pedido[] pedidos = new Pedido[MAX_PEDIDOS];
    private int cantPedidos = 0;

    private Repartidor[] repartidores = new Repartidor[MAX_REPARTIDORES];
    private int cantRepartidores = 0;

    // ---------------------------------------------------------------- Clientes

    /**
     * Registra un cliente nuevo.
     *
     * @param cliente cliente completo (código positivo) y con código no repetido
     * @throws AlmacenamientoLlenoException si ya hay {@value #MAX_CLIENTES} clientes
     * @throws IllegalArgumentException     si el cliente es nulo, está incompleto
     *                                      o su código ya existe
     */
    public void registrarCliente(Cliente cliente) throws AlmacenamientoLlenoException {
        if (cliente == null || cliente.getCodigo() <= 0) {
            throw new IllegalArgumentException("El cliente está incompleto.");
        }
        if (cantClientes == MAX_CLIENTES) {
            throw new AlmacenamientoLlenoException(
                    "No se pueden registrar más clientes (máximo " + MAX_CLIENTES + ").");
        }
        if (indiceCliente(cliente.getCodigo()) != -1) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con el código " + cliente.getCodigo() + ".");
        }
        clientes[cantClientes] = cliente;
        cantClientes++;
    }

    /**
     * Busca un cliente por su código.
     *
     * @param codigo código del cliente
     * @return el cliente encontrado
     * @throws ClienteNoEncontradoException si no existe
     */
    public Cliente buscarCliente(int codigo) throws ClienteNoEncontradoException {
        int i = indiceCliente(codigo);
        if (i == -1) {
            throw new ClienteNoEncontradoException(
                    "No existe un cliente con el código " + codigo + ".");
        }
        return clientes[i];
    }

    /**
     * Busca un cliente por su nombre, sin distinguir mayúsculas de minúsculas.
     * Si hay varios con el mismo nombre, devuelve el primero registrado.
     *
     * @param nombre nombre del cliente
     * @return el cliente encontrado
     * @throws ClienteNoEncontradoException si no existe
     */
    public Cliente buscarCliente(String nombre) throws ClienteNoEncontradoException {
        if (nombre != null) {
            String buscado = nombre.trim();
            for (int i = 0; i < cantClientes; i++) {
                if (clientes[i].getNombre().equalsIgnoreCase(buscado)) {
                    return clientes[i];
                }
            }
        }
        throw new ClienteNoEncontradoException(
                "No existe un cliente llamado \"" + nombre + "\".");
    }

    /**
     * Elimina un cliente y desplaza los siguientes una posición hacia atrás
     * para no dejar huecos.
     *
     * @param codigo código del cliente a eliminar
     * @return el cliente eliminado
     * @throws ClienteNoEncontradoException si no existe
     */
    public Cliente eliminarCliente(int codigo) throws ClienteNoEncontradoException {
        int posicion = indiceCliente(codigo);
        if (posicion == -1) {
            throw new ClienteNoEncontradoException(
                    "No existe un cliente con el código " + codigo + ".");
        }
        Cliente eliminado = clientes[posicion];
        for (int i = posicion; i < cantClientes - 1; i++) {
            clientes[i] = clientes[i + 1];
        }
        clientes[cantClientes - 1] = null;
        cantClientes--;
        return eliminado;
    }

    private int indiceCliente(int codigo) {
        for (int i = 0; i < cantClientes; i++) {
            if (clientes[i].getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    // ------------------------------------------------------------- Repartidores

    /**
     * Registra un repartidor nuevo.
     *
     * @param repartidor repartidor con legajo no repetido
     * @throws AlmacenamientoLlenoException si ya hay {@value #MAX_REPARTIDORES} repartidores
     * @throws IllegalArgumentException     si es nulo o su legajo ya existe
     */
    public void registrarRepartidor(Repartidor repartidor) throws AlmacenamientoLlenoException {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo.");
        }
        if (cantRepartidores == MAX_REPARTIDORES) {
            throw new AlmacenamientoLlenoException(
                    "No se pueden registrar más repartidores (máximo " + MAX_REPARTIDORES + ").");
        }
        if (indiceRepartidor(repartidor.getLegajo()) != -1) {
            throw new IllegalArgumentException(
                    "Ya existe un repartidor con el legajo " + repartidor.getLegajo() + ".");
        }
        repartidores[cantRepartidores] = repartidor;
        cantRepartidores++;
    }

    private int indiceRepartidor(int legajo) {
        for (int i = 0; i < cantRepartidores; i++) {
            if (repartidores[i].getLegajo() == legajo) {
                return i;
            }
        }
        return -1;
    }

    // ------------------------------------------------------------------ Pedidos

    /**
     * Registra un pedido nuevo. Verifica que haya lugar, que el código no se
     * repita y que el cliente del pedido esté registrado.
     *
     * @param pedido pedido ya construido (el constructor valida peso, distancia y destino)
     * @throws PedidoInvalidoException      si es nulo o su código ya existe
     * @throws AlmacenamientoLlenoException si ya hay {@value #MAX_PEDIDOS} pedidos
     * @throws ClienteNoEncontradoException si el cliente del pedido no está registrado
     */
    public void registrarPedido(Pedido pedido)
            throws PedidoInvalidoException, AlmacenamientoLlenoException, ClienteNoEncontradoException {
        if (pedido == null) {
            throw new PedidoInvalidoException("El pedido no puede ser nulo.");
        }
        if (cantPedidos == MAX_PEDIDOS) {
            throw new AlmacenamientoLlenoException(
                    "No se pueden registrar más pedidos (máximo " + MAX_PEDIDOS + ").");
        }
        if (indicePedido(pedido.getCodigo()) != -1) {
            throw new PedidoInvalidoException(
                    "Ya existe un pedido con el código " + pedido.getCodigo() + ".");
        }
        // Lanza ClienteNoEncontradoException si el cliente no está registrado.
        buscarCliente(pedido.getCliente().getCodigo());

        pedidos[cantPedidos] = pedido;
        cantPedidos++;
    }

    /**
     * Elimina un pedido y desplaza los siguientes para no dejar huecos.
     *
     * @param codigo código del pedido a eliminar
     * @return el pedido eliminado
     * @throws PedidoInvalidoException si no existe un pedido con ese código
     */
    public Pedido eliminarPedido(int codigo) throws PedidoInvalidoException {
        int posicion = indicePedido(codigo);
        if (posicion == -1) {
            throw new PedidoInvalidoException("No existe un pedido con el código " + codigo + ".");
        }
        Pedido eliminado = pedidos[posicion];
        for (int i = posicion; i < cantPedidos - 1; i++) {
            pedidos[i] = pedidos[i + 1];
        }
        pedidos[cantPedidos - 1] = null;
        cantPedidos--;
        return eliminado;
    }

    private int indicePedido(int codigo) {
        for (int i = 0; i < cantPedidos; i++) {
            if (pedidos[i].getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    // ------------------------------------------------------------------ Consulta

    /** @return una copia de los clientes cargados, sin casilleros vacíos */
    public Cliente[] getClientes() {
        return Arrays.copyOf(clientes, cantClientes);
    }

    /** @return una copia de los pedidos cargados, sin casilleros vacíos */
    public Pedido[] getPedidos() {
        return Arrays.copyOf(pedidos, cantPedidos);
    }

    /** @return una copia de los repartidores cargados, sin casilleros vacíos */
    public Repartidor[] getRepartidores() {
        return Arrays.copyOf(repartidores, cantRepartidores);
    }

    public int getCantClientes() {
        return cantClientes;
    }

    public int getCantPedidos() {
        return cantPedidos;
    }

    public int getCantRepartidores() {
        return cantRepartidores;
    }
}