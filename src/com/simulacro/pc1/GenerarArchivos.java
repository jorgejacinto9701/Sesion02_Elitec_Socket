package com.simulacro.pc1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import util.MySqlDBConexion;

public class GenerarArchivos {

	public static void main(String[] args) {
		GenerarArchivos ga = new GenerarArchivos();
		ga.generar();
	}

	public void generar() {

		// PASO 1 ==> Crear una lista con la tabla alumnos en mysql
		List<Alumno> lista = new ArrayList<Alumno>();

		Connection con = MySqlDBConexion.getConexion();
		try {
			PreparedStatement st = con.prepareStatement("select * from alumno");
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Alumno a = new Alumno();
				a.setIdAlumno(rs.getInt(1));
				a.setNombre(rs.getString(2));
				a.setDni(rs.getString(3));
				a.setCorreo(rs.getString(4));
				a.setFechaNacimiento(rs.getString(5));
				lista.add(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Lista de alumnos: ==> " + lista.size());

		// PASO 2 ==> Crear un archivo JSON a partir de la lista creada en el paso 1
		// usando la libreria GSON
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(lista);

		// Generar el archivo con la data del json
		try {
			FileWriter writer = new FileWriter("C:/cliente/alumnos.json");
			writer.write(json);
			writer.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		// PASO3 ==> Crear un archivo EXCEl a partir de la lista creada en el paso 1
		// usando la libreria APACHE POI

		// Crear un archivo excel
		String[] HEADERs = { "ID", "Nombre", "Email", "Fecha Nacimiento" };
		String SHEET = "Productos";
		int[] HEADERs_WITH = { 1000, 10000, 10000, 10000 };

		try {
			FileOutputStream fileOut = new FileOutputStream("C:/cliente/alumnos.xlsx");
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET);

			// Crear el encabezado. Tamaño de las columnas
			for (int i = 0; i < HEADERs.length; i++) {
				sheet.setColumnWidth(i, HEADERs_WITH[i]);
			}

			// Crear el encabezado. Nombre de las columnas
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < HEADERs.length; i++) {
				headerRow.createCell(i).setCellValue(HEADERs[i]);
			}

			// Crear las filas
			int rowNum = 1;
			for (Alumno aux : lista) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(aux.getIdAlumno());
				row.createCell(1).setCellValue(aux.getNombre());
				row.createCell(2).setCellValue(aux.getCorreo());
				row.createCell(3).setCellValue(aux.getFechaNacimiento());
			}

			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//PASO 4 => Crear un archivo PDF con itextpdf
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/cliente/alumnos.pdf");
			Document document = new Document();
			PdfWriter.getInstance(document, fileOut);
			document.open();
			
			//generar cabecera del pdf
			document.add(new com.itextpdf.text.Paragraph("LISTA DE ALUMNOS"));
			
			//generar columnas del pdf
			document.add(new com.itextpdf.text.Paragraph("ID - NOMBRE - CORREO - FECHA NACIMIENTO"));
			
			
			
			for(Alumno aux : lista) {
				String linea = aux.getIdAlumno() + 
							" - " + aux.getNombre() + 
							" - " + aux.getCorreo() + 
							" - " + aux.getFechaNacimiento();
				document.add(new com.itextpdf.text.Paragraph(linea));
			}
			
			document.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//PASO 5 => Crear una carpeta de Nombre Data
		File carpetaData = new File("C:/cliente/data");
		carpetaData.mkdir();
		
		//PASO 6 => Mover los archivos generados en los pasos anteriores a la carpeta creada en el paso 5
		File archivoJson = new File("C:/cliente/alumnos.json");
		File archivoExcel = new File("C:/cliente/alumnos.xlsx");
		File archivoPDF = new File("C:/cliente/alumnos.pdf");
		try {
			FileUtils.moveFileToDirectory(archivoJson, carpetaData, true);
			FileUtils.moveFileToDirectory(archivoExcel, carpetaData, true);
			FileUtils.moveFileToDirectory(archivoPDF, carpetaData, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//PASO 7 => Comprimir la carpeta creada en el paso 5 a un archivo ZIP
		 FileOutputStream fos = null;
	     ZipOutputStream zipOut = null;
	     try {
	         fos = new FileOutputStream("C:/cliente/data.zip");
	         zipOut = new ZipOutputStream(fos);
	         File fileToZip = carpetaData;
	         zipFile(fileToZip, fileToZip.getName(), zipOut);
	         zipOut.close();
	         fos.close();
	  	         
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
		
	}
	
	public void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
	    if (fileToZip.isHidden()) {
	        return;
	    }
	    if (fileToZip.isDirectory()) {
	        if (fileName.endsWith("/")) {
	            zipOut.putNextEntry(new ZipEntry(fileName));
	            zipOut.closeEntry();
	        } else {
	            zipOut.putNextEntry(new ZipEntry(fileName + "/"));
	            zipOut.closeEntry();
	        }
	        File[] children = fileToZip.listFiles();
	        for (File childFile : children) {
	            zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
	        }
	        return;
	    }
	    FileInputStream fis = new FileInputStream(fileToZip);
	    ZipEntry zipEntry = new ZipEntry(fileName);
	    zipOut.putNextEntry(zipEntry);
	    byte[] bytes = new byte[1024];
	    int length;
	    while ((length = fis.read(bytes)) >= 0) {
	        zipOut.write(bytes, 0, length);
	    }
	    fis.close();
	}
}
