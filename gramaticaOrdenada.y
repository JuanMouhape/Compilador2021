%{	
	package Analizadores;
	import java.util.ArrayList;
	import java.io.PrintWriter;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import javax.swing.JFileChooser;
%} 

%token ID CTE IF ELSE ENDIF PRINT THEN BEGIN END LONG SINGLE RETURN FUNC CADENA ERROR BREAK MAYOR_IGUAL MENOR_IGUAL IGUAL DISTINTO ASIGN REPEAT AND OR POST TRY CATCH
%%

Programa: ID Sentencias_declarativas Bloque_sentencias_ejecutables {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa aceptado.");}
	;

Declaracion_variables_tipo_funcion: Encabezado_funcion Lista_variables
		;
		
Encabezado_funcion: Tipo FUNC '(' Tipo ')'
		;
	
Declaracion_funcion: Tipo FUNC ID '(' Parametro ')' Sentencias_declarativas BEGIN Bloque_sentencias_ejecutables RETURN '(' Retorno ')' END  {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Declaracion de funciones. ");}
		| Tipo FUNC ID '(' Parametro ')' Sentencias_declarativas BEGIN Bloque_sentencias_ejecutables RETURN '(' Retorno ')' POST ':' '('  Condicion ')' ';' END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Declaracion de funciones con POST. ");}
		;
		
Retorno: Expresion
	;
	
Parametro: Tipo ID
	;
	
Sentencias_declarativas: Sentencia_dec
			| Sentencia_dec Sentencias_declarativas
			;

		
Sentencia_dec: Tipo Lista_variables; {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia declarativa ");}

Lista_variables: ID
		| ID ',' Lista_variables
		;
		
Tipo: LONG
      | SINGLE
	;
	
Try_Catch: TRY Sentencia_ej CATCH Bloque_sentencias_ejecutables END';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con TRY CATCH - BEGIN Y END. ");}
	;

Bloque_sentencias_ejecutables: Sentencia_ej
				| BEGIN Sentencias_ejecutables END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con sentencias ejecutables con BEGIN Y END. ");}
				;


Sentencias_ejecutables: Sentencia_ej
	   | Sentencia_ej Sentencias_ejecutables
	   ;
	   
Sentencia_ej: Asignacion
	        | Seleccion
	        | Comparacion
	        | Impresion
		;
		
Condicion: Comparacion
	| Expresion
	| Comparacion AND Comparacion
	| Comparacion OR Comparacion
	| Expresion AND Expresion
	| Expresion OR Expresion
	;
	
Seleccion: IF '(' Condicion ')' THEN Bloque_sentencias_ejecutables ELSE Bloque_sentencias_ejecutables ENDIF';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia if else ");}
	| IF '(' Condicion ')' THEN Bloque_sentencias_ejecutables ENDIF';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia if ");}
	;
	
Comparacion: Expresion '>' Expresion
	     | Expresion '<' Expresion
	     | Expresion MAYOR_IGUAL Expresion
     	 | Expresion MENOR_IGUAL Expresion
	     | Expresion IGUAL Expresion
	     | Expresion DISTINTO Expresion

	     ;
		 

Impresion: PRINT '(' CADENA ')' ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia print ");}
	;

Asignacion: ID ASIGN Expresion';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia asignacion de variable. ");}
	;
	
Expresion:
	| Tipo '('Expresion_bis')'
	;

Expresion_bis: Termino
	| Termino '+' Expresion
	| Termino '-' Expresion
	;

Termino: Factor
	| Termino '*'  Factor
	| Termino '/' Factor
	| Invocar_funcion
	;
	

Invocar_funcion: ID '(' Parametro ')'
		;
		
Factor: id
	|cte
	;
	


id: ID;
cte: CTE;

%%

AnalizadorLexico aLexico=new AnalizadorLexico();
	//ArrayList<String> errores = new ArrayList<String>();
	ArrayList<String> salida = new ArrayList<String>();
	ArrayList<String> tokens = new ArrayList<String>();

	int yylex(){
		int token = aLexico.yylex();
		if(token == 257 || token == 258 || token  == 270) yylval = aLexico.getyylval();
		tokens.add(new Integer(token).toString());
		return token;
	}

	//void yyerror(String error) {
	//errores.add("Numero de linea: "+ (aLexico.getContadorFila()+1) +" - "+error);
	//}

	/*public ArrayList<String> getErrores() {
		ArrayList<String> erroresTotales = new ArrayList<String>();
		erroresTotales.add("Errores lexicos:");
		erroresTotales.addAll(aLexico.getErrores());		
		erroresTotales.add("Errores sintacticos:");
		erroresTotales.addAll(errores);
		return erroresTotales;
	}
	*/

	public void saveFile(){

	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("choosertitle");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

		    File archivo = chooser.getSelectedFile();
	
		    try {
	
		    	PrintWriter writer = new PrintWriter(archivo+"\\salidaCompilador.txt");
		    	writer.print("");
		    	FileWriter escribir = new FileWriter(archivo+"\\salidaCompilador.txt", true);
		    	StringBuilder sb = new StringBuilder("\r\n");
				
		    	escribir.write("Tokens reconocidos:");
				escribir.write(sb.toString());
				escribir.write(sb.toString());
				for(String s: tokens){
					escribir.write(s.toString()+ " - ");
				}

				escribir.write(sb.toString());
				escribir.write(sb.toString());
		    	escribir.write("Estructuras reconocidas:");
				escribir.write(sb.toString());
				escribir.write(sb.toString());
				for(String s: salida){
					escribir.write(s.toString());
					escribir.write(sb.toString());
				}

				escribir.write(sb.toString());
				//for(String s: this.getErrores()){
					//escribir.write(s.toString());
					//escribir.write(sb.toString());
				//}

		        escribir.close();
	
		    } catch (FileNotFoundException ex) {
		    } catch (IOException ex) {
		    }
	    }

	}

	public static void main(String args[])
	{
		Parser par = new Parser(false);
		System.out.println(par.yyparse());
		par.saveFile();
	}