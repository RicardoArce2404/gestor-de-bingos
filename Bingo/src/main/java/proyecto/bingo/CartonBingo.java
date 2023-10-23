package proyecto.bingo;

import java.util.List;

/**
 * Representación de un cartón de bingo.
 */
class CartonBingo {

  private final String codigoUnico;
  private final int[][] numeros;
  private Jugador dueño;
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
   * Obtiene el objeto Dueño asociado al cartón.
   * 
   * @return Objeto Dueño asociado al cartón.
   */
  
  public Jugador getDueño() {
    return dueño;
  }
  
  public void setDueño(Jugador pDueño) {
    dueño = pDueño;
  }
  
  /**
   * Verifica si el cartón tiene configuración de X.
   * @param numerosLlamados Lista de números llamados hasta ahora.
   * @return True si se cumple la configuración, sino false.
   */
  public boolean tieneConfiguracionX(List<Integer> numerosLlamados) {
    // Verificar diagonal creciente
    int j = 0;
    for (int i = 4; i >= 0; i--) {
      if (!numerosLlamados.contains(numeros[i][j])) {
        return false;
      }
      j++;
    }
    // Verificar diagonal decreciente
    for (int i = 0; i < 5; i++) {
      if (!numerosLlamados.contains(numeros[i][i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Verifica si el cartón tiene configuración de 4 esquinas.
   * @param numerosLlamados Lista de números llamados hasta ahora.
   * @return True si se cumple la configuración, sino false.
   */
  public boolean tieneCuatroEsquinas(List<Integer> numerosLlamados) {
    // Verifica si los números en las cuatro esquinas han sido llamados    
    if (numerosLlamados.contains(numeros[0][0]) && numerosLlamados.contains(numeros[0][4])
        && numerosLlamados.contains(numeros[4][0]) && numerosLlamados.contains(numeros[4][4])) {
      return true;
    }
    return false;
  }

  /**
   * Verifica si el cartón tiene configuración de cartón lleno.
   * @param numerosLlamados Lista de números llamados hasta ahora.
   * @return True si se cumple la configuración, sino false.
   */
  public boolean tieneCartonLleno(List<Integer> numerosLlamados) {
    // El cartón está lleno si todos sus números han sido llamados
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (!numerosLlamados.contains(numeros[j][i])) {
          return false; // Al menos un número no ha sido llamado
        }
      }
    }
    return true; // Todos los números han sido llamados, el cartón está lleno
  }

  /**
   * Verifica si el cartón tiene configuración de Z.
   * @param numerosLlamados Lista de números llamados hasta ahora.
   * @return True si se cumple la configuración, sino false.
   */
  public boolean tieneConfiguracionZ(List<Integer> numerosLlamados) {
    for (int i = 0; i < 5; i++) {  // Se verifica la fila superior
      if (!numerosLlamados.contains(numeros[0][i])) {
        return false;
      }
    }
    for (int i = 0; i < 5; i++) {  // Se verifica la fila inferior
      if (!numerosLlamados.contains(numeros[4][i])) {
        return false;
      } 
    }
    // Se verifican los elementos de la diagonal creciente
    return numerosLlamados.contains(numeros[1][3])
           && numerosLlamados.contains(numeros[2][2])
           && numerosLlamados.contains(numeros[3][1]);
  }

  /**
   * Verifica si el cartón tiene cierto número.
   * @param numeroLlamado Número a buscar en el cartón.
   * @return true si se cumple la verificación, sino false.
   */
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
