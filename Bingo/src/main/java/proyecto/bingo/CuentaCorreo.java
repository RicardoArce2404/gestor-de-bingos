package proyecto.bingo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Multipart;


/**
 * Clase que representa una cuenta de correo para enviar mensajes.
 *
 */
import javax.mail.NoSuchProviderException;import javax.mail.Part;
/**
 * Clase que representa una cuenta de correo para enviar mensajes.
 *
 */
public class CuentaCorreo {

    private final String usuario;
    private final String clave = "fmdlldcnfaohdwtm"; // Clave de la cuenta de correo
    private final String servidor = "smtp.gmail.com"; // Servidor SMTP de Gmail
    private final String servidorLectura = "pop.gmail.com"; // Servidor para la lectura de correos
    private final String puerto = "587"; // Puerto para la conexión SMTP
    private final String puertoLectura = "995"; //Puerto para la lectura de correos
    private final Properties propiedades;
    private Properties propiedadesLectura;

    /**
     * Constructor de la clase CuentaCorreo que establece la configuración
     * necesaria para realizar la conexión del servidor de correo.
     *
     * @param pCorreo El correo del usuario que envía los mensajes.
     */
    public CuentaCorreo(String pCorreo) {
        propiedades = new Properties();
        propiedades.put("mail.smtp.host", servidor);
        propiedades.put("mail.smtp.port", puerto);
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        usuario = pCorreo;
    }

    /**
     * Abre una sesión de correo para poder enviar mensajes.
     *
     * @return La sesión en la que se podrán enviar correos.
     */
    private Session abrirSesion() {
        Session sesion = Session.getInstance(propiedades, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, clave);
            }
        });
        return sesion;
    }
    
    private Session abrirSesionLectura() {
      propiedadesLectura = new Properties();
      propiedadesLectura.setProperty("mail.pop3.starttls.enable", "false");
      propiedadesLectura.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory" );
      propiedadesLectura.setProperty("mail.pop3.socketFactory.fallback", "false");
      propiedadesLectura.setProperty("mail.pop3.port","995");
      propiedadesLectura.setProperty("mail.pop3.socketFactory.port", "995");
      
      Session sesion = Session.getInstance(propiedadesLectura);
      
      return sesion;
    }

    /**
     * Envía un correo con texto.
     *
     * @param pDestinatario La dirección de correo del destinatario.
     * @param pTituloCorreo El título del correo.
     * @param pCuerpo El cuerpo del correo.
     */
    public void enviarCorreo(String pDestinatario, String pTituloCorreo, String pCuerpo) {
        Session sesion = abrirSesion();

        try {
            Message message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(pDestinatario));
            message.setSubject(pTituloCorreo);
            message.setText(pCuerpo);

            Transport.send(message);
        } catch (MessagingException e) {
            // Manejar excepciones aquí si es necesario
        }
    }

    /**
     * Envía un correo con archivos adjuntos.
     *
     * @param pDestinatario La dirección de correo del destinatario.
     * @param pTituloCorreo El título del correo.
     * @param pCuerpo El cuerpo del correo.
     * @param pArchivosAdjuntos Los archivos adjuntos a enviar.
     */
    public void enviarCorreo(String pDestinatario, String pTituloCorreo, String pCuerpo,
            String[] pArchivosAdjuntos) {

        Session sesion = abrirSesion();

        try {
            Message message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(pDestinatario)
            );
            message.setSubject(pTituloCorreo);
            MimeBodyPart cuerpoPart = new MimeBodyPart();
            cuerpoPart.setText(pCuerpo);
            MimeMultipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(cuerpoPart);
            for (String file : pArchivosAdjuntos) {
                MimeBodyPart filesPart = new MimeBodyPart();
                filesPart.attachFile(file);
                multiPart.addBodyPart(filesPart);
            }

            message.setContent(multiPart);

            Transport.send(message);
        } catch (MessagingException | java.io.IOException e) {
        }
    }
    
    public ArrayList<String> leerCorreosRecibidos() throws NoSuchProviderException, MessagingException, IOException {
        propiedadesLectura = new Properties();
        propiedadesLectura.setProperty("mail.pop3.starttls.enable", "false");
        propiedadesLectura.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory" );
        propiedadesLectura.setProperty("mail.pop3.socketFactory.fallback", "false");
        propiedadesLectura.setProperty("mail.pop3.port", "995");
        propiedadesLectura.setProperty("mail.pop3.socketFactory.port", "995");
        
      
      Session sesion = Session.getInstance(propiedadesLectura);
        
        Store store = sesion.getStore("pop3");
        store.connect("pop.gmail.com", "bingolimonense@gmail.com" , clave);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        
        Message[] mensajes = folder.getMessages();
        ArrayList<String> mensajesProcesados = new ArrayList(); 
        
        for (Message message : mensajes) {
            MimeMultipart parte = (MimeMultipart) message.getContent();
            MimeBodyPart cuerpo = (MimeBodyPart) parte.getBodyPart(0) ;
            if (cuerpo.getContent().toString().length()<200){
              String[] mensajesProcesadosAux = cuerpo.getContent().toString().toLowerCase().replace(",", "").replace(".","").split(" ");
              ArrayList<String> mensajesProcesadosAuxV2= new ArrayList(Arrays.asList(mensajesProcesadosAux));
              mensajesProcesados.addAll(mensajesProcesadosAuxV2);
              mensajesProcesados.remove("\n");
            }
           
         }
         return mensajesProcesados;
        }
    
 }