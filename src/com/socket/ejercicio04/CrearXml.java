package com.socket.ejercicio04;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class CrearXml {

	public static void main(String[] args) {
	
		Cliente cliente1 = new Cliente(1, "Juan", "Perez", "12345678");
		Cliente cliente2 = new Cliente(2, "Maria", "Gomez", "87654321");
		Cliente cliente3 = new Cliente(3, "Carlos", "Lopez", "11223344");
		
		List<Cliente> lista = new ArrayList<Cliente>();
		lista.add(cliente1);
		lista.add(cliente2);
		lista.add(cliente3);
		
		try{
		
			File file = new File("C:/cliente/clientes.xml");
			FileWriter fileWriter = new FileWriter(file);
			
			//Generar el archivo XML con XmlMapper
			XmlMapper xmlMapper = new XmlMapper();
			String xml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(lista);
			fileWriter.write(xml);
			fileWriter.close();
			System.out.println("Archivo XML creado exitosamente.");
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
}
