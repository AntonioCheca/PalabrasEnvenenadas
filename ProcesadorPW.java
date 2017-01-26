import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;


public class ProcesadorPW extends Thread {
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private Socket[] socketConexiones;
	// stream de lectura (por aquí se recibe lo que envía el cliente)
	private BufferedReader[] inReaders;
	// stream de escritura (por aquí se envía los datos al cliente)
	private PrintWriter[] outPrinters;

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final int MAX_VIDAS = 10;
	public static final int VELOCIDAD = 2;
	public int[] vidas;
	public String[] names;

	private int RNG;
	private int n;
	private char[] azarchain;

	private ArrayList<String> diccionario;

	// Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
	private Random random;

	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public ProcesadorPW(Socket socketServicio_J1, Socket socketServicio_J2, ArrayList<String> copia_diccionario) {
		socketConexiones = new Socket[2];
		inReaders = new BufferedReader[2];
		outPrinters = new PrintWriter[2];
		vidas = new int[2];
		this.socketConexiones[0] = socketServicio_J1;
		this.socketConexiones[1] = socketServicio_J2;
		random = new Random();
		vidas[0] = MAX_VIDAS;
		vidas[1] = MAX_VIDAS;
		names = new String[2];
		diccionario = copia_diccionario;
		boolean tiene_sentido = false;

		while(!tiene_sentido){
			RNG = random.nextInt(5); ///////////////////////////////////////////////////////////////////////

			// Definimos n.
			n = random.nextInt();
			//
			switch(RNG){	//Necesitamos cambiar n según cada caso, para facilitar la creación de nuevas funciones booleanas
				case 4:
					n = 2+(n%12);
					break;
			}
			azarchain = new char[5];


			for(int i=0; i < 5; i++)
				azarchain[i] = (char)(random.nextInt(26)+'a');

			tiene_sentido = BuenAzar();
		}

	}

	private void EscribirCondicion(){
		switch(RNG){
			case 0:
				System.out.println("QUE EMPIECE POR " + azarchain[0]);
				break;
			case 1:
				System.out.println("QUE LA PRIMERA NO SEA " + azarchain[0]);
				break;
			case 2:
				System.out.println("QUE LA ÚLTIMA SEA " + azarchain[0]);
				break;
			case 3:
				System.out.println("QUE LA ÚLTIMA LETRA NO SEA " + azarchain[0]);
				break;
			case 4:
				System.out.println("QUE EL NÚMERO DE LETRAS SEA " + n);
				break;
		}
	}

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
	private boolean isPoisoned(String word){
		boolean is_poisoned = false;
		switch(RNG){
			case 0:
				is_poisoned = (azarchain[0] == word.charAt(0));
				break;
			case 1:
				is_poisoned = (azarchain[0] != word.charAt(0));
				break;
			case 2:
				is_poisoned = ( azarchain[0] == word.charAt(word.length() -1) );
				break;
			case 3:
				is_poisoned = ( azarchain[0] != word.charAt(word.length() -1) );
				break;
			case 4:
				is_poisoned = (word.length() == n);
				break;
			default: break;
		}
		return is_poisoned;
	}

	private int PoisonedWords(){
		int contadorPalabras=0;
		for(int i=0; i<diccionario.size(); i++)
			if( isPoisoned(diccionario.get(i)) )
				contadorPalabras++;

		return contadorPalabras;
	}
	private boolean BuenAzar(){
		int poisoned_words = PoisonedWords();
		return( poisoned_words > 2*MAX_VIDAS && poisoned_words < diccionario.size() - 100);
	}

	private ArrayList<String> PalabrasQueEmpiecenPor(char x){
		ArrayList<String> palabras;
		palabras = new ArrayList<>();
		for(int i=0; i<diccionario.size();i++)
			if( diccionario.get(i).charAt(0) == x )
				palabras.add(diccionario.get(i));
		return palabras;
	}

	private void MostrarVidas(){
		final int BARRA_TOTAL = 20;
		int barra_J1 = BARRA_TOTAL * vidas[0] / MAX_VIDAS;
		int barra_J2 = BARRA_TOTAL * vidas[1] / MAX_VIDAS;

		System.out.printf(names[0] + ": [");
		for(int i=0; i<barra_J1; i++)
			System.out.printf("=");
		for(int i=0;i<(BARRA_TOTAL-barra_J1); i++)
			System.out.printf(" ");
		System.out.printf("]");

		System.out.printf("-VS-");

		System.out.printf("[");
		for(int i=0;i<(BARRA_TOTAL-barra_J2); i++)
			System.out.printf(" ");
		for(int i=0; i<barra_J2; i++)
			System.out.printf("=");
		System.out.printf("]: " + names[1] + "\n");
	}

	// Aquí es donde se realiza el procesamiento realmente:
	public void run(){
		//System.out.println("RNG = " + RNG + ", n = " + n + ", azarchain = " + azarchain[0]);

		// Como máximo leeremos un bloque de 1024 bytes. Esto se puede modificar.
		String wordRecibido;

		// Array de bytes para enviar la respuesta. Podemos reservar memoria cuando vayamos a enviarka:
		String datosEnviar;

		boolean game_over = false;

		EscribirCondicion();

		int winner=-1;
		int turno=-1;
		try {
			Thread.sleep(4000);
			// Obtiene los flujos de escritura/lectura
			inReaders[0] = new BufferedReader(new InputStreamReader(socketConexiones[0].getInputStream()));
			outPrinters[0] = new PrintWriter(socketConexiones[0].getOutputStream(),true);
			inReaders[1] = new BufferedReader(new InputStreamReader(socketConexiones[1].getInputStream()));
			outPrinters[1] = new PrintWriter(socketConexiones[1].getOutputStream(),true);
			// Pedimos los nombres
			boolean nombre = false;

			while(!nombre){
				names[0] = inReaders[0].readLine();
				String[] aux = names[0].split(" ");
				if( aux.length == 2 && aux[0].equals("NAME") ){
					names[0] = aux[1];
					nombre=true;
				}
				else
					outPrinters[0].println("ERR");

			}
			nombre = false;
			while(!nombre){
				names[1] = inReaders[1].readLine();
				String[] aux = names[1].split(" ");
				if( aux.length == 2 && aux[0].equals("NAME") ){
					names[1] = aux[1];
					nombre=true;
				}
				else
					outPrinters[1].println("ERR");

			}

			if( names[0].equals(names[1]) ){
				names[0] = names[0] + "_J1";				names[1] = names[1] + "_J2";
			}

			outPrinters[0].println("OK");
			outPrinters[1].println("OK");
			System.out.println("NOMBRES: " + names[0] + " " + names[1]);

			// Quién empieza
			int first_player = random.nextInt(2);
			winner = -1;

			// Avisar quién empieza
			String empiezas = "START 1";
			String no_empiezas = "START 0";

			if(first_player == 0){
				outPrinters[0].println(empiezas);
				outPrinters[1].println(no_empiezas);
			}
			else{
				outPrinters[1].println(empiezas);
				outPrinters[0].println(no_empiezas);
			}

			// Empieza el juego, turno = 0 si le toca a J1, turno = 1 si le toca a J2.
			turno = first_player;
			String wordAnterior = "";
			while(!game_over){
				MostrarVidas();
				// Recibimos "WORD *" del jugador, y lo cambiamos a quedarnos con "*". Y que "WORD" sea "WORD".
				wordRecibido = inReaders[turno].readLine();
				if(wordRecibido == null){
					System.out.println("\n" + names[turno] + " ha cerrado la conexión.");
					game_over=true;
					winner=1-turno;
				}
				else{
					System.out.printf(names[turno] + " dice: ");
					if(turno == 1)
						System.out.printf(new String(new char[40]).replace("\0", " ")); // 40 espacios
					String[] palabrasRecibidas = wordRecibido.split(" ");
					if(palabrasRecibidas[0].equals("WORD") && palabrasRecibidas.length == 2){
						wordRecibido = palabrasRecibidas[1];
						if( !wordAnterior.equals("") && (wordAnterior.charAt(wordAnterior.length()-1) != wordRecibido.charAt(0)) ){
							winner = 1-turno;
							game_over = true;
							System.out.println("");
						}
						else{
							wordAnterior = new String(wordRecibido);
							// Buscamos la palabra en el diccionario "del Servidor", y la quitamos.
							ArrayList<String> palabrasPrimeraLetra = PalabrasQueEmpiecenPor(wordRecibido.charAt(0));
							int indexWord = palabrasPrimeraLetra.indexOf(wordRecibido);
							if(indexWord < 0){
								System.out.println(wordRecibido);
								outPrinters[turno].println("NOT FOUND");
								winner = 1-turno;
								game_over = true;
							}
							else{
								// Aplicamos el envenenamiento...
								if( isPoisoned(wordRecibido) ){
									System.out.println(ANSI_RED + wordRecibido + ANSI_RESET);
									outPrinters[turno].println("POISON 1");
									vidas[turno] = vidas[turno] - 1;
									if(vidas[turno] == 0){
										winner = 1-turno;
										game_over = true;
									}
								}
								else{
									System.out.println(ANSI_YELLOW + wordRecibido + ANSI_RESET);
									outPrinters[turno].println("POISON 0");
								}


								// Enviamos al siguiente jugador la palabra dicha.
								if(!game_over)
									outPrinters[1-turno].println("WORD " + wordRecibido);
								turno = 1-turno;
								diccionario.remove(wordRecibido);
							}
							sleep(1000/VELOCIDAD);
						}
					}
					else{ //ERRWORD
						game_over=true;
						winner=1-turno;
						outPrinters[turno].println("ERRWORD");
					}
				}
			}
			MostrarVidas();
			outPrinters[0].println("GAME OVER");
			outPrinters[1].println("GAME OVER");

			if( vidas[winner] == MAX_VIDAS )
				outPrinters[winner].println("YOU WIN, PERFECT");
			else
				outPrinters[winner].println("YOU WIN");

			outPrinters[1-winner].println("YOU LOSE");
			System.out.println(names[winner] + " WINS!");
			Thread.sleep(8000);
			socketConexiones[0].close();
			socketConexiones[1].close();


		} catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		} catch (InterruptedException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		}

	}
}
