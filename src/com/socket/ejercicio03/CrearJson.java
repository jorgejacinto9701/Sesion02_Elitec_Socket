package com.socket.ejercicio03;

import java.io.FileWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CrearJson {

	
	public static void main(String[] args) {
		
		Auto obj1 = new Auto(1, "Toyota", "Corolla");
		Auto obj2 = new Auto(2, "Honda", "Civic");
		Auto obj3 = new Auto(3, "Ford", "Mustang");
		
		List<Auto> listaAutos = List.of(obj1, obj2, obj3);
		
		//Generar el json de gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(listaAutos);
		
		//Imprime en consola el json generado
		System.out.println(json);
		
		//Generar el archivo con la data del json
		try {
			FileWriter writer = new FileWriter("C:/cliente/autos.json");
			writer.write(json);
			writer.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	
		
	}
}
