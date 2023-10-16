
package proyecto.bingo;

import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author User
 */
public class CuentaCorreo {
  private String usuario;
  private String clave = "fqqoeznlkbzapybq";
  private String servidor = "smtp.gmail.com";
  private String puerto = "587";
  private Properties propiedades;
  
  /**
  * Constructor de la clase CuentaCorreo que establece lo necesario 
  * para realizar la conexión del servidor de correo
  */
  public CuentaCorreo(String pCorreo) {
    propiedades = new Properties();
    propiedades.put("mail.smtp.host", servidor);
    propiedades.put("mail.smtp.port",puerto);
    propiedades.put("mail.smtp.auth","true");
    propiedades.put("mail.smtp.starttls.enable","true");
    propiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
    usuario = pCorreo;
  }

  /**
  * Método para poder abrir una sesión
  * 
  * @return la sesion en la que se podrá enviar correos
  */
  private Session abrirSesion() {
    Session sesion = Session.getInstance(propiedades, 
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(usuario, clave);
        }
    });
    return sesion;
  }

  /**
  * Método que envia un correo con texto
  * 
  * @param el correo destinatario, el asunto del correo y el cuerpo del correo
  */
  public void enviarCorreo(String destinatario, String tituloCorreo, String cuerpo){
    
    Session sesion = abrirSesion();

    try {
      Message message = new MimeMessage(sesion);
      message.setFrom(new InternetAddress(usuario));
      message.setRecipients(
        Message.RecipientType.TO, 
        InternetAddress.parse(destinatario)
      );
      message.setSubject(tituloCorreo);
      message.setText(cuerpo);
   
      Transport.send(message);
    }
    catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  /**
  * Método que envia un correo con archivos adjuntos
  * 
  * @param El correo destinatario, el asunto del correo, el cuerpo del correo 
  * y una lista con las rutas de los archivos que se adjuntarán
  */
  public void enviarCorreo(String destinatario, String tituloCorreo, String cuerpo,
                           String[] archivosAdjuntos){
    
    Session sesion = abrirSesion();

    try {
      Message message = new MimeMessage(sesion);
      message.setFrom(new InternetAddress(usuario));
      message.setRecipients(
        Message.RecipientType.TO, 
        InternetAddress.parse(destinatario)
      );
      message.setSubject(tituloCorreo);
      MimeBodyPart cuerpoPart = new MimeBodyPart();  
      cuerpoPart.setText(cuerpo);
      MimeMultipart multiPart = new MimeMultipart();
      multiPart.addBodyPart(cuerpoPart);
      for (String file : archivosAdjuntos) {
        MimeBodyPart filesPart = new MimeBodyPart();
        filesPart.attachFile(file);
        multiPart.addBodyPart(filesPart);
      }

      message.setContent(multiPart);
   
      Transport.send(message);
    }
    catch (MessagingException | java.io.IOException e) {
      e.printStackTrace();
    }
  }
}
