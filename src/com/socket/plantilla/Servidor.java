package com.socket.plantilla;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	private final int PORT = 13;
	
	public Servidor() {

		Socket cliente = null;
		try (ServerSocket servidor = new ServerSocket(PORT)) {
			System.out.println("Servidor iniciado en el puerto " + PORT);
			
			//Bucle infinito para atender a los clientes que se conecten
			while (true) {

				//Esperar a que un cliente se conecte(Se detiene el programa hasta que un cliente se conecte)
				cliente = servidor.accept();
				System.out.println("Se inicia la atención al cliente...");
				
				//Imprime la IP del cliente que se ha conectado
				System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
			
				//flujos de la comunicación
				BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
				PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
				
				
				//comunicacion con el cliente
				String mensaje = entrada.readLine();
				System.out.println("Mensaje recibido del cliente: " + mensaje);
				
				//respuesta al cliente
				salida.println("Hola cliente, soy el servidor");
				
				cliente.close();
				System.out.println("Se ha cerrado la conexión con el cliente");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new Servidor();
	}
	
}