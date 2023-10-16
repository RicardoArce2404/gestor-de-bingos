package proyecto.bingo;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.TextAlignment;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Esta clase representa un programa para generar cartones de bingo y crear
 * archivos PDF para cada uno.
 */
public class Bingo {

    // Lista para almacenar los cartones generados.
    private static final List<CartonBingo> cartones = new ArrayList<>();

    // Lista para almacenar los jugadores registrados.
    private static final List<Jugador> jugadores = new ArrayList<>();

    // Agrega esta constante para definir el tamaño del cartón de bingo
    private static final int TAMANO_CARTON = 5;
    private static final List<Integer> numerosLlamados = new ArrayList<>();

    /**
     * El método principal del programa.
     *
     * @param args Los argumentos de línea de comandos (no se utilizan en este
     * caso).
     * @throws MalformedURLException Si la URL de la imagen es incorrecta.
     * @throws FileNotFoundException Si no se puede encontrar la ubicación de
     * destino para el archivo PDF.
     */
    public static void main(String[] args) throws MalformedURLException, FileNotFoundException {
        try (Scanner scanner = new Scanner(System.in)) {

            cargarJugadoresDesdeCSV(); // Cargar jugadores desde el archivo CSV

            while (true) {
                mostrarMenuPrincipal();

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consume el salto de línea

                if (opcion == 1) {
                    registrarJugador(scanner);
                } else if (opcion == 2) {
                    generarYMostrarCartones(scanner);
                } else if (opcion == 3) {
                    mostrarListaDeCartonesConIdentificadores();
                } else if (opcion == 4) {
                    buscarYMostrarCarton(scanner);
                } else if (opcion== 5) {
                  enviarCorreo(scanner);
                } else if (opcion == 6) {
                    iniciarJuegoDeBingo(scanner);
                } else if (opcion == 7) {
                    guardarJugadoresEnCSV(); // Guardar jugadores en el archivo CSV
                    System.out.println("Gracias por jugar. ¡Hasta luego!");
                    break;
                } else {
                    System.out.println("Opción no válida. Por favor, elija una opción válida.");
                }
            }
        }
    }
// Actualizar el método para iniciar el juego de bingo

    public static void iniciarJuegoDeBingo(Scanner scanner) {
        System.out.println("Elija la configuración de juego:");
        System.out.println("1. Jugar en X");
        System.out.println("2. Cuatro esquinas");
        System.out.println("3. Cartón lleno");
        System.out.println("4. Jugar en Z");
        System.out.print("Elija una opción: ");
        int configuracionJuego = scanner.nextInt();
        scanner.nextLine();

        if (configuracionJuego >= 1 && configuracionJuego <= 4) {
            jugarBingo(configuracionJuego);
        } else {
            System.out.println("Opción no válida. Por favor, elija una configuración de juego válida.");
        }
    }

    public static void jugarBingo(int configuracionJuego) {
        // Lógica para el juego de bingo
        CartonBingo cartonGanador = null;
        boolean juegoEnCurso = true;

        while (juegoEnCurso) {
            // Generar un número aleatorio y verificar que no se haya llamado antes
            int numeroLlamado;
            do {
                numeroLlamado = generarNumeroAleatorio(1, 75);
            } while (numerosLlamados.contains(numeroLlamado));

            numerosLlamados.add(numeroLlamado); // Agregar el número llamado a la lista

            System.out.println("Número llamado: " + numeroLlamado);

            // Verificar si algún cartón tiene el número llamado
            for (CartonBingo carton : cartones) {
                if (carton.tieneNumero(numeroLlamado)) {
                    System.out.println("Cartón " + carton.getNumeroCarton() + " tiene el número " + numeroLlamado);
                    // Aquí es donde verificamos si el cartón es ganador según la configuración específica
                    if (configuracionJuego == 1 && carton.tieneConfiguracionX()) {
                        cartonGanador = carton;
                        juegoEnCurso = false;
                        break;
                    } else if (configuracionJuego == 2 && carton.tieneCuatroEsquinas()) {
                        cartonGanador = carton;
                        juegoEnCurso = false;
                        break;
                    } else if (configuracionJuego == 3 && carton.tieneCartonLleno()) {
                        cartonGanador = carton;
                        juegoEnCurso = false;
                        break;
                    } else if (configuracionJuego == 4 && carton.tieneConfiguracionZ()) {
                        cartonGanador = carton;
                        juegoEnCurso = false;
                        break;
                    }
                }
            }

            if (cartonGanador != null) {
                System.out.println("¡Tenemos un ganador! Cartón " + cartonGanador.getNumeroCarton());
                break;
            }
        }
    }
    
    public static void enviarCorreo(Scanner scanner){
      
      System.out.println("Ingrese el correo:");
      String correo = scanner.nextLine();
      System.out.println("Ingrese la cantidad de cartones (Entre 1 y 5):");
      int cantidad = scanner.nextInt();
      enviarCartonCorreo("sermonbadi@gmail.com", correo, cantidad);
    }
    // Genera un número aleatorio en el rango especificado
    public static int generarNumeroAleatorio(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    // Método para mostrar la lista de cartones con identificadores
    public static void mostrarListaDeCartonesConIdentificadores() {
        System.out.println("Lista de Cartones Disponibles: \n");
        for (int i = 0; i < cartones.size(); i++) {
            CartonBingo carton = cartones.get(i);
            System.out.println("Carton " + (i + 1) + ": Identificador: " + carton.getCodigoUnico());
        }
    }

    /**
     * Genera un código único de tres letras y tres números.
     *
     * @return Un código único en formato String.
     */
    public static String generarCodigoUnico() {
        StringBuilder codigo = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            char letra = (char) (rand.nextInt(26) + 'A');
            codigo.append(letra);
        }

        for (int i = 0; i < 3; i++) {
            int numero = rand.nextInt(10);
            codigo.append(numero);
        }

        return codigo.toString();
    }

    /**
     * Genera un cartón de bingo con números aleatorios y un código único.
     *
     * @param codigoUnico El código único del cartón.
     * @return Un objeto CartonBingo que representa el cartón generado.
     */
    public static CartonBingo generarTarjetaDeBingoConCodigoUnico(String codigoUnico) {
        int[][] tarjeta = new int[5][5];
        ArrayList<Integer> numerosUtilizados = new ArrayList<>();
        boolean valido = false;
        int tmp = 0;

        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j < 5; j++) {
                while (!valido) {
                    tmp = (int) (Math.random() * 15) + 1 + 15 * i;
                    if (!numerosUtilizados.contains(tmp)) {
                        valido = true;
                        numerosUtilizados.add(tmp);
                    }
                }
                tarjeta[j][i] = tmp;
                valido = false;
            }
        }

        return new CartonBingo(codigoUnico, tarjeta);
    }

    /**
     * Crea un archivo PDF para el cartón de bingo.
     *
     * @param carton El objeto CartonBingo que representa el cartón.
     * @param numeroCarton El número del cartón.
     * @throws MalformedURLException Si la URL de la imagen es incorrecta.
     * @throws FileNotFoundException Si no se puede encontrar la ubicación de
     * destino para el archivo PDF.
     */
    public static void crearArchivoPDF(CartonBingo carton, int numeroCarton) throws MalformedURLException, FileNotFoundException {
        String rutaCarpetaCartones = "C:\\Users\\User\\OneDrive - Estudiantes ITCR\\Escritorio\\TEC\\POO\\Proyecto1\\Bingo\\Cartones";
        File carpetaCartones = new File(rutaCarpetaCartones);

        if (!carpetaCartones.exists()) {
            carpetaCartones.mkdirs();
        }

        String nombreArchivoPDF = rutaCarpetaCartones + "\\" + carton.getCodigoUnico() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(nombreArchivoPDF));
            PdfDocument pdfDoc = new PdfDocument(writer);

            try (Document doc = new Document(pdfDoc)) {
                Table table = new Table(UnitValue.createPercentArray(new float[]{20, 20, 20, 20, 20}));
                table.setWidth(UnitValue.createPercentValue(100));

                // Agregar encabezados de las columnas
                table.addCell("-             B            -");
                table.addCell("-             I            -");
                table.addCell("-             N          -");
                table.addCell("-             G          -");
                table.addCell("-             O          -");

                // Agregar los números del cartón a la tabla
                for (int[] fila : carton.getNumeros()) {
                    for (int numero : fila) {
                        table.addCell(Integer.toString(numero));
                    }
                }

                String rutaImagen = "C:\\Users\\User\\OneDrive - Estudiantes ITCR\\Escritorio\\TEC\\POO\\Proyecto1\\Bingo\\src\\main\\java\\proyecto\\bingo\\Bingo.png";
                Image imagen = new Image(ImageDataFactory.create(rutaImagen));
                imagen.setFixedPosition(pdfDoc.getDefaultPageSize().getWidth() - 100, pdfDoc.getDefaultPageSize().getHeight() - 100);
                doc.add(imagen);
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(table);
                doc.add(new Paragraph("- Identificador: " + carton.getCodigoUnico()).setTextAlignment(TextAlignment.RIGHT));
            }

            System.out.println("\n Archivo PDF generado: " + nombreArchivoPDF);
        } catch (IOException e) {
            System.err.println("\n Error al crear el archivo PDF: " + e.getMessage());
        }
    }

    /**
     * Muestra la lista de cartones disponibles.
     */
    public static void mostrarListaDeCartones() {
        System.out.println("Lista de Cartones Disponibles: \n");
        for (int i = 0; i < cartones.size(); i++) {
            CartonBingo carton = cartones.get(i);
            System.out.println("Carton : " + (i + 1) + ". Identificador: " + carton.getCodigoUnico());
        }
    }

    /**
     * Busca un cartón por su código único.
     *
     * @param codigo El código único del cartón a buscar.
     * @return El objeto CartonBingo si se encuentra, o null si no se encuentra.
     */
    public static CartonBingo buscarCartonPorCodigo(String codigo) {
        for (CartonBingo carton : cartones) {
            if (carton.getCodigoUnico().equalsIgnoreCase(codigo)) {
                return carton;
            }
        }
        return null;
    }

    /**
     * Busca un cartón por su código único y lo muestra en la consola.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     */
    public static void buscarYMostrarCarton(Scanner scanner) {
        System.out.print("Ingrese el identificador del cartón que desea buscar: ");
        String codigo = scanner.nextLine();

        CartonBingo carton = buscarCartonPorCodigo(codigo);

        if (carton != null) {
            mostrarCartonEnConsola(carton);
        } else {
            System.out.println("No se encontró ningún cartón con el identificador proporcionado.");
        }
    }

    /**
     * Muestra la información de un cartón en la consola.
     *
     * @param carton El objeto CartonBingo que representa el cartón a mostrar.
     */
    public static void mostrarCartonEnConsola(CartonBingo carton) {
        System.out.println("Identificador de Cartón: " + carton.getCodigoUnico());
        System.out.println("Contenido del Cartón:");
        int[][] numeros = carton.getNumeros();
        System.out.print("B\tI\tN\tG\tO\n");
        for (int[] fila : numeros) {
            for (int numero : fila) {
                System.out.print(numero + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Muestra el menú principal.
     */
    public static void mostrarMenuPrincipal() {
        System.out.println("\n----- Menú Principal -----");
        System.out.println("1. Registrar jugador");
        System.out.println("2. Generar y mostrar cartones");
        System.out.println("3. Lista de cartones creados");
        System.out.println("4. Buscar cartón por identificador");
        System.out.println("5. Enviar cartones");
        System.out.println("6. Iniciar juego");
        System.out.println("7. Salir");
        System.out.print("Elija una opción: ");
    }

    /**
     * Registra un nuevo jugador con nombre, cédula y correo.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     */
    public static void registrarJugador(Scanner scanner) {
        System.out.println("----- Registrar Jugador -----");
        System.out.print("Nombre completo (máximo 50 caracteres): ");
        String nombre = scanner.nextLine();

        // Validar que el nombre no exceda los 50 caracteres
        if (nombre.length() > 50) {
            System.out.println("Error: El nombre no puede exceder los 50 caracteres.");
            return;
        }

        // Validar que la cédula no se repita
        String cedula;
        do {
            System.out.print("Cédula (7 caracteres, no se repite entre los jugadores): ");
            cedula = scanner.nextLine();
        } while (cedulaRepetida(cedula) || cedula.length() != 7);

        System.out.print("Correo electrónico (debe contener '@'): ");
        String correo = scanner.nextLine();

        // Validar que el correo contenga el carácter '@'
        if (!correo.contains("@")) {
            System.out.println("Error: El correo electrónico debe contener el carácter '@'.");
            return;
        }

        Jugador nuevoJugador = new Jugador(nombre, cedula, correo);
        jugadores.add(nuevoJugador);
        System.out.println("Jugador registrado con éxito.");
    }

    /**
     * Genera y muestra los cartones de bingo.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     * @throws java.net.MalformedURLException
     * @throws java.io.FileNotFoundException
     */
    public static void generarYMostrarCartones(Scanner scanner) throws MalformedURLException, FileNotFoundException {
        System.out.print("Recuerda que el rango de creación es de 1 a 500 cartones\n");
        System.out.print("Ingrese la cantidad de cartones de bingo a crear: ");
        int cantidadCartones = scanner.nextInt();

        // Validar que la cantidad esté dentro del rango 1-500
        if (cantidadCartones < 1 || cantidadCartones > 500) {
            System.out.println("Error: Cantidad es inválida (debe estar en el rango de 1 a 500).");
        } else {
            // Generar y mostrar la cantidad de cartones especificada
            for (int i = 1; i <= cantidadCartones; i++) {
                System.out.println("Cartón " + i + ":");
                String codigoUnico = generarCodigoUnico();
                CartonBingo carton = generarTarjetaDeBingoConCodigoUnico(codigoUnico);
                mostrarCartonEnConsola(carton);
                cartones.add(carton);
                crearArchivoPDF(carton, i);
                System.out.println(); // Separador entre cartones
            }
        }
    }

    /**
     * Carga los jugadores desde un archivo CSV.
     */
    public static void cargarJugadoresDesdeCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("jugadores.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String nombre = parts[0];
                    String cedula = parts[1];
                    String correo = parts[2];
                    Jugador jugador = new Jugador(nombre, cedula, correo);
                    jugadores.add(jugador);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los jugadores desde el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Guarda los jugadores en un archivo CSV.
     */
    public static void guardarJugadoresEnCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("jugadores.csv"))) {
            for (Jugador jugador : jugadores) {
                writer.write(jugador.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los jugadores en el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Verifica si la cédula está repetida entre los jugadores.
     *
     * @param cedula La cédula a verificar.
     * @return true si la cédula está repetida, false en caso contrario.
     */
    public static boolean cedulaRepetida(String cedula) {
        for (Jugador jugador : jugadores) {
            if (jugador.getCedula().equals(cedula)) {
                System.out.println("Error: La cédula ya está registrada para otro jugador.");
                return true;
            }
        }
        return false;
    }
    
    public static void enviarCartonCorreo(String correoUsuario, String correoDestinatario , int cantidad) {
        Random aleatorio = new Random();
        String[] imagenesBingo = new String[cantidad];
        for (int i=0; i<cantidad; i+=1) {
            int indiceRandom = aleatorio.nextInt(cartones.size());
            CartonBingo carton = cartones.get(indiceRandom);
            cartones.remove(indiceRandom);
            String ruta="C:\\Users\\User\\OneDrive - Estudiantes ITCR\\Escritorio\\TEC\\POO\\Proyecto1\\Bingo\\Cartones\\" + carton.getCodigoUnico()+".pdf";
            imagenesBingo[i]= ruta;
        }
        CuentaCorreo cuenta = new CuentaCorreo(correoUsuario);
        cuenta.enviarCorreo(correoDestinatario, "Cartones Bingo", "Se adjuntas las imagenes de los cartones que pediste para el bingo", imagenesBingo);
    }
}

/**
 * Esta clase representa un cartón de bingo con un código único y una matriz de
 * números.
 */
class CartonBingo {

    private final String codigoUnico;
    private final int[][] numeros;
    private static int numeroCarton = 0;

    /**
     * Constructor de la clase CartonBingo.
     *
     * @param codigoUnico El código único del cartón.
     * @param numeros La matriz de números del cartón.
     */
    public CartonBingo(String codigoUnico, int[][] numeros) {
        this.codigoUnico = codigoUnico;
        this.numeros = numeros;
        CartonBingo.numeroCarton++;
    }

    /**
     * Obtiene el código único del cartón.
     *
     * @return El código único del cartón en formato String.
     */
    public String getCodigoUnico() {
        return codigoUnico;
    }

    /**
     * Obtiene la matriz de números del cartón.
     *
     * @return La matriz de números del cartón.
     */
    public int[][] getNumeros() {
        return numeros;
    }

    public boolean tieneConfiguracionX() {
        // Lógica para verificar si el cartón cumple con la configuración X
        // La configuración X se cumple si se ha marcado al menos un número en cada columna.
        for (int i = 0; i < 5; i++) {
            boolean columnaCompleta = true;
            for (int j = 0; j < 5; j++) {
                if (numeros[j][i] == 0) {
                    columnaCompleta = false;
                    break;
                }
            }
            if (columnaCompleta) {
                return true; // Se cumple la configuración X
            }
        }
        return false; // No se cumple la configuración X
    }

    public boolean tieneCuatroEsquinas() {
        // Lógica para verificar si el cartón cumple con la configuración de Cuatro Esquinas
        // Cuatro Esquinas se cumple si se han marcado los números en las esquinas.
        return (numeros[0][0] != 0 && numeros[0][4] != 0 && numeros[4][0] != 0 && numeros[4][4] != 0);
    }

    public boolean tieneCartonLleno() {
        // Lógica para verificar si el cartón está lleno
        // El cartón está lleno si todos los números están marcados.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (numeros[j][i] == 0) {
                    return false; // Al menos un número no está marcado
                }
            }
        }
        return true; // Todos los números están marcados, el cartón está lleno
    }

    public boolean tieneConfiguracionZ() {
        // Lógica para verificar si el cartón cumple con la configuración Z
        // La configuración Z se cumple si se ha marcado al menos un número en todas las filas y columnas excepto la central.
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                continue; // Saltar la fila central
            }
            boolean filaCompleta = true;
            boolean columnaCompleta = true;
            for (int j = 0; j < 5; j++) {
                if (numeros[j][i] == 0) {
                    filaCompleta = false;
                }
                if (numeros[i][j] == 0) {
                    columnaCompleta = false;
                }
            }
            if (!filaCompleta || !columnaCompleta) {
                return false; // No se cumple la configuración Z
            }
        }
        return true; // Se cumple la configuración Z
    }

    /**
     * Obtiene el número del cartón.
     *
     * @return El número del cartón.
     */
    public int getNumeroCarton() {
        return numeroCarton;
    }

    public boolean tieneNumero(int numeroLlamado) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (numeros[j][i] == numeroLlamado) {
                    return true;
                }
            }
        }
        return false;
    }
}

/**
 * Esta clase representa un jugador con nombre, cédula y correo electrónico.
 */
class Jugador {

    private final String nombre;
    private final String cedula;
    private final String correo;

    /**
     * Constructor de la clase Jugador.
     *
     * @param nombre Nombre completo del jugador.
     * @param cedula Cédula del jugador.
     * @param correo Correo electrónico del jugador.
     */
    public Jugador(String nombre, String cedula, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
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

    @Override
    public String toString() {
        return nombre + "," + cedula + "," + correo;
    }
}