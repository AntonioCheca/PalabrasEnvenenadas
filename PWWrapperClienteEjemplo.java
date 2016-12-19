//
// PoisonedWords
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
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

public class PWWrapper {
	private static String name = "Charmander";


	public static void main(String[] args) {
   OutputStream procOut = null;
   InputStream procIn = null;

	 BufferedReader inReaderServer = null;
	 PrintWriter outPrinterServer = null;
	 BufferedReader inReader = null;
	 PrintWriter outPrinter = null;

	 String buferRecepcion, buferEnvio;

   try
   {
			//Aquí va vuestro programa
      Process process = Runtime.getRuntime().exec ("./PWClienteEjemplo");
      procOut = process.getOutputStream();
      procIn = process.getInputStream();

			inReader = new BufferedReader(new InputStreamReader(procIn));
			outPrinter = new PrintWriter(procOut,true);

			// Nombre del host donde se ejecuta el servidor:
			String host="localhost";
			// Puerto en el que espera el servidor:
			int port=8989;

			// Socket para la conexión TCP
			Socket socketConexion=null;

			// Creamos un socket que se conecte a "hist" y "port":
			//////////////////////////////////////////////////////
			socketConexion=new Socket(host,port);
			//////////////////////////////////////////////////////

			inReaderServer = new BufferedReader(new InputStreamReader(socketConexion.getInputStream()));
			outPrinterServer = new PrintWriter(socketConexion.getOutputStream(),true);



			buferRecepcion = inReader.readLine();
			outPrinterServer.println("NAME " + buferRecepcion);

			inReaderServer.readLine();	//Es la confirmación del nombre, se puede ignorar
			buferRecepcion = inReaderServer.readLine();	//Vemos quién empieza
			if(buferRecepcion.equals("START 1")){
				outPrinter.println("1");
				buferRecepcion = inReader.readLine();
				outPrinterServer.println("WORD " + buferRecepcion);
				buferRecepcion = inReaderServer.readLine();
				if(buferRecepcion.equals("POISON 1"))
					outPrinter.println("1");
				else
					outPrinter.println("0");
			}
			else
				outPrinter.println("0");

			boolean game_on = true;
			while(game_on){
				buferRecepcion = inReaderServer.readLine();
				if(buferRecepcion.equals("GAME OVER")){
					outPrinter.println("GAME OVER");
					game_on = false;
				}
				else{
					String[] palabrasSeparadas = buferRecepcion.split(" ");
					buferRecepcion = palabrasSeparadas[1];
					outPrinter.println(buferRecepcion);
					buferRecepcion = inReader.readLine();
					outPrinterServer.println("WORD " + buferRecepcion);
					buferRecepcion = inReaderServer.readLine();
					if(buferRecepcion.equals("POISON 1"))
						outPrinter.println("1");
					else
						outPrinter.println("0");
				}
			}
			buferRecepcion = inReaderServer.readLine();
			outPrinter.println(buferRecepcion);

   }
   catch (IOException ioe)
   {
      System.out.println(ioe);
	 }
	}
}
