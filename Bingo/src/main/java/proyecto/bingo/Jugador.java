package proyecto.bingo;

/**
 * Representación de un jugador de bingo.
 */
public class Jugador {

    private final String nombre;
    private final String cedula;
    private final String correo;

    /**
     * Constructor de la clase Jugador que crea una instancia de jugador con los
     * siguientes atributos: nombre, cédula y correo.
     *
     * @param pNombre Nombre completo del jugador.
     * @param pCedula Cédula del jugador.
     * @param pCorreo Correo electrónico del jugador.
     */
    public Jugador(String pNombre, String pCedula, String pCorreo) {
        this.nombre = pNombre;
        this.cedula = pCedula;
        this.correo = pCorreo;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return El nombre del jugador en formato String.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la cédula del jugador.
     *
     * @return La cédula del jugador en formato String.
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * Obtiene el correo electrónico del jugador.
     *
     * @return El correo electrónico del jugador en formato String.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Representación en formato CSV del jugador.
     *
     * @return Una cadena en formato CSV que contiene nombre, cédula y correo
     * del jugador.
     */
    @Override
    public String toString() {
        return nombre + "," + cedula + "," + correo;
    }
}
