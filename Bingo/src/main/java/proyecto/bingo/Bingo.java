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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Esta clase representa un programa para generar cartones de bingo y crear
 * archivos PDF para cada uno.
 */
public class Bingo {

    // Lista para almacenar los cartones generados.
    private static final List<CartonBingo> cartones = new ArrayList<>();

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
            // Solicitar al usuario la cantidad de cartones que desea crear
            System.out.print("Recuerda que el rango de creación es de 1 a 500 cartones\n");
            System.out.print("Ingrese la cantidad de cartones de bingo a crear: ");
            int cantidadCartones = scanner.nextInt();

            // Validar que la cantidad esté dentro del rango 1-500
            if (cantidadCartones < 1 || cantidadCartones > 500) {
                System.out.println("Error: Cantidad es invalida (Debe estar en el rango de 1 a 500.)\n");
            } else {
                // Generar y mostrar la cantidad de cartones especificada
                for (int i = 1; i <= cantidadCartones; i++) {
                    System.out.println("Cartón " + i + ":");
                    String codigoUnico = generarCodigoUnico();
                    CartonBingo carton = generarTarjetaDeBingoConCodigoUnico(codigoUnico);
                    mostrarCartonEnConsola(carton); // Mostrar el cartón recién creado
                    cartones.add(carton);
                    crearArchivoPDF(carton, i);
                    System.out.println(); // Separador entre cartones
                }

                // Mostrar la lista de cartones y permitir al usuario seleccionar uno
                while (true) {
                    mostrarListaDeCartones();
                    System.out.print("Ingrese el código del cartón que desea utilizar, si desea salir ingrese (s): \n");
                    String codigoSeleccionado = scanner.next();
                    if (codigoSeleccionado.equalsIgnoreCase("s")) {
                        System.out.print("--------------------------------------------\n");
                        System.out.print("      Muchas gracias por preferirnos        \n");
                        System.out.print("--------------------------------------------\n");
                        break;
                    }
                    CartonBingo cartonSeleccionado = buscarCartonPorCodigo(codigoSeleccionado);
                    if (cartonSeleccionado != null) {
                        mostrarCartonEnConsola(cartonSeleccionado);
                    } else {
                        System.out.println("Código de cartón no válido.");
                    }
                }
            }
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
        String rutaCarpetaCartones = "C:\\Users\\usuario\\Documents\\NetBeansProjects\\Bingo\\Cartones";
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

                String rutaImagen = "C:\\Users\\usuario\\Documents\\NetBeansProjects\\Bingo\\Bingo.png";
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

    /**
     * Obtiene el número del cartón.
     *
     * @return El número del cartón.
     */
    public int getNumeroCarton() {
        return numeroCarton;
    }
}
