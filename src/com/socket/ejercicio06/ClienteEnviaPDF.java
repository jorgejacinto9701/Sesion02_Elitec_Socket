package com.socket.ejercicio06;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class ClienteEnviaPDF {

	private final String HOST = "localhost";
	private final int PUERTO = 13;
	
	public ClienteEnviaPDF() {
		try {
			Socket cliente = new Socket(HOST, PUERTO);
			
			//flujos de la comunicación para enviar un archivo PDF
			File archivoPDF = new File("C:/cliente/archivo.pdf");
			FileInputStream fis = new FileInputStream(archivoPDF);
			DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
			
			
			//Enviar el achivo PDF al servidor
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				salida.write(buffer, 0, bytesRead);
			}
		
			salida.flush();
			System.out.println("Archivo PDF enviado al servidor");
			
			fis.close();
			salida.close();
			cliente.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ClienteEnviaPDF();
	}
	
}
