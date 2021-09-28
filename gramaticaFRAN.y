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

%token ID CTE IF ELSE END_IF PRINT INT BEGIN END FLOAT FOR CLASS EXTENDS CADENA ERROR VOID MAYOR_IGUAL MENOR_IGUAL IGUAL DISTINTO ASIGN ERROR


%%
programa : conjunto_sentencias {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa aceptado.");}
    	;

conjunto_sentencias: BEGIN  sentencias_ejecutables END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con sentencias ejecutables sin declaraciones. ");}
			| declaraciones BEGIN sentencias_ejecutables END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Programa con sentencias ejecutables y declaraciones. ");}
		   ;

declaraciones: declaracion_clase
			  | declaracion_sentencia
			  | declaracion_sentencia declaraciones
			  | declaracion_clase declaraciones 
			 ;

declaracion_clase: CLASS ID BEGIN declaracion_sentencia_clase END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Declaracion de clase. ");}
		    	  |CLASS ID EXTENDS lista_variables BEGIN declaracion_sentencia_clase  END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Declaracion de clase con herencia multiple. ");}
	  			  |CLASS error EXTENDS lista_variables BEGIN declaracion_sentencia_clase  END {yyerror("Error en la definicion de la clase: falta nombre de clase");}
	  			  |CLASS error BEGIN declaracion_sentencia_clase  END {yyerror("Error en la definicion de la clase: falta nombre de clase");}
	  			  |CLASS ID error declaracion_sentencia_clase  END {yyerror("Error en la definicion de la clase: falta begin de clase");}
	  			  |CLASS ID EXTENDS lista_variables error declaracion_sentencia_clase  END {yyerror("Error en la definicion de la clase: falta begin de clase");}
	  			  |CLASS ID EXTENDS error BEGIN declaracion_sentencia_clase  END {yyerror("Error en la definicion de la clase: falta nombre de clase extendidas");}
	  			  |CLASS ID error lista_variables BEGIN declaracion_sentencia_clase  END {yyerror("Error en la definicion de la clase: falta palabra reservada extends");}
					
					
		;

declaracion_sentencia_clase: declaracion_sentencia
				| declaracion_sentencia declaracion_sentencia_clase
				| declaracion_metodo 
				| declaracion_metodo declaracion_sentencia_clase
				;

declaracion_metodo: VOID ID '(' ')' BEGIN sentencias_ejecutables END {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Declaracion de metodo. ");}
	  			  | VOID error '(' ')' BEGIN sentencias_ejecutables END {yyerror("Error en la definicion del metodo: falta nombre de metodo");}
	  			  | VOID ID '(' error ')' BEGIN sentencias_ejecutables END {yyerror("Error en la definicion del metodo: contiene parametros");}
	  			  | VOID ID error ')' BEGIN sentencias_ejecutables END {yyerror("Error en la definicion del metodo: falta parentesis (");}
	  			  | VOID ID '(' error BEGIN sentencias_ejecutables END {yyerror("Error en la definicion del metodo: falta parentesis )");}
	  			  | VOID ID '(' ')' error sentencias_ejecutables END {yyerror("Error en la definicion del metodo: falta begin");}
		         ;

declaracion_sentencia: tipo lista_variables ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia declarativa ");}
		;

tipo: INT
      | FLOAT
      | ID
      ;

lista_variables: ID
		| ID ',' lista_variables
		;

sentencias_ejecutables: sentencia_ej
	   | sentencia_ej sentencias_ejecutables
	   ;

sentencia_ej: asignacion
	        | impresion
	        | seleccion
	        | iteracion
	        | llamadometodo
			| error ';' {yyerror("Error de sentencia");}
	        ;

seleccion: IF '(' condicion ')' bloque END_IF {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia if ");}
	   | IF '(' condicion ')' bloque ELSE bloque END_IF {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia if else ");}
	   | IF '(' error ')' bloque END_IF {yyerror("Error en la definicion del if");}
	   | IF '(' error bloque END_IF {yyerror("Error en la definicion del if falta )");}
	   | IF error ')' bloque END_IF {yyerror("Error en la definicion del if falta (");}
   	   | IF '(' error ')' bloque ELSE bloque END_IF {yyerror("Error en la definicion del if");}
	   | IF '(' error bloque ELSE bloque END_IF {yyerror("Error en la definicion del if falta )");}
	   | IF error ')' bloque ELSE bloque END_IF {yyerror("Error en la definicion del if falta (");}
	   ;

iteracion:  FOR '(' asignacion condicion ';' expresion ')' bloque ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia for ");}
		   |FOR '(' error ')' bloque ';'{yyerror("Error en la definicion del for");}
		   |FOR error ')' bloque ';'{yyerror("Error en la definicion del for falta (");}
		   |FOR '(' error bloque ';'{yyerror("Error en la definicion del for falta )");}
		;

llamadometodo:  ID '.' ID '(' ')' ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - LLamado a metodo de objeto. ");}
			   |ID '.' ID error ';'{yyerror("Error en la invocacion a metodo");}
			   |ID '.' error '(' ')' ';'{yyerror("Error en la invocacion a metodo - metodo vacio");}
		;

impresion: PRINT '(' CADENA ')' ';' {salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia print ");}
		  |PRINT error ';'{yyerror("Error en la impresion");}
		;

asignacion: identificador ASIGN expresion ';'{salida.add("Linea - "+ (aLexico.getContadorFila()+1)+" - Sentencia asignacion de variable. ");}
			|identificador ASIGN error ';'{yyerror("Error en la asignacion lado derecho");}
			|error ASIGN expresion ';'{yyerror("Error en la asignacion lado izquierdo");}
	     ;

condicion: expresion MAYOR_IGUAL expresion
	     | expresion MENOR_IGUAL expresion
     	 | expresion '>' expresion
	     | expresion '<' expresion
	     | expresion IGUAL expresion
	     | expresion DISTINTO expresion
	     ;

bloque: sentencia_ej
	| BEGIN sentencias_ejecutables END
	;

expresion: expresion '+' termino
	    | expresion '-' termino
	    | termino
	    ;
termino: termino '*' factor
	| termino '/' factor
	| factor
	;

factor: identificador
		| cte 
		| ERROR
        ;

identificador: ID
			  |ID '.' ID
				;

cte : CTE { if(!aLexico.verificarRango($1.sval)){
	    	yyerror("Error : constante entera fuera de rango.");}}
    | '-' CTE {aLexico.actualizarTablaSimbolos($2.sval);}
	;
%%
	AnalizadorLexico aLexico=new AnalizadorLexico();
	ArrayList<String> errores = new ArrayList<String>();
	ArrayList<String> salida = new ArrayList<String>();
	ArrayList<String> tokens = new ArrayList<String>();

	int yylex(){
		int token = aLexico.yylex();
		if(token == 257 || token == 258 || token  == 270) yylval = aLexico.getyylval();
		tokens.add(new Integer(token).toString());
		return token;
	}

	void yyerror(String error) {
	errores.add("Numero de linea: "+ (aLexico.getContadorFila()+1) +" - "+error);
	}

	public ArrayList<String> getErrores() {
		ArrayList<String> erroresTotales = new ArrayList<String>();
		erroresTotales.add("Errores lexicos:");
		erroresTotales.addAll(aLexico.getErrores());		
		erroresTotales.add("Errores sintacticos:");
		erroresTotales.addAll(errores);
		return erroresTotales;
	}


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
				for(String s: this.getErrores()){
					escribir.write(s.toString());
					escribir.write(sb.toString());
				}

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