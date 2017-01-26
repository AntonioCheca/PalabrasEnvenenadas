import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PWServidor {
	private static ArrayList<String> getDiccionario() throws FileNotFoundException, IOException {
		ArrayList<String> diccionario = new ArrayList<>();

		File file = new File("./listado-general-new.txt");
		FileInputStream fis = null;
		BufferedReader reader = null;

		fis = new FileInputStream(file);
		reader = new BufferedReader(new InputStreamReader(fis));

		String word = reader.readLine();
		while(word != null){
			diccionario.add(word);
			word = reader.readLine();
		}

		return diccionario;
	}

	public static void main(String[] args) {
		ServerSocket socketServidor;
		int port = 8989;

		try {
			socketServidor = new ServerSocket(port);
			ArrayList<String> diccionario = getDiccionario();
			System.out.println("Diccionario leído.");
			do {
				Socket socketConexion_J1 = socketServidor.accept();
				Socket socketConexion_J2 = socketServidor.accept();
				System.out.println("Se ha conseguido establecer una conexión con los dos clientes.");
				ProcesadorPW procesador = new ProcesadorPW(socketConexion_J1, socketConexion_J2, new ArrayList<String>(diccionario));
				procesador.start();
			} while (true);
		} catch (FileNotFoundException ex){
			System.out.println("Error leyendo el diccionario");
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}
	}
}
