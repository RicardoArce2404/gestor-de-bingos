/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.bingo.newpackage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import proyecto.bingo.Bingo;
import proyecto.bingo.CuentaCorreo;
/**
 *
 * @author usuario
 */
public class AppCuenta {
    public static void main(String args[]) throws MessagingException, NoSuchProviderException, IOException, TransformerException, SAXException{
       CuentaCorreo correo = new CuentaCorreo("bingolimonense@gmail.com");
       ArrayList<String> mensajes = correo.leerCorreosRecibidos();
       for (String mensaje: mensajes){
         System.out.println(mensaje);
       }
    }
    
 
}
