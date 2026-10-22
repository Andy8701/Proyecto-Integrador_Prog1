package modelo;

/**
 * Representa a un cliente de CityLink que realiza pedidos de envío.
 * Todos los atributos son privados y los setters validan los datos.
 */
public class Cliente {

    private int codigo;
    private String nombre;
    private String telefono;
    private String zona;

    /**
     * Constructor vacío. Deja los atributos con sus valores por defecto.
     */
    public Cliente() {
    }

    /**
     * Constructor completo. Usa los setters para que los datos se validen.
     *
     * @param codigo   código del cliente (mayor que cero)
     * @param nombre   nombre del cliente (no vacío)
     * @param telefono teléfono del cliente (no vacío)
     * @param zona     zona de la ciudad donde vive (no vacía)
     * @throws IllegalArgumentException si algún dato no es válido
     */
    public Cliente(int codigo, String nombre, String telefono, String zona) {
        setCodigo(codigo);
        setNombre(nombre);
        setTelefono(telefono);
        setZona(zona);
    }

    /**
     * @return el código del cliente
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo nuevo código, debe ser mayor que cero
     * @throws IllegalArgumentException si el código no es positivo
     */
    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser mayor que cero.");
        }
        this.codigo = codigo;
    }

    /**
     * @return el nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre nuevo nombre, no puede ser nulo ni vacío
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    public void setNombre(String nombre) {
        if (nombre == null ||