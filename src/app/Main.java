package app;

import java.util.Scanner;

import excepciones.AlmacenamientoLlenoException;
import excepciones.CapacidadExcedidaException;
import excepciones.ClienteNoEncontradoException;
import excepciones.EstadoInvalidoException;
import excepciones.PedidoInvalidoException;
import modelo.Bicicleta;
import modelo.Camioneta;
import modelo.CityLink;
import modelo.Cliente;
import modelo.EstadoPedido;
import modelo.Moto;
import modelo.Pedido;
import modelo.PedidoFragil;
import modelo.PedidoNormal;
import modelo.PedidoUrgente;
import modelo.Repartidor;
import modelo.Vehiculo;

/**
 * Menú de consola de CityLink. Solo se ocupa de leer por teclado, mostrar
 * resultados y capturar los errores que lanzan el gestor y el modelo.
 */
public class Main {

    private static final Scanner teclado = new Scanner(System.in);
    private static final CityLink gestor = new CityLink();

    public static void main(String[] args) {
        try {
            gestor.cargarDatosDePrueba();
            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero("Opción: ");
                try {
                    ejecutar(opcion);
                } catch (ClienteNoEncontradoException | PedidoInvalidoException
                         | CapacidadExcedidaException | AlmacenamientoLlenoException
                         | EstadoInvalidoException | IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } while (opcion != 10);
        } catch (ClienteNoEncontradoException | PedidoInvalidoException
                 | CapacidadExcedidaException | AlmacenamientoLlenoException e) {
            System.out.println("No se pudieron cargar los datos de prueba: " + e.getMessage());
        } finally {
            teclado.close();
            System.out.println("Programa finalizado.");
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== CityLink =====");
        System.out.println(" 1. Registrar cliente");
        System.out.println(" 2. Registrar repartidor con su vehículo");
        System.out.println(" 3. Registrar pedido");
        System.out.println(" 4. Asignar un pedido a un repartidor");
        System.out.println(" 5. Cambiar el estado de un pedido");
        System.out.println(" 6. Buscar un cliente");
        System.out.println(" 7. Listar pedidos con costo y tiempo estimado");
        System.out.println(" 8. Consultar el estado de un pedido o repartidor");
        System.out.println(" 9. Ver importe total y comisión de cada repartidor");
        System.out.println("10. Salir");
    }

    private static void ejecutar(int opcion)
            throws ClienteNoEncontradoException, PedidoInvalidoException,
                   CapacidadExcedidaException, AlmacenamientoLlenoException {
        switch (opcion) {
            case 1: registrarCliente(); break;
            case 2: registrarRepartidor(); break;
            case 3: registrarPedido(); break;
            case 4: asignarPedido(); break;
            case 5: cambiarEstado(); break;
            case 6: buscarCliente(); break;
            case 7: listarPedidos(); break;
            case 8: consultarEstado(); break;
            case 9: verTotales(); break;
            case 10: System.out.println("Saliendo..."); break;
            default: System.out.println("Opción inválida.");
        }
    }

    // ---------------------------------------------------------------- Opciones

    private static void registrarCliente() throws AlmacenamientoLlenoException {
        int codigo = leerEntero("Código: ");
        String nombre = leerTexto("Nombre: ");
        String telefono = leerTexto("Teléfono: ");
        String zona = leerTexto("Zona: ");
        gestor.registrarCliente(new Cliente(codigo, nombre, telefono, zona));
        System.out.println("Cliente registrado.");
    }

    private static void registrarRepartidor() throws AlmacenamientoLlenoException {
        int legajo = leerEntero("Legajo: ");
        String nombre = leerTexto("Nombre: ");
        int tipo = leerEntero("Vehículo (1=Bicicleta, 2=Moto, 3=Camioneta): ");
        String patente = leerTexto("Patente: ");
        Vehiculo vehiculo;
        switch (tipo) {
            case 1: vehiculo = new Bicicleta(patente); break;
            case 2: vehiculo = new Moto(patente); break;
            case 3: vehiculo = new Camioneta(patente); break;
            default: throw new IllegalArgumentException("Tipo de vehículo inválido.");
        }
        gestor.registrarRepartidor(new Repartidor(legajo, nombre, vehiculo));
        System.out.println("Repartidor registrado.");
    }

    private static void registrarPedido()
            throws ClienteNoEncontradoException, PedidoInvalidoException,
                   AlmacenamientoLlenoException {
        int tipo = leerEntero("Tipo (1=Normal, 2=Urgente, 3=Frágil): ");
        int codigo = leerEntero("Código del pedido: ");
        int codigoCliente = leerEntero("Código del cliente: ");
        String destino = leerTexto("Destino: ");
        double peso = leerDecimal("Peso (kg): ");
        double distancia = leerDecimal("Distancia (km): ");

        Cliente cliente = gestor.buscarCliente(codigoCliente);
        Pedido pedido;
        switch (tipo) {
            case 1: pedido = new PedidoNormal(codigo, cliente, destino, peso, distancia); break;
            case 2: pedido = new PedidoUrgente(codigo, cliente, destino, peso, distancia); break;
            case 3: pedido = new PedidoFragil(codigo, cliente, destino, peso, distancia); break;
            default: throw new IllegalArgumentException("Tipo de pedido inválido.");
        }
        gestor.registrarPedido(pedido);
        System.out.println("Pedido registrado.");
    }

    private static void asignarPedido()
            throws PedidoInvalidoException, CapacidadExcedidaException,
                   AlmacenamientoLlenoException {
        int codigo = leerEntero("Código del pedido: ");
        int legajo = leerEntero("Legajo del repartidor: ");
        gestor.asignarPedido(codigo, legajo);
        System.out.println("Pedido asignado.");
    }

    private static void cambiarEstado() throws PedidoInvalidoException {
        int codigo = leerEntero("Código del pedido: ");
        int opcion = leerEntero("Nuevo estado (1=En camino, 2=Entregado, 3=Cancelado): ");
        EstadoPedido nuevo;
        switch (opcion) {
            case 1: nuevo = EstadoPedido.EN_CAMINO; break;
            case 2: nuevo = EstadoPedido.ENTREGADO; break;
            case 3: nuevo = EstadoPedido.CANCELADO; break;
            default: throw new IllegalArgumentException("Estado inválido.");
        }
        gestor.cambiarEstadoPedido(codigo, nuevo);
        System.out.println("Estado actualizado.");
    }

    private static void buscarCliente() throws ClienteNoEncontradoException {
        int criterio = leerEntero("Buscar por (1=Código, 2=Nombre): ");
        Cliente encontrado;
        if (criterio == 1) {
            encontrado = gestor.buscarCliente(leerEntero("Código: "));
        } else if (criterio == 2) {
            encontrado = gestor.buscarCliente(leerTexto("Nombre: "));
        } else {
            throw new IllegalArgumentException("Criterio inválido.");
        }
        System.out.println(encontrado);
    }

    private static void listarPedidos() {
        String[] lineas = gestor.listarPedidos();
        if (lineas.length == 0) {
            System.out.println("No hay pedidos cargados.");
        }
        for (String linea : lineas) {
            System.out.println(linea);
        }
    }

    private static void consultarEstado() throws PedidoInvalidoException {
        int opcion = leerEntero("Consultar (1=Pedido, 2=Repartidor): ");
        if (opcion == 1) {
            System.out.println(gestor.consultarEstadoPedido(leerEntero("Código del pedido: ")));
        } else if (opcion == 2) {
            System.out.println(gestor.consultarEstadoRepartidor(leerEntero("Legajo: ")));
        } else {
            throw new IllegalArgumentException("Opción inválida.");
        }
    }

    private static void verTotales() {
        System.out.printf("Importe total de los pedidos: $%.2f%n", gestor.calcularImporteTotal());
        for (Repartidor r : gestor.getRepartidores()) {
            System.out.printf("Comisión de %s (legajo %d): $%.2f%n",
                    r.getNombre(), r.getLegajo(), r.calcularImporte());
        }
    }

    // ----------------------------------------------------------------- Lectura

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número entero válido.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número válido.");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }
}