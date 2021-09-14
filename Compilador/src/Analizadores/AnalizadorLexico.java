package Analizadores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import AccionesSemanticas.*;

import javax.swing.JFileChooser;

import AccionesSemanticas.AS;

public class AnalizadorLexico{
	private File archivo;
	private ArrayList<String> allLines;
	
	private HashMap<String, HashMap<String, Object>> tablaSimbolos;
	private int[][] matrizTransicionEstados;
	private AS[][] matrizAccionesSemanticas;
	
	private static final int F = 0; //estado final
	
	public AnalizadorLexico() {
		
		//JFILECHOOSER

		JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivo = fc.getSelectedFile();
        }
       
        //LEO TODAS LAS LINEAS DEL ARCHIVO
        
        allLines = new ArrayList<String>();
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					archivo));
			String line = reader.readLine();
			while (line != null) {
				StringBuilder linea = (new StringBuilder(line)).append('\n');
				allLines.add(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	//INICIALIZO TABLA DE SIMBOLOS CON PALABRAS RESERVADAS
	
			tablaSimbolos=new HashMap<String, HashMap<String, Object>>();
			tablaSimbolos.put("if", new HashMap<String, Object>());
			tablaSimbolos.get("if").put("Reservada", true);
			tablaSimbolos.put("else", new HashMap<String, Object>());
			tablaSimbolos.get("else").put("Reservada", true);
			tablaSimbolos.put("end_if", new HashMap<String, Object>());
			tablaSimbolos.get("end_if").put("Reservada", true);
			tablaSimbolos.put("print", new HashMap<String, Object>());
			tablaSimbolos.get("print").put("Reservada", true);
			tablaSimbolos.put("int", new HashMap<String, Object>());
			tablaSimbolos.get("int").put("Reservada", true);
			tablaSimbolos.put("begin", new HashMap<String, Object>());
			tablaSimbolos.get("begin").put("Reservada", true);
			tablaSimbolos.put("void", new HashMap<String, Object>());
			tablaSimbolos.get("void").put("Reservada", true);
			tablaSimbolos.put("end", new HashMap<String, Object>());
			tablaSimbolos.get("end").put("Reservada", true);
			tablaSimbolos.put("float", new HashMap<String, Object>());
			tablaSimbolos.get("float").put("Reservada", true);
			tablaSimbolos.put("for", new HashMap<String, Object>());
			tablaSimbolos.get("for").put("Reservada", true);
			tablaSimbolos.put("class", new HashMap<String, Object>());
			tablaSimbolos.get("class").put("Reservada", true);
			tablaSimbolos.put("extends", new HashMap<String, Object>());
			tablaSimbolos.get("extends").put("Reservada", true);
			
				
	//INICIALIZO MATRIZ TRANSICION DE ESTADOS
	
			matrizTransicionEstados= new int[][] {
				{1,2,1,7,2,2,17,18,3,9,12,11,10,1,0,13,0,0,0,F},
				{1,1,1,F,F,F,F,F,F,F,F,F,F,1,F,F,F,F,F,F},
				{F,2,F,F,F,F,F,F,4,F,F,F,F,F,F,F,F,F,F,F},
				{0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{F,4,F,F,F,F,F,F,F,F,F,F,F,F,5,F,F,F,F,F},
				{0,6,0,0,6,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{F,6,F,F,F,F,F,F,F,F,F,F,F,F,0,F,F,F,F,F},
				{F,F,F,8,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
				{8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,F,F,F,F},
				{0,0,0,0,0,0,0,0,0,0,F,0,0,0,0,0,0,0,0,0},
				{F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
				{F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
				{F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
				{13,13,13,13,14,13,13,13,13,13,13,13,13,13,13,F,13,13,13,13},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15,0,0,0},
				{0,0,0,0,16,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{13,13,13,13,0,13,13,13,13,13,13,13,13,13,13,0,15,13,13,13},
				{0,0,0,0,0,0,F,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,F,0,0,0,0,0,0,0,0,0,0,0,0},
			};
				
	//INICIALIZO MATRIZ ACCIONES SEMANTICAS
			
			AS AS1 = new AS1();
			AS AS2 = new AS2();
			AS AS3 = new AS3();
			AS AS4 = new AS4();
			AS AS5 = new AS5();
			AS AS6 = new AS6();
			AS AS7 = new AS7();
			AS AS8 = new AS8();
			AS AS9 = new AS9();
			AS AS10 = new AS10();
			AS AS11 = new AS11();
			AS AS12 = new AS12();
			AS AS13 = new AS13();
		
			
matrizAccionesSemanticas = new AS[][] {
	{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS1,AS1,AS1,AS10},/*Inicial*/
	{AS2,AS2,AS2,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS2,AS7,AS7,AS7,AS7,AS7,AS7},/*ID y PR*/
	{AS6,AS2,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS2,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},/*Cte long*/
	{AS12,AS2,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12},/*Cte float*/
	{AS5,AS2,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS2,AS5,AS5,AS5,AS5,AS5},/*Cte float*/
	{AS12,AS2,AS12,AS12,AS12,AS2,AS2,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12},/*Cte float*/
	{AS5,AS2,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS12,AS5,AS5,AS5,AS5,AS5},/*Cte Float*/
	{AS10,AS10,AS10,AS1,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10,AS10},/*Comentarios*/
	{AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS4,AS1,AS1,AS1},/*Comentarios*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS13,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12},/*Op Asignacion*/
	{AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS13,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9},/*Comparador > o >=*/
	{AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS13,AS9,AS13,AS9,AS9,AS9,AS9,AS9,AS9,AS9},/*Comparador < o <= o <>*/
	{AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS13,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9},/*Comparador = o ==*/
	{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS3,AS2,AS2,AS2,AS2},/*Cadena Multilinea*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS2,AS12,AS12,AS12},/*Cadena Multilinea*/
	{AS12,AS12,AS12,AS12,AS12,AS2,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12},/*Cadena Multilinea*/
	{AS2,AS2,AS2,AS2,AS2,AS11,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS11,AS2,AS2,AS2,AS2},/*Cadena Multilinea*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS13,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12},/*AND*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS13,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12},/*OR*/
};


}	


	
	
	
}