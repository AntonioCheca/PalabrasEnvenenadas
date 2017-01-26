import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PWServidor{
	public static void main(String[] args) {
		ServerSocket socketServidor;
		int port = 8989;

		try {
			socketServidor = new ServerSocket(port);
			do {
				Socket socketConexion_J1 = socketServidor.accept();
				Socket socketConexion_J2 = socketServidor.accept();
				System.out.println("Se ha conseguido establecer una conexión con los dos clientes.");
				ProcesadorPW procesador = new ProcesadorPW(socketConexion_J1, socketConexion_J2);
				procesador.start();
			} while (true);
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}
	}
}
