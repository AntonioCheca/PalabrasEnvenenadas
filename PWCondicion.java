import java.util.Random;
import java.util.ArrayList;

/*
  CONDICIONES:
      Todos los subconjuntos de palabras deben contener al menos 2*MAX_VIDAS palabras y que al menos haya 100 palabras no envenenadas (véase BuenAzar).
    ESTÁS ENVENENADO SI...
    0- Primera letra sea 'x'.
    1- Que la primera letra no sea 'x'.
    2- Última letra sea 'x'.
    3- Que la última letra no sea 'x'.
    4- Número de letras de la palabra sea 'n'.
*/
public class PWCondicion {
  private static Random random = new Random();
  private int tipo;           // Tipo de la condición
  private int n;              // Número asociado a la condición
	private char[] azarchain;   // Cadena de caracteres asociada a la condición

  public PWCondicion(ArrayList<String> diccionario) {
		boolean tiene_sentido = false;

		while(!tiene_sentido){
			tipo = random.nextInt(5); ///////////////////////////////////////////////////////////////////////
			n = random.nextInt();
			switch(tipo) {     // Necesitamos cambiar n según cada caso, para facilitar la creación de nuevas funciones booleanas
				case 4:
					n = 2+(n%12);
					break;
			}
			azarchain = new char[5];

			for(int i=0; i < 5; i++)
				azarchain[i] = (char)(random.nextInt(26)+'a');

			tiene_sentido = BuenAzar(diccionario);
		}
  }

  public boolean isPoisoned(String word) {
		switch(tipo){
			case 0:
				return azarchain[0] == word.charAt(0);
			case 1:
				return azarchain[0] != word.charAt(0);
			case 2:
				return azarchain[0] == word.charAt(word.length() -1);
			case 3:
				return azarchain[0] != word.charAt(word.length() -1);
			case 4:
				return word.length() == n;
		}
		return false;
  }

	private int PoisonedWords(ArrayList<String> diccionario) {
		int contadorPalabras=0;
		for(int i=0; i<diccionario.size(); i++)
			if(isPoisoned(diccionario.get(i)))
				contadorPalabras++;

		return contadorPalabras;
	}

	private boolean BuenAzar(ArrayList<String> diccionario) {
		int poisoned_words = PoisonedWords(diccionario);
		return poisoned_words > 2*ProcesadorPW.MAX_VIDAS && poisoned_words < diccionario.size() - 100;
	}

  public int getPrimerJugador() {
    return random.nextInt(2);
  }

  @Override
  public String toString() {
		switch(tipo) {
			case 0:
				return "QUE EMPIECE POR " + azarchain[0];
			case 1:
				return "QUE LA PRIMERA NO SEA " + azarchain[0];
			case 2:
				return "QUE LA ÚLTIMA SEA " + azarchain[0];
			case 3:
				return "QUE LA ÚLTIMA LETRA NO SEA " + azarchain[0];
			case 4:
				return "QUE EL NÚMERO DE LETRAS SEA " + n;
		}
    return "CONDICIÓN INVÁLIDA";
  }  
}
