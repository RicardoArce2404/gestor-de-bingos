/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyecto.bingo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 * Ventana de cantar número.
 */
public class CantarNumero extends javax.swing.JFrame {

  private Bingo bingo;

  /**
   * Constructor de la clase CantarNumero.
   *
   * @param pBingo Objeto Bingo con los datos de la sesión actual.
   */
  public CantarNumero(Bingo pBingo) {
    initComponents();
    bingo = pBingo;
    textoTipoJuego.setText("Modo de juego: " + bingo.getModoJuego());
    textoPremio.setText("Premio: " + bingo.getPremio());
    
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel4 = new javax.swing.JPanel();
    textoNumerosCantados = new javax.swing.JLabel();
    textoTipoJuego = new javax.swing.JLabel();
    botonCantanNumero = new javax.swing.JButton();
    botonRegresar = new javax.swing.JButton();
    textoPremio = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    cuadroNumerosCantados = new javax.swing.JTextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jPanel4.setBackground(new java.awt.Color(204, 255, 255));

    textoNumerosCantados.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
    textoNumerosCantados.setText("Números cantados:");

    textoTipoJuego.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
    textoTipoJuego.setForeground(new java.awt.Color(0, 102, 102));
    textoTipoJuego.setText("Tipo de juego: - - - - - - - - - -");

    botonCantanNumero.setBackground(new java.awt.Color(204, 204, 255));
    botonCantanNumero.setFont(new java.awt.Font("Bookman Old Style", 0, 18)); // NOI18N
    botonCantanNumero.setText("Cantar número");
    botonCantanNumero.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        botonCantanNumeroActionPerformed(evt);
      }
    });

    botonRegresar.setBackground(new java.awt.Color(204, 204, 255));
    botonRegresar.setFont(new java.awt.Font("Bookman Old Style", 0, 18)); // NOI18N
    botonRegresar.setText("Regresar");
    botonRegresar.setToolTipText("");
    botonRegresar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        botonRegresarActionPerformed(evt);
      }
    });

    textoPremio.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
    textoPremio.setText("Premio: - - - - - - - - - -");

    cuadroNumerosCantados.setColumns(20);
    cuadroNumerosCantados.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    cuadroNumerosCantados.setLineWrap(true);
    cuadroNumerosCantados.setRows(5);
    jScrollPane1.setViewportView(cuadroNumerosCantados);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGap(302, 302, 302)
        .addComponent(botonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, Short.MAX_VALUE))
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(textoTipoJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(42, 42, 42)
            .addComponent(textoPremio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(textoNumerosCantados, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)))
              .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(botonCantanNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 36, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(textoTipoJuego)
          .addComponent(textoPremio))
        .addGap(32, 32, 32)
        .addComponent(botonCantanNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(textoNumerosCantados)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
        .addGap(18, 18, 18)
        .addComponent(botonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(15, 15, 15))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void botonCantanNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCantanNumeroActionPerformed
    bingo.cantarNumero();

    String numerosLlamados = "";
    // Se crea el texto que va a tener los números que han sido llamados.
    for (Integer numero : bingo.getNumerosLlamados()) {
      numerosLlamados = numerosLlamados.concat(numero.toString() + ", ");
    }
    cuadroNumerosCantados.setText(numerosLlamados);
    
    ArrayList<CartonBingo> cartonesGanadores = bingo.getCartonesGanadores();
    // Esto se hace para evitar enviar 2 correos de felicitaciones.
    ArrayList<String> direccionesEmailUsadas = new ArrayList<>();
    if (!cartonesGanadores.isEmpty()) {
      CuentaCorreo cuenta = new CuentaCorreo("bingolimonense@gmail.com");
      for (CartonBingo carton : cartonesGanadores) {
        // Se revisa si ya se ha enviado un correo a esa dirección.
        if (direccionesEmailUsadas.contains(carton.getDueño().getCorreo())) {
          continue;
        }
        direccionesEmailUsadas.add(carton.getDueño().getCorreo());
        String cuerpoEmail = "Hola, como dueño/a del cartón con código " + carton.getCodigoUnico()
                           + ", te informamos que ganaste el siguiente premio: "
                           + bingo.getPremio() + ", felicitaciones!";
        cuenta.enviarCorreo(carton.getDueño().getCorreo(), "¡Bingo! ¡Has ganado :D!",
                            cuerpoEmail);
      }
      String cartonesXML = "";
      if (cartonesGanadores.size() == 1) {
        
        String mensajeGanador = "¡Bingo!\n";
        mensajeGanador += "Cartón ganador: " + cartonesGanadores.get(0).getCodigoUnico();
        cartonesXML =cartonesGanadores.get(0).getDueño().getCedula();
        JOptionPane.showMessageDialog(this, mensajeGanador);
        
        
      } else {
        String mensajeGanador = "¡Bingo!\nCartones ganadores:\n";
        for (CartonBingo carton : cartonesGanadores) {
          mensajeGanador += "• " + carton.getCodigoUnico() + "\n";
          cartonesXML = cartonesXML.concat(carton.getDueño().getCedula()+", ");
        }
        JOptionPane.showMessageDialog(this, mensajeGanador);
       }
      String tipo = "";
      if(bingo.getModoJuego().contains("X")){
        tipo = "X";
      }
      if(bingo.getModoJuego().contains("4")){
        tipo = "E";
      }
      if(bingo.getModoJuego().contains("C")){
        tipo = "L";
      } if(bingo.getModoJuego().contains("Z")){
        tipo  = "Z";
      }
      try {
        bingo.agregarArchivoXml("RegistroBingo.xml", tipo, numerosLlamados.substring(0, numerosLlamados.length() -2), cartonesXML, LocalDateTime.now());
      } catch (SAXException ex) {
        Logger.getLogger(CantarNumero.class.getName()).log(Level.SEVERE, null, ex);
      } catch (TransformerException ex) {
        Logger.getLogger(CantarNumero.class.getName()).log(Level.SEVERE, null, ex);
      }
      bingo.setBlankNumerosLlamados();
      bingo.setBlankCartonesGanadores();
      this.dispose();
    }
  }//GEN-LAST:event_botonCantanNumeroActionPerformed

  private void botonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegresarActionPerformed
    this.dispose();
  }//GEN-LAST:event_botonRegresarActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(CantarNumero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(CantarNumero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(CantarNumero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(CantarNumero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton botonCantanNumero;
  private javax.swing.JButton botonRegresar;
  private javax.swing.JTextArea cuadroNumerosCantados;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel textoNumerosCantados;
  private javax.swing.JLabel textoPremio;
  private javax.swing.JLabel textoTipoJuego;
  // End of variables declaration//GEN-END:variables
}
