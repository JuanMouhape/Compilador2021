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

Programa: ID conjunto_sentencias {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa aceptado.");}
	;

conjunto_sentencias: Bloque_sentencias_ejecutables {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con sentencias ejecutables sin declaraciones. ");}
			| Sentencias_declarativas Bloque_sentencias_ejecutables {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con sentencias ejecutables y declaraciones. ");}
		   ;
		
Declaracion_variables_tipo_funcion: Encabezado_funcion Lista_variables
		;
		
Encabezado_funcion: Tipo FUNC '(' Tipo ')'
		;
	
Declaracion_funcion: Tipo FUNC ID '(' Parametro ')' Sentencias_declarativas BEGIN Sentencias_ejecutables RETURN '(' Retorno ')' END  	{salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Declaracion de funciones. ");}
		| Tipo FUNC ID '(' Parametro ')' Sentencias_declarativas BEGIN Sentencias_ejecutables RETURN '(' Retorno ')' POST ':' '('  Condicion ')' ';' END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Declaracion de funciones con POST. ");}
		;
		
Retorno: Expresion
	;
	                  
Parametro: Tipo ID
	;
	
Sentencias_declarativas: Sentencia_dec 
			| Sentencias_declarativas Sentencia_dec
			;

		
Sentencia_dec: Tipo Lista_variables ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia declarativa ");}
			| Declaracion_funcion ';'
			| Declaracion_variables_tipo_funcion ';'
			;

Lista_variables: ID
		| ID ',' Lista_variables
		;
		

Bloque_sentencias_ejecutables: Sentencia_ej
				| BEGIN Sentencias_ejecutables END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con sentencias ejecutables con BEGIN Y END. ");}
				| BEGIN Sentencias_ejecutables BREAK ';' END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con sentencias ejecutables con BEGIN, END y BREAK ");}
				;

				
Try_Catch: TRY Sentencia_ej CATCH BEGIN Sentencias_ejecutables END ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con TRY CATCH - BEGIN Y END. ");}
	;



Sentencias_ejecutables: Sentencia_ej
	   | Sentencias_ejecutables Sentencia_ej
	   ;
	   
Sentencia_ej: Asignacion
	        | Seleccion
	        | Comparacion
	        | Impresion
	        | Iteracion
	        | Try_Catch
	        | Invocar_funcion
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

Iteracion:  REPEAT '(' Asignacion ';' Comparacion ')' Bloque_sentencias_ejecutables ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia repeat ");}
		;

Expresion: Termino
	|Tipo '('Expresion')'
	| Expresion '+' Termino 
	| Expresion '-' Termino 
	;

Termino: Factor
	| Termino '*'  Factor
	| Termino '/' Factor
	;
	
Invocar_funcion: ID '(' Parametro ')'
		;
		
Factor: id
	| cte
	;
	
Tipo: LONG
    | SINGLE
	;

id: ID;
cte: CTE;

%%