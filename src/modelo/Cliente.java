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
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    /**
     * @return el teléfono del cliente
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono nuevo teléfono, no puede ser nulo ni vacío
     * @throws IllegalArgumentException si el teléfono es nulo o vacío
     */
    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }
        this.telefono = telefono.trim();
    }

    /**
     * @return la zona de la ciudad del cliente
     */
    public String getZona() {
        return zona;
    }

    /**
     * @param zona nueva zona, no puede ser nula ni vacía
     * @throws IllegalArgumentException si la zona es nula o vacía
     */
    public void setZona(String zona) {
        if (zona == null || zona.trim().isEmpty()) {
            throw new IllegalArgumentException("La zona no puede estar vacía.");
        }
        this.zona = zona.trim();
    }

    @Override
    public String toString() {
        return "Cliente [código=" + codigo + ", nombre=" + nombre
                + ", teléfono=" + telefono + ", zona=" + zona + "]";
    }
}