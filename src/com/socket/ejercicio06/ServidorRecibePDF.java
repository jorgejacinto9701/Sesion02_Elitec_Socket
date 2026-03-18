package com.socket.ejercicio06;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ServidorRecibePDF {


	private final int PORT = 13;
	
	public ServidorRecibePDF() {

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
				//Se obtiene el PDF de llegada
				File fileLlegada = new File("C:/servidor/pdfLlegada.pdf");
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
				
				//Convertir a un archivo ZIP
				FileInputStream fis = new FileInputStream(fileLlegada);
				ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("C:/servidor/comprimido.zip"));
				
                ZipEntry zipEntry = new ZipEntry("C:/servidor/pdfLlegada.pdf");
                zipOutputStream.putNextEntry(zipEntry);
                
                // agregar los bytes al archivo zip
                int byteLeidos = 0;
                while ((byteLeidos = fis.read()) != -1) {
                    zipOutputStream.write(byteLeidos);
                }
               
                fis.close();
                zipOutputStream.closeEntry();
                zipOutputStream.close();

				cliente.close();
				System.out.println("Se ha cerrado la conexión con el cliente");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new ServidorRecibePDF();
	}
}
