package proyecto.bingo;

import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/**
 * Este programa simula la creación de una tarjeta de bingo con números
 * aleatorios y genera archivos PDF para cada cartón.
 */
public class Bingo {

    private static Iterable<int[]> tarjeta;

    /**
     * Método principal que inicia el programa de generación de cartones de
     * bingo y crea archivos PDF.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en
     * este caso).
     */
    public static void main(String[] args) throws MalformedURLException {
        try (Scanner scanner = new Scanner(System.in)) {
            // Solicitar al usuario la cantidad de cartones que desea crear
            System.out.print("Ingrese la cantidad de cartones de bingo a crear (1-500): ");
            int cantidadCartones = scanner.nextInt();

            // Validar que la cantidad esté dentro del rango 1-500
            if (cantidadCartones < 1 || cantidadCartones > 500) {
                System.out.println("Error: Cantidad no válida. Debe estar en el rango de 1 a 500.");
            } else {
                // Generar y mostrar la cantidad de cartones especificada
                for (int i = 1; i <= cantidadCartones; i++) {
                    System.out.println("Cartón " + i + ":");
                    String codigoUnico = generarCodigoUnico();
                    tarjeta = generarTarjetaDeBingoConCodigoUnico();
                    crearArchivoPDF(codigoUnico, i);
                    System.out.println(); // Separador entre cartones
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

        // Generar tres letras aleatorias
        for (int i = 0; i < 3; i++) {
            char letra = (char) (rand.nextInt(26) + 'A');
            codigo.append(letra);
        }

        // Generar tres números aleatorios
        for (int i = 0; i < 3; i++) {
            int numero = rand.nextInt(10);
            codigo.append(numero);
        }

        return codigo.toString();
    }

    /**
     * Genera un cartón de bingo con números aleatorios y un código único.
     *
     * @return
     */
    public static Iterable<int[]> generarTarjetaDeBingoConCodigoUnico() {
        int[][] tarjeta = new int[5][5];
        ArrayList<Integer> numerosUtilizados = new ArrayList<>();
        boolean valido = false;
        int tmp = 0;

        // Llenar el cartón con números aleatorios sin repetición
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

        // Generar un código único para el cartón
        String codigoUnico = generarCodigoUnico();

        // Títulos de las columnas: B, I, N, G, O
        String[] titulosColumnas = {"B", "I", "N", "G", "O"};

        for (String titulo : titulosColumnas) {
            System.out.print(titulo + "\t");
        }
        System.out.println();

        for (int[] fila : tarjeta) {
            for (int numero : fila) {
                System.out.print(numero + "\t");
            }
            System.out.println();
        }

        return () -> new Iterator<int[]>() {
            private int rowIndex = 0;

            @Override
            public boolean hasNext() {
                return rowIndex < tarjeta.length;
            }

            @Override
            public int[] next() {
                return tarjeta[rowIndex++];
            }
        };
    }

    /**
     * Crea un archivo PDF para el cartón de bingo.
     *
     * @param codigoUnico El código único del cartón.
     * @param numeroCarton El número del cartón.
     */
    public static void crearArchivoPDF(String codigoUnico, int numeroCarton) throws MalformedURLException {
        String nombreArchivoPDF = String.format(codigoUnico) + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(nombreArchivoPDF));
            PdfDocument pdfDoc = new PdfDocument(writer);
            // Agregar el identificador al PDF
            try (Document doc = new Document(pdfDoc)) {

            // Crear una tabla para los números del cartón con 5 columnas
            Table table = new Table(UnitValue.createPercentArray(new float[]{20, 20, 20, 20, 20}));
            table.setWidth(UnitValue.createPercentValue(100));

                // Agregar los números del cartón a la tabla
                for (int[] fila : tarjeta) {
                    for (int numero : fila) {
                        table.addCell(Integer.toString(numero));
                    }
                }
                // Agregar la imagen como logotipo en la esquina derecha
                String rutaImagen = "C:\\Users\\usuario\\Documents\\NetBeansProjects\\Bingo\\Bingo.png"; // Reemplaza con la ruta de tu imagen
                Image imagen = new Image(ImageDataFactory.create(rutaImagen));
                imagen.setFixedPosition(pdfDoc.getDefaultPageSize().getWidth() - 100, pdfDoc.getDefaultPageSize().getHeight() - 100);
                doc.add(imagen);
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("-              B                                I                                    N                              G                                 O            "));
                doc.add(table);
                // Agregar el identificador al PDF
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("                                                             Identificador:     " + codigoUnico).setTextAlignment(TextAlignment.LEFT));
            }

            System.out.println("Archivo PDF generado: " + nombreArchivoPDF);
        } catch (FileNotFoundException | IOException e) {
            System.err.println("Error al crear el archivo PDF: " + e.getMessage());
        }
    }
}
