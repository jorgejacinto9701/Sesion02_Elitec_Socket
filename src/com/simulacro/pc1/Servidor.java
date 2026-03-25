package com.simulacro.pc1;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	private final int PUERTO = 13;
	
	public Servidor() {
	
		Socket cliente = null;
		try (ServerSocket servidor = new ServerSocket(PUERTO)) {
			System.out.println("Servidor iniciado en el puerto " + PUERTO);
			
			//Bucle infinito para atender a los clientes que se conecten
			while (true) {

				//Esperar a que un cliente se conecte(Se detiene el programa hasta que un cliente se conecte)
				cliente = servidor.accept();
				System.out.println("Se inicia la atención al cliente...");
				
				//Imprime la IP del cliente que se ha conectado
				System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
			
				//flujos de la comunicación
				//Se obtiene el PDF de llegada
				File fileLlegada = new File("C:/servidor/data.zip");
				FileOutputStream fos = new FileOutputStream(fileLlegada);
				DataInputStream entrada = new DataInputStream(cliente.getInputStream());
				
				//comunicacion con el cliente
				byte[] buffer = new byte[4096];
				int bytesLeidos;
				while ((bytesLeidos = entrada.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesLeidos);
				}
				
				fos.close();
				entrada.close();
				
				
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
