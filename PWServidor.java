import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//
// YodafyServidorConcurrente
// (CC) jjramos, 2012
//
public class PWServidor{

	public static void main(String[] args) {
	
		ServerSocket socketServidor;
		// Puerto de escucha
		int port=8989;
		// array de bytes auxiliar para recibir o enviar datos.
		byte []buffer=new byte[256];
		// Número de bytes leídos
		int bytesLeidos=0;
		
		try {
			// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
			//////////////////////////////////////////////////
			socketServidor = new ServerSocket(port);
		do {
				
				// Aceptamos una nueva conexión con accept()
				/////////////////////////////////////////////////
				Socket socketConexion_J1 = socketServidor.accept();
				Socket socketConexion_J2 = socketServidor.accept();
				//////////////////////////////////////////////////
				System.out.println("Se ha conseguido establecer una conexión con los dos clientes.");
				
				// Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
				// argumento el nuevo socket, para que realice el procesamiento
				// Este esquema permite que se puedan usar hebras más fácilmente.
				ProcesadorPW procesador=new ProcesadorPW(socketConexion_J1,socketConexion_J2);
				procesador.start();
				
			} while (true);
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

	}

}
