package modelo;

import java.util.Arrays;
import java.time.LocalDateTime;


import excepciones.AlmacenamientoLlenoException;
import excepciones.ClienteNoEncontradoException;
import excepciones.PedidoInvalidoException;
import excepciones.CapacidadExcedidaException;

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

    // ------------------------------------------------- Búsquedas por código

    /**
     * Busca un pedido por su código.
     *
     * @param codigo código del pedido
     * @return el pedido encontrado
     * @throws PedidoInvalidoException si no existe
     */
    public Pedido buscarPedido(int codigo) throws PedidoInvalidoException {
        int i = indicePedido(codigo);
        if (i == -1) {
            throw new PedidoInvalidoException("No existe un pedido con el código " + codigo + ".");
        }
        return pedidos[i];
    }

    /**
     * Busca un repartidor por su legajo.
     *
     * @param legajo legajo del repartidor
     * @return el repartidor encontrado
     * @throws IllegalArgumentException si no existe
     */
    public Repartidor buscarRepartidor(int legajo) {
        int i = indiceRepartidor(legajo);
        if (i == -1) {
            throw new IllegalArgumentException("No existe un repartidor con el legajo " + legajo + ".");
        }
        return repartidores[i];
    }

    // ------------------------------------------------------------- Operaciones

    /**
     * Asigna un pedido a un repartidor, verificando capacidad y restricciones.
     *
     * @param codigoPedido código del pedido
     * @param legajo       legajo del repartidor
     * @throws PedidoInvalidoException      si el pedido no existe, no está pendiente o ya está asignado
     * @throws CapacidadExcedidaException   si el vehículo no puede transportarlo
     * @throws AlmacenamientoLlenoException si el repartidor tiene el máximo de pedidos
     */
    public void asignarPedido(int codigoPedido, int legajo)
            throws PedidoInvalidoException, CapacidadExcedidaException, AlmacenamientoLlenoException {
        Pedido pedido = buscarPedido(codigoPedido);
        Repartidor repartidor = buscarRepartidor(legajo);
        repartidor.asignarPedido(pedido);
    }

    /**
     * Cambia el estado de un pedido. Para pasar a EN_CAMINO el pedido tiene
     * que estar asignado a un repartidor.
     *
     * @param codigo      código del pedido
     * @param nuevoEstado estado al que se quiere pasar
     * @throws PedidoInvalidoException si no existe o no está asignado
     * @throws excepciones.EstadoInvalidoException si la transición no está permitida
     */
    public void cambiarEstadoPedido(int codigo, EstadoPedido nuevoEstado)
            throws PedidoInvalidoException {
        Pedido pedido = buscarPedido(codigo);
        if (nuevoEstado == EstadoPedido.EN_CAMINO && pedido.getVehiculo() == null) {
            throw new PedidoInvalidoException(
                    "El pedido " + codigo + " no está asignado a ningún repartidor.");
        }
        pedido.cambiarEstado(nuevoEstado);
    }

    /**
     * Arma una línea por pedido con su costo y tiempo estimado. Recorre los
     * pedidos y llama a calcularCosto y calcularTiempoEntrega sin preguntar
     * de qué tipo es cada uno (polimorfismo).
     *
     * @return una línea de texto por cada pedido cargado
     */
    public String[] listarPedidos() {
        String[] lineas = new String[cantPedidos];
        for (int i = 0; i < cantPedidos; i++) {
            Pedido p = pedidos[i];
            Vehiculo v = p.getVehiculo();
            String detalle;
            if (v == null) {
                detalle = "sin vehículo asignado";
            } else {
                detalle = String.format("costo $%.2f, tiempo %.1f min",
                        p.calcularCosto(v), v.calcularTiempoEntrega(p.getDistanciaKm()));
            }
            lineas[i] = p + " -> " + detalle;
        }
        return lineas;
    }

    /**
     * Consulta el estado actual de un pedido (interfaz Rastreable).
     *
     * @throws PedidoInvalidoException si no existe
     */
    public String consultarEstadoPedido(int codigo) throws PedidoInvalidoException {
        Rastreable r = buscarPedido(codigo);
        return r.obtenerEstadoActual();
    }

    /**
     * Consulta el estado actual de un repartidor (interfaz Rastreable).
     *
     * @throws IllegalArgumentException si no existe
     */
    public String consultarEstadoRepartidor(int legajo) {
        Rastreable r = buscarRepartidor(legajo);
        return r.obtenerEstadoActual();
    }

    /**
     * Suma el importe de todos los pedidos (interfaz Facturable). Los pedidos
     * sin vehículo o cancelados aportan 0.
     *
     * @return el importe total en pesos
     */
    public double calcularImporteTotal() {
        double total = 0;
        for (int i = 0; i < cantPedidos; i++) {
            total += pedidos[i].calcularImporte();
        }
        return total;
    }

    // -------------------------------------------------------- Datos de prueba

    /**
     * Carga 5 clientes, 3 repartidores (uno por tipo de vehículo) y 8 pedidos
     * de los tres tipos, algunos asignados y en distintos estados.
     */
    public void cargarDatosDePrueba()
            throws PedidoInvalidoException, AlmacenamientoLlenoException,
                   ClienteNoEncontradoException, CapacidadExcedidaException {
        Cliente ana = new Cliente(1, "Ana Pérez", "2657-111111", "Centro");
        Cliente luis = new Cliente(2, "Luis Gómez", "2657-222222", "Norte");
        Cliente marta = new Cliente(3, "Marta Díaz", "2657-333333", "Sur");
        Cliente carlos = new Cliente(4, "Carlos Ruiz", "2657-444444", "Este");
        Cliente sofia = new Cliente(5, "Sofía López", "2657-555555", "Oeste");
        registrarCliente(ana);
        registrarCliente(luis);
        registrarCliente(marta);
        registrarCliente(carlos);
        registrarCliente(sofia);

        registrarRepartidor(new Repartidor(101, "Juan Torres", new Bicicleta("BICI-01")));
        registrarRepartidor(new Repartidor(102, "Lucía Ferreyra", new Moto("AB123CD")));
        registrarRepartidor(new Repartidor(103, "Pedro Suárez", new Camioneta("AC456EF")));

        registrarPedido(new PedidoNormal(1, ana, "Barrio Norte", 2, 3,
                LocalDateTime.of(2026, 10, 22, 8, 0)));
        registrarPedido(new PedidoNormal(2, luis, "Zona Sur", 8, 4.5,
                LocalDateTime.of(2026, 10, 22, 8, 30)));
        registrarPedido(new PedidoNormal(3, marta, "Microcentro", 25, 6,
                LocalDateTime.of(2026, 10, 22, 9, 0)));
        registrarPedido(new PedidoUrgente(4, carlos, "Parque La Pedrera", 3, 5,
                LocalDateTime.of(2026, 10, 22, 9, 30)));
        registrarPedido(new PedidoUrgente(5, sofia, "Terminal", 12, 8,
                LocalDateTime.of(2026, 10, 22, 10, 0)));
        registrarPedido(new PedidoFragil(6, ana, "Hospital", 4, 3.5,
                LocalDateTime.of(2026, 10, 22, 10, 30)));
        registrarPedido(new PedidoFragil(7, luis, "Universidad", 20, 7,
                LocalDateTime.of(2026, 10, 22, 11, 0)));
        registrarPedido(new PedidoFragil(8, marta, "Zona Industrial", 150, 12,
                LocalDateTime.of(2026, 10, 22, 11, 30)));

        // El pedido 5 queda pendiente y sin asignar.
        asignarPedido(1, 101);
        asignarPedido(2, 101);
        asignarPedido(3, 102);
        asignarPedido(4, 102);
        asignarPedido(6, 102);
        asignarPedido(7, 103);
        asignarPedido(8, 103);

        cambiarEstadoPedido(1, EstadoPedido.EN_CAMINO);
        cambiarEstadoPedido(1, EstadoPedido.ENTREGADO);
        cambiarEstadoPedido(4, EstadoPedido.EN_CAMINO);
        cambiarEstadoPedido(4, EstadoPedido.ENTREGADO);
        cambiarEstadoPedido(8, EstadoPedido.EN_CAMINO);
        cambiarEstadoPedido(8, EstadoPedido.ENTREGADO);
        cambiarEstadoPedido(3, EstadoPedido.EN_CAMINO);
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