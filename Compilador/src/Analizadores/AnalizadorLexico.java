package Analizadores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
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
	private int ultimoEstado;
	private int contadorFila;
	private int contadorColumna;
	public ArrayList<String> erroresPosibles=new ArrayList<String>();
	private StringBuilder lexema;
	private HashMap<String, Integer> codigosTokens;
	private HashMap<Character, Integer> mapCaracterColumna;
	private static final int F = -1; //estado final
	private static final int M = -2; //estado de error
	
	public AnalizadorLexico() {
		
		//Inicializaciones
		contadorFila=0;
		contadorColumna=0;
		
		
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
			reader = new BufferedReader(new FileReader(archivo));
			String line = reader.readLine();
			while (line != null) {
				StringBuilder linea = (new StringBuilder(line)).append('\n');
				allLines.add(line);
				// lee la siguiente linea 
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	//INICIALIZO TABLA DE SIMBOLOS CON PALABRAS RESERVADAS
	
			tablaSimbolos=new HashMap<String, HashMap<String, Object>>();
			tablaSimbolos.put("IF", new HashMap<String, Object>());
			tablaSimbolos.get("IF").put("Reservada", true);
			tablaSimbolos.put("ELSE", new HashMap<String, Object>());
			tablaSimbolos.get("ELSE").put("Reservada", true);
			tablaSimbolos.put("ENDIF", new HashMap<String, Object>());
			tablaSimbolos.get("ENDIF").put("Reservada", true);
			tablaSimbolos.put("PRINT", new HashMap<String, Object>());
			tablaSimbolos.get("PRINT").put("Reservada", true);
			//tablaSimbolos.put("INT", new HashMap<String, Object>());
			//tablaSimbolos.get("INT").put("Reservada", true);
			tablaSimbolos.put("BEGIN", new HashMap<String, Object>());
			tablaSimbolos.get("BEGIN").put("Reservada", true);
			//tablaSimbolos.put("VOID", new HashMap<String, Object>());
			//tablaSimbolos.get("VOID").put("Reservada", true);
			tablaSimbolos.put("END", new HashMap<String, Object>());
			tablaSimbolos.get("END").put("Reservada", true);
			tablaSimbolos.put("LONG", new HashMap<String, Object>());
			tablaSimbolos.get("LONG").put("Reservada", true);
			tablaSimbolos.put("SINGLE", new HashMap<String, Object>());
			tablaSimbolos.get("SINGLE").put("Reservada", true);
			/*
			tablaSimbolos.put("FOR", new HashMap<String, Object>());
			tablaSimbolos.get("FOR").put("Reservada", true);
			tablaSimbolos.put("CLASS", new HashMap<String, Object>());
			tablaSimbolos.get("CLASS").put("Reservada", true);
			tablaSimbolos.put("EXTENDS", new HashMap<String, Object>());
			tablaSimbolos.get("EXTENDS").put("Reservada", true);
			*/
			tablaSimbolos.put("REPEAT", new HashMap<String, Object>());
			tablaSimbolos.get("REPEAT").put("Reservada", true);
			tablaSimbolos.put("BREAK", new HashMap<String, Object>());
			tablaSimbolos.get("BREAK").put("Reservada", true);
			tablaSimbolos.put("RETURN", new HashMap<String, Object>());
			tablaSimbolos.get("RETURN").put("Reservada", true);
			tablaSimbolos.put("THEN", new HashMap<String, Object>());
			tablaSimbolos.get("THEN").put("Reservada", true);
			tablaSimbolos.put("FUNC", new HashMap<String, Object>());
			tablaSimbolos.get("FUNC").put("Reservada", true);
			
			//ASIGNO CODIGO DE TOKEN			
			
			codigosTokens= new HashMap<String, Integer>();


			codigosTokens.put("(", 40);
			codigosTokens.put(")", 41);
			codigosTokens.put("*", 42);
			codigosTokens.put("+", 43);
			codigosTokens.put(",", 44);
			codigosTokens.put("-", 45);
			codigosTokens.put(".", 46);
			codigosTokens.put("/", 47);
			codigosTokens.put(";", 59);
			codigosTokens.put("<", 60);
			codigosTokens.put("=", 61);
			codigosTokens.put(">", 62);
			codigosTokens.put("_", 95);
			codigosTokens.put("ID", 257);
			codigosTokens.put("CTE", 258);
			codigosTokens.put("IF", 259);
			codigosTokens.put("ELSE", 260);
			codigosTokens.put("END_IF", 261);
			codigosTokens.put("PRINT", 262);
			codigosTokens.put("THEN", 263);
			codigosTokens.put("BEGIN", 264);
			codigosTokens.put("END", 265);
			codigosTokens.put("LONG", 266);
			codigosTokens.put("SINGLE", 267);
			codigosTokens.put("RETURN", 268);
			//codigosTokens.put("class", 268);
			codigosTokens.put("FUNC", 269);
			codigosTokens.put("CADENA", 270);
			codigosTokens.put("ERROR", 271);
			codigosTokens.put("BREAK", 272);
			codigosTokens.put(">=", 273);
			codigosTokens.put("<=", 274);
			codigosTokens.put("==", 275);
			codigosTokens.put("<>", 276);
			codigosTokens.put(":=", 277);
			codigosTokens.put("REPEAT", 278);
			codigosTokens.put("&&", 279);
			codigosTokens.put("||", 280);
			
			
			//MAPEO CADA CHARACTER CON EL NUMERO DE COLUMNA CORRESPONDIENTE
			
			mapCaracterColumna = new HashMap<Character, Integer>();
			char[] letras = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
			for(int i=0; i<letras.length; i++) {
				mapCaracterColumna.put(letras[i], 0); //Letras minisculas
				mapCaracterColumna.put(Character.toUpperCase(letras[i]), 13); //Letras mayusculas
			}
			
			char[] digitos = {'0','1','2','3','4','5','6','7','8','9'};
			for(int i=0; i<digitos.length; i++) {
				mapCaracterColumna.put(digitos[i], 1);
			}
			mapCaracterColumna.put('_', 2);
			mapCaracterColumna.put('*', 3);
			mapCaracterColumna.put('+', 4);
			mapCaracterColumna.put('-', 5);
			mapCaracterColumna.put('&', 6);
			mapCaracterColumna.put('|', 7);
			mapCaracterColumna.put('.', 8);
			mapCaracterColumna.put(':', 9);
			mapCaracterColumna.put('=', 10);
			mapCaracterColumna.put('<', 11);
			mapCaracterColumna.put('>', 12);
			mapCaracterColumna.put('S', 14);
			mapCaracterColumna.put('%', 15);
			mapCaracterColumna.put('\n', 16);
			mapCaracterColumna.put('	', 17);
			mapCaracterColumna.put(' ', 18);
			mapCaracterColumna.put('/', 19);
			mapCaracterColumna.put('(', 19);
			mapCaracterColumna.put(')', 19);
			mapCaracterColumna.put(',', 19);
			mapCaracterColumna.put(';', 19);
			
	//INICIALIZO MATRIZ TRANSICION DE ESTADOS
	
			matrizTransicionEstados= new int[][] {
				{1,2,1,7,2,2,17,18,3,9,12,11,10,1,1,13,0,0,0,F, M},
				{1,1,1,F,F,F,F,F,F,F,F,F,F,1,1,F,F,F,F,F, F},
				{F,2,F,F,F,F,F,F,4,F,F,F,F,F,F,F,F,F,F,F, F},
				{M,4,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M, M},
				{F,4,F,F,F,F,F,F,F,F,F,F,F,F,5,F,F,F,F,F, F},
				{M,6,M,M,6,6,M,M,M,M,M,M,M,M,M,M,M,M,M,M, M},
				{F,6,F,F,F,F,F,F,F,F,F,F,F,F,M,F,F,F,F,F, F},
				{F,F,F,8,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F, F},
				{8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,F,8,8,8, 8},
				{M,M,M,M,M,M,M,M,M,M,F,M,M,M,M,M,M,M,M,M, M},
				{F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F, F},
				{F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F, F},
				{F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F, F},
				{13,13,13,13,14,13,13,13,13,13,13,13,13,13,13,F,M,13,13,13, 13},
				{M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,15,M,M,M, M},
				{M,M,M,M,16,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M, M},
				{13,13,13,13,M,13,13,13,13,13,13,13,13,13,13,M,M,13,13,13, 13},
				{M,M,M,M,M,M,F,M,M,M,M,M,M,M,M,M,M,M,M,M, M},
				{M,M,M,M,M,M,M,F,M,M,M,M,M,M,M,M,M,M,M,M, M},
			};
				
	//INICIALIZO MATRIZ ACCIONES SEMANTICAS
			
			AS AS1 = new AS1();
			AS AS2 = new AS2();
			AS AS3 = new AS3();
			AS AS4 = new AS4();
			AS AS5 = new AS5();
			AS AS6 = new AS6();
			AS AS7 = new AS7();
			AS AS9 = new AS9();
			AS AS11 = new AS11();
			AS AS12 = new AS12();
			AS AS14 = new AS14();
			AS AS15 = new AS15();
		
			
matrizAccionesSemanticas = new AS[][] {
	{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS4,AS1,AS1,AS2, AS11},/*Inicial*/
	{AS14,AS14,AS14,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS14,AS14,AS7,AS7,AS7,AS7,AS7, null},/*ID y PR*/
	{AS6,AS14,AS6,AS6,AS6,AS6,AS6,AS6,AS14,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6, null},/*Cte long*/
	{AS12,AS14,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12, null},/*Cte float*/
	{AS5,AS14,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS14,AS5,AS5,AS5,AS5,AS5, null},/*Cte float*/
	{AS12,AS14,AS12,AS12,AS14,AS14,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12, null},/*Cte float*/
	{AS5,AS14,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS12,AS5,AS5,AS5,AS5,AS5, null},/*Cte Float*/
	{AS9,AS9,AS9,AS1,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9, null},/*Comentarios*/
	{AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS4,AS1,AS1,AS1, null},/*Comentarios*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS14,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12, null},/*Op Asignacion*/
	{AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS14,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9, null},/*Comparador > o >=*/
	{AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS14,AS9,AS14,AS9,AS9,AS9,AS9,AS9,AS9,AS9, null},/*Comparador < o <= o <>*/
	{AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS14,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9, null},/*Comparador = o ==*/
	{AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS3,AS12,AS14,AS14,AS14, null},/*Cadena Multilinea*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS15,AS12,AS12,AS12, null},/*Cadena Multilinea*/
	{AS12,AS12,AS12,AS12,AS14,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12, null},/*Cadena Multilinea*/
	{AS14,AS14,AS14,AS14,AS12,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS14,AS12,AS12,AS14,AS14,AS14, null},/*Cadena Multilinea*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS14,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12, null},/*AND*/
	{AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS14,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12,AS12, null}/*OR*/
	};
	}
	
	
	//Metodos utilizados en las acciones semanticas
	
	public HashMap<String, HashMap<String, Object>> getTablaSimbolos(){
		return this.tablaSimbolos;
	}
	
	public void agregarAtributoLexema(String lexema, String key, Object valor) {
		tablaSimbolos.get(lexema).put(key, valor);
	}
	
	public void agregarLexemaATS() {
		if(tablaSimbolos.containsKey(lexema.toString())) {
			tablaSimbolos.get(lexema.toString()).put("Contador",((Integer)tablaSimbolos.get(lexema.toString()).get("Contador"))+1); //actualiza Contador
		}else {
			tablaSimbolos.put(lexema.toString(), new HashMap<String,Object>());
			tablaSimbolos.get(lexema.toString()).put("Reservada", false);
			tablaSimbolos.get(lexema.toString()).put("Contador", 1);
		}
	}
	
	
	public void inicializarLexema() {
		lexema = new StringBuilder();
	}
	
	public void agregarCaracter(char c) {
		lexema.append(c);
	}
	
	public StringBuilder getLexema() {
		return lexema;
	}
	
	public void disminuirContador() {
		if(contadorColumna!=0)
			contadorColumna--; //vuelvo 1 para reconocer el ultimo caracter leido en la fila
		else {
			contadorFila--;
			contadorColumna=allLines.get(contadorFila).length(); //lo posiciono en el ultimo caracter de la fila anterior por estar en la columna 0
		}
	}
	
	public void aumentarFila() {
		contadorFila++;
		contadorColumna = 0;
	}


	public HashMap<String, Object> getAtributos() {
		return tablaSimbolos.get(lexema.toString());
	}

	
	//REVISAR BIEN ESTO TIRA ERROR
	public void addError(String s) {
		erroresPosibles.add("Numero de linea: " + (contadorFila+1) + " - " + s);
	}
	
	public ArrayList<String> getErrores() {
		return erroresPosibles;
	}
	
	public int getContadorFila() {
		return contadorFila;
	}
	
	
	//Funcion principal a invocar
	
	public int yylex() {
		ultimoEstado=0; //estado inicial
		String token = null; 
		while(ultimoEstado > F) { //mientras que no estoy en estado final ni error "M"
 			char proximoCaracter;
			if(allLines.get(contadorFila).length() == contadorColumna) { //estoy en el ultimo caracter de la linea
				proximoCaracter = '\n'; 
			}else {
				proximoCaracter=allLines.get(contadorFila).charAt(contadorColumna); //sino estoy en el ultimo caracter de la linea, guardo en variable el caracter
			}
			contadorColumna++; //aumento la columna al otro caracter
			int columnaCaracter;
			if(mapCaracterColumna.containsKey(proximoCaracter)) { //si el caracter leido anteriormente existe en las matrices o es columna de las matrices
				columnaCaracter=mapCaracterColumna.get(proximoCaracter);//guardo el numero de columna en la variable 
			}else {
				columnaCaracter=20; //le asigno una columna que no existe aun
			}
			if(matrizAccionesSemanticas[ultimoEstado][columnaCaracter] != null) { //si tiene accion semantica asignada
				token=matrizAccionesSemanticas[ultimoEstado][columnaCaracter].aplicar(this, proximoCaracter); //ejecuta metodo de accion semantica
			}
			ultimoEstado=matrizTransicionEstados[ultimoEstado][columnaCaracter];//guarda el proximo estado 
		}
/*
		if (token == null) { //para el caso del comentario, revisar***
			return 0;
		}
*/		
		if(token != null && (token.equals("ID") || token.equals("CTE") || token.equals("CADENA"))) { //todo lo valido
			return codigosTokens.get(token); //retorno el codigo del token
		}else if(token != null && token.equals("ERROR")){ //me trairia el error
			return codigosTokens.get(token); //retorno el codigo del token
		}else {
			return codigosTokens.get(lexema.toString()); //paso a string el lexema, al no ser ID, CTE, CADENA o ERROR, devuelvo su codigo token asociado.
		}
	}
	
	//me traigo la tabla de simbolos entera
	
	public String getDatosTablaSimbolos() {
			StringBuilder texto =  new StringBuilder();
			for(String s : tablaSimbolos.keySet()) { //por cada clave dentro de la tabla de simbolos 
				texto.append(s+":\r\n"); //agrega la clave leida al texo y hago una nueva linea
				for(String prop : tablaSimbolos.get(s).keySet()) { //busco la clave del hash interno segun el caracter leido anteriormente
					texto.append("    "+prop+": "+tablaSimbolos.get(s).get(prop).toString()+"\r\n");  //me traigo lo de adentro del hashmap interior, con el prop llego a reservada
				}
			}
			return texto.toString();
		}

	
	//Para el parser o analizador sintactico
	public boolean verificarRango(String lexema){
		if(tablaSimbolos.get(lexema).get("Tipo") == "int") {
			BigDecimal bd = new BigDecimal(lexema);
			if(new BigDecimal("32767").compareTo(bd)<=0) {
				return false;
			}
		}
		return true;
	}		
	
	/*
	//Para el parser o analizador sintactico
	
	public void actualizarTablaSimbolos(String lexema) {
		if(tablaSimbolos.containsKey("-"+lexema)) {
			tablaSimbolos.get("-"+lexema).put("Contador",((Integer)tablaSimbolos.get("-"+lexema).get("Contador"))+1);
		}else {
			tablaSimbolos.put("-"+lexema, new HashMap<String,Object>());
			tablaSimbolos.get("-"+lexema).put("Reservada", false);
			tablaSimbolos.get("-"+lexema).put("Tipo",tablaSimbolos.get(lexema).get("Tipo"));
			tablaSimbolos.get("-"+lexema).put("Contador", 1);
		}
		tablaSimbolos.get(lexema).put("Contador",((Integer)tablaSimbolos.get(lexema).get("Contador"))-1);
		if(((Integer)tablaSimbolos.get(lexema).get("Contador")) == 0) {
			tablaSimbolos.remove(lexema);
		}
	}
	*/
	
	
	
	public static void main(String[] args) throws IOException {
		
		AnalizadorLexico lexico = new AnalizadorLexico();
		
		int cantidad_lineas= lexico.allLines.size(); //cantidad de lineas del archivo que leo
		
		for (int i=1; i<= cantidad_lineas; i++)
				System.out.println( lexico.yylex()); //imprime los token de cada linea
	}
}