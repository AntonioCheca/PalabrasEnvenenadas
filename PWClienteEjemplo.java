//
// PoisonedWords
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
//import com.google.common.collect.Multimap;
//import org.apache.commons.collections4.MultiMap;

public class PWClienteEjemplo {
	private static String name = "Charmander";
	private static ArrayList<String> diccionario;

	private static Random random;

	private static ArrayList<String> PalabrasQueEmpiecenPor(char x){
		ArrayList<String> palabras;
		palabras = new ArrayList<>();
		for(int i=0; i<diccionario.size();i++)
			if( diccionario.get(i).charAt(0) == x )
				palabras.add(diccionario.get(i));
		return palabras;
	}

	public static void main(String[] args) {

		if(args.length != 0){
			System.out.println("Uso del programa: ./PWClienteEjemplo");
			return;
		}

		String buferEnvio;
		String buferRecepcion;
		diccionario = new ArrayList<>();
		File file = new File("./listado-general-new.txt");
		FileInputStream fis = null;
		BufferedReader reader = null;
		random = new Random();

		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String word = reader.readLine();
			while(word != null){
				diccionario.add(word);
				word = reader.readLine();
			}

			fis.close();

			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter outPrinter = new PrintWriter(System.out,true);

			outPrinter.println(name);	//Decimos nuestro nombre
			buferRecepcion = inReader.readLine();

			if(buferRecepcion.equals("1")){
				outPrinter.println("alabar");
				diccionario.remove("alabar");	//La eliminamos para no repetirla
				buferRecepcion = inReader.readLine();	//Leemos si está envenenada o no
				//Este cliente es bastante sencillo y no tiene en cuenta si las palabras que dice están o no envenenadas
			}

			boolean game_on = true;
			while(game_on){
				buferRecepcion = inReader.readLine();	//Leemos la palabra, o si se ha acabado el juego un GAME OVER
				if(buferRecepcion.equals("GAME OVER"))
					game_on = false;
				else{
					// Lees la palabra que ha dicho el contrincante
					char ultimaLetra = buferRecepcion.charAt(buferRecepcion.length() -1);
					diccionario.remove(buferRecepcion);

					ArrayList<String> palabras = PalabrasQueEmpiecenPor(ultimaLetra);
					//Coges las posibles palabras que puedes decir, y coge una al azar
					if(palabras.size() > 0){
						int rand_int = random.nextInt(palabras.size());
						buferEnvio = palabras.get(rand_int);
						diccionario.remove(palabras.get(rand_int));
					}
					else
						buferEnvio = "_NOMOREWORDS";	//Cuando no tenemos nada que decir

					outPrinter.println(buferEnvio);

					// Leemos si está o no envenenada
					buferRecepcion = inReader.readLine();
				}
			}

			//Se lee si ganamos o no
			//Las posibilidades son YOU WIN, YOU LOST o YOU WIN, PERFECT
			buferRecepcion = inReader.readLine();

			reader.close();

		} catch (FileNotFoundException ex){
			System.out.printf("Error leyendo el archivo\n");
		}catch (IOException ex){
			System.out.printf("Error en la salida entrada\n");
		}

	}
}
