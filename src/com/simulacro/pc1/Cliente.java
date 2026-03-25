package com.simulacro.pc1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class Cliente {
	
	private final String HOST = "localhost";
	private final int PUERTO = 13;
	
	public Cliente() {
	
		try {
			Socket cliente = new Socket(HOST, PUERTO);
			
			//flujos de la comunicación para enviar un archivo PDF
			File archivoPDF = new File("C:/cliente/data.zip");
			FileInputStream fis = new FileInputStream(archivoPDF);
			DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
			 
			
			//Enviar el achivo PDF al servidor
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				salida.write(buffer, 0, bytesRead);
			}
		
			salida.flush();
					
			fis.close();
			salida.close();
			cliente.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new GenerarArchivos().generar();
		new Cliente();
	}

}
