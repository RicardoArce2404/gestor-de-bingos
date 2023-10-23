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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Esta clase representa un programa para generar cartones de bingo y crear
 * archivos PDF para cada uno.
 */
public final class Bingo {

  private ArrayList<CartonBingo> cartonesGenerados = new ArrayList();  // Todos los cartones generados.
  private ArrayList<CartonBingo> cartonesEnviados = new ArrayList();  // Solo los cartones enviados por correo.
  private final ArrayList<Jugador> jugadores = new ArrayList();     // Jugadores registrados.
  private final ArrayList<Integer> numerosLlamados = new ArrayList(); // Numeros llamados
  private ArrayList<Integer> numerosLlamadosJuego = new ArrayList();
  private final ArrayList<CartonBingo> cartonesGanadores = new ArrayList(); // Cartón o cartones ganadores
  private String modoJuego;
  private String premio;

  /**
   * Contructor de la clase Bingo.
   */
  public Bingo() {
    cargarJugadoresDesdeCSV(); // Cargar jugadores desde el archivo CSV
  }

  /**
   * Realiza el juego de bingo con la configuración especificada.
   *
   */
  public void cantarNumero() {

    // Generar un número aleatorio y verificar que no se haya llamado antes
    int numeroLlamado;
    do {
      numeroLlamado = generarNumeroAleatorio(1, 75);
    } while (numerosLlamadosJuego.contains(numeroLlamado));

    numerosLlamados.add(numeroLlamado); // Agregar el número llamado a la lista
    numerosLlamadosJuego.add(numeroLlamado);
    for (CartonBingo carton : cartonesEnviados) {
      // Primero se verifica si el cartón tiene el número llamado, luego se verifica si
      // ese cartón tiene la configuración correspondiente al modo de juego en uso.
      if (!carton.tieneNumero(numeroLlamado)) {
        continue;
      }
      if (modoJuego.equals("En X") && carton.tieneConfiguracionX(numerosLlamadosJuego)
          || modoJuego.equals("4 esquinas") && carton.tieneCuatroEsquinas(numerosLlamadosJuego)
          || modoJuego.equals("Cartón lleno") && carton.tieneCartonLleno(numerosLlamadosJuego)
          || modoJuego.equals("En Z") && carton.tieneConfiguracionZ(numerosLlamadosJuego)) {
        cartonesGanadores.add(carton);
      }
    }
  }

  /**
   * Envía un cartón de bingo por correo electrónico.
   *
   * @param pCorreoDestinatario Correo del jugador al cual se le quiere enviar el o los cartones.
   * @param pCantidad Cantidad de cartones a enviar.
   */
  public void enviarCorreo(String pCorreoDestinatario, int pCantidad) {
    Random aleatorio = new Random();
    String[] imagenesBingo = new String[pCantidad];

    for (int i = 0; i < pCantidad; i++) {
      int indiceRandom = aleatorio.nextInt(cartonesGenerados.size());
      CartonBingo carton = cartonesGenerados.get(indiceRandom);
      cartonesGenerados.remove(indiceRandom);

      for (Jugador jugador : jugadores) {  // Asignarle al cartón su respectivo dueño.
        if (jugador.getCorreo().equals(pCorreoDestinatario)) {
          carton.setDueño(jugador);
          break;
        }
      }

      cartonesEnviados.add(carton);
      String ruta = ".\\Cartones\\" + carton.getCodigoUnico() + ".pdf";
      imagenesBingo[i] = ruta;
    }
    CuentaCorreo cuenta = new CuentaCorreo("bingolimonense@gmail.com");
    cuenta.enviarCorreo(pCorreoDestinatario, "Cartones Bingo",
                        "Se adjuntas las imagenes de los cartones que pediste para el bingo", imagenesBingo);
  }

  /**
   * Genera un número aleatorio en el rango especificado.
   *
   * @param pMin El valor mínimo del rango.
   * @param pMax El valor máximo del rango.
   * @return Un número aleatorio dentro del rango.
   */
  public int generarNumeroAleatorio(int pMin, int pMax) {
    Random random = new Random();
    return random.nextInt(pMax - pMin + 1) + pMin;
  }

  /**
   * Genera un código único de tres letras y tres números.
   *
   * @return Un código único en formato String.
   */
  public String generarCodigoUnico() {
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
   * @param pCodigoUnico El código único del cartón.
   * @return Un objeto CartonBingo que representa el cartón generado.
   */
  public CartonBingo generarTarjetaDeBingoConCodigoUnico(String pCodigoUnico) {
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

    return new CartonBingo(pCodigoUnico, tarjeta);
  }

  /**
   * Crea un archivo PDF para el cartón de bingo.
   *
   * @param pCarton El objeto CartonBingo que representa el cartón.
   * @param pNumeroCarton El número del cartón.
   * @throws MalformedURLException Si la URL de la imagen es incorrecta.
   * @throws FileNotFoundException Si no se puede encontrar la ubicación de
   * destino para el archivo PDF.
   */
  public void crearArchivoPDF(CartonBingo pCarton, int pNumeroCarton) throws MalformedURLException, FileNotFoundException {
    String rutaCarpetaCartones = ".\\Cartones";
    File carpetaCartones = new File(rutaCarpetaCartones);

    if (!carpetaCartones.exists()) {
      carpetaCartones.mkdirs();
    }

    String nombreArchivoPDF = rutaCarpetaCartones + "\\" + pCarton.getCodigoUnico() + ".pdf";

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
        for (int[] fila : pCarton.getNumeros()) {
          for (int numero : fila) {
            table.addCell(Integer.toString(numero));
          }
        }

        String rutaImagen = ".\\Bingo.png";
        Image imagen = new Image(ImageDataFactory.create(rutaImagen));
        imagen.setFixedPosition(pdfDoc.getDefaultPageSize().getWidth() - 100, pdfDoc.getDefaultPageSize().getHeight() - 100);
        doc.add(imagen);
        doc.add(new Paragraph("\n"));
        doc.add(new Paragraph("\n"));
        doc.add(new Paragraph("\n"));
        doc.add(table);
        doc.add(new Paragraph("- Identificador: " + pCarton.getCodigoUnico()).setTextAlignment(TextAlignment.RIGHT));
      }
      
    } catch (IOException e) {
      System.err.println("\n Error al crear el archivo PDF: " + e.getMessage());
    }
  }

  /**
   * Busca un cartón por su código único.
   *
   * @param pCodigo El código único del cartón a buscar.
   * @return El objeto CartonBingo si se encuentra, o null si no se encuentra.
   */
  public CartonBingo buscarCartonPorCodigo(String pCodigo) {
    for (CartonBingo carton : cartonesEnviados) {
      if (carton.getCodigoUnico().equalsIgnoreCase(pCodigo)) {
        return carton;
      }
    }
    return null;
  }

  /**
   * Busca un cartón por su código único y lo muestra en la consola.
   *
   * @param pScanner El objeto Scanner para la entrada del usuario.
   */
  public void buscarYMostrarCarton(Scanner pScanner) {
    System.out.print("Ingrese el identificador del cartón que desea buscar: ");
    String codigo = pScanner.nextLine();

    CartonBingo carton = buscarCartonPorCodigo(codigo);

    // logica para mostrar el carton encontrado en la GUI
  }

  /**
   * Registra un nuevo jugador con nombre, cédula y correo.
   *
   * @param pNombre Nombre del jugador.
   * @param pCedula Cédula del jugador.
   * @param pCorreo Correo electrónico del jugador.
   */
  public void registrarJugador(String pNombre, String pCedula, String pCorreo) {
    // Las validaciones para los parámetros usados en este método se realizan desde la
    // interfaz, al llegar aquí se puede asegurar que los parámetros ya fueron validados.
    Jugador nuevoJugador = new Jugador(pNombre, pCedula, pCorreo);
    jugadores.add(nuevoJugador);
  }
  
  /**
   * Genera y muestra los cartones de bingo.
   *
   * @param pCantidadCartones Cantidad de cartones a generar.
   * @throws java.net.MalformedURLException
   * @throws java.io.FileNotFoundException
   */
  public void generarCartones(int pCantidadCartones) throws MalformedURLException, FileNotFoundException {
    borrarCartonesAnteriores(); // Elimina los cartones anteriores antes de generar los nuevos.
    // Las validaciones para los parámetros usados en este método se realizan desde la
    // interfaz, al llegar aquí se puede asegurar que los parámetros ya fueron validados.
    // Generar y mostrar la cantidad de cartones especificada
    for (int i = 1; i <= pCantidadCartones; i++) {
      String codigoUnico = generarCodigoUnico();
      CartonBingo carton = generarTarjetaDeBingoConCodigoUnico(codigoUnico);
      cartonesGenerados.add(carton);
      crearArchivoPDF(carton, i);
    }
  }

  /**
   * Este método borra los archivos de cartones anteriores en una carpeta
   * especificada.
   */
  public void borrarCartonesAnteriores() {
    // Ruta de la carpeta que contiene los cartones anteriores
    String rutaCarpetaCartones = ".\\Cartones";

    // Crear un objeto File para representar la carpeta de cartones
    File carpetaCartones = new File(rutaCarpetaCartones);

    // Obtener una lista de archivos en la carpeta
    File[] archivos = carpetaCartones.listFiles();

    if (archivos != null) {  // Verificar si la lista de archivos no es nula
      for (File archivo : archivos) {  // Iterar a través de los archivos en la carpeta
        if (archivo.isFile()) {  // Verificar si el archivo es un archivo (no una carpeta)
          archivo.delete();  // Borrar el archivo
        }
      }
    }
  }

  /**
   * Carga los jugadores desde un archivo CSV.
   */
  public void cargarJugadoresDesdeCSV() {
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
  public void guardarJugadoresEnCSV() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("jugadores.csv"))) {
      for (Jugador jugador : jugadores) {
        writer.write(jugador.toString());
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("Error al guardar los jugadores en el archivo CSV: " + e.getMessage());
    }
  }
  
  public void agregarArchivoXml(String nombre,String tipoJuego, String numerosCantados, String ganador, LocalDateTime fechaHora) throws TransformerException, SAXException{
      File file = new File(nombre);
      try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document documento = dBuilder.parse(file);
        Element raiz = documento.getDocumentElement();
        Element partida = documento.createElement("partida");
        raiz.appendChild(partida);
        
        Element tipo = documento.createElement("tipo");
        Text contPartida = documento.createTextNode(tipoJuego);
        partida.appendChild(tipo);
        tipo.appendChild(contPartida);
        
        Element numsCantados = documento.createElement("numerosCantados");
        Text contNumsCantados = documento.createTextNode(numerosCantados);
        partida.appendChild(numsCantados);
        numsCantados.appendChild(contNumsCantados);
        
        Element ganadores = documento.createElement("ganadores");
        Text contGanadores = documento.createTextNode(ganador);
        partida.appendChild(ganadores);
        ganadores.appendChild(contGanadores);
        
        Element hora = documento.createElement("hora");
        Text contHora = documento.createTextNode(DateTimeFormatter.ofPattern("HH:mm").format(fechaHora));
        partida.appendChild(hora);
        hora.appendChild(contHora);
        
        Element fecha = documento.createElement("fecha");
        Text contFecha = documento.createTextNode(DateTimeFormatter.ofPattern("ddMMyyy").format(fechaHora));
        partida.appendChild(fecha);
        fecha.appendChild(contFecha);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        DOMSource source = new DOMSource(documento);
	 

        
        transformer.transform(source, sr);
        PrintWriter writer = new PrintWriter(new FileWriter(nombre));
        writer.println(sw.toString());
        writer.close();
      } catch(Exception e) {
          e.printStackTrace();
    }
  }
  public void setBlankNumerosLlamados(){
    numerosLlamadosJuego.clear();
  }
  
  public void setBlankCartonesGanadores(){
    cartonesGanadores.clear();
  }

  /**
   * Obtiene la lista de cartones a usar en la partida.
   *
   * @return La lista de cartones a usar.
   */
  public ArrayList<CartonBingo> getCartones() {
    return cartonesEnviados;
  }
  
  /**
   * Obtiene la lista de cartones que aún no han sido enviados por correo.
   *
   * @return La lista de cartones generados.
   */
  public ArrayList<CartonBingo> getCartonesGenerados() {
    return cartonesGenerados;
  }

  /**
   * Obtiene la lista de jugadores registrados.
   *
   * @return La lista de jugadores.
   */
  public ArrayList<Jugador> getJugadores() {
    return jugadores;
  }

  /**
   * Obtiene la lista de números llamados durante el juego.
   *
   * @return La lista de números llamados.
   */
  public ArrayList<Integer> getNumerosLlamados() {
    return numerosLlamadosJuego;
  }

  /**
   * Obtiene la lista de cartones ganadores.
   *
   * @return Una lista de objetos CartonBingo.
   */
  public ArrayList<CartonBingo> getCartonesGanadores() {
    return cartonesGanadores;
  }
  
  public String getModoJuego() {
    return modoJuego;
  }
  
  public void setModoJuego(String pModoJuego) {
    modoJuego = pModoJuego;
  }
  
  public String getPremio() {
    return premio;
  }
  
  public void setPremio(String pPremio) {
    premio = pPremio;
  }

  /**
   * Vacía ambas listas con cartones.
   */
  public void resetearListasCartones() {
    cartonesGenerados.clear();
    cartonesEnviados.clear();
  }
}
