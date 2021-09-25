package AccionesSemanticas;

import java.util.HashMap;

import Analizadores.AnalizadorLexico;

//Controlar 22 caracteres

public class AS7 extends AS{
	
	public AS7() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		
		lexico.disminuirContador(); //para quedarme con lo ultimo leido
		HashMap <String,Object> atributos = lexico.getAtributos(); //getAtributos: devuelve el lexema pasado a string
		if (atributos != null) {
			if ((boolean) atributos.get("Reservada")) {  //esta la palabra reservada en el lexema
				return null;
			}
		}
		else { //recorte del lexema a 22 caracteres unicamente
			StringBuilder lexemaCompleto = lexico.getLexema(); //devuelve el lexema
			if (lexemaCompleto.length() > 22) {
				lexemaCompleto.delete(22, lexemaCompleto.length()); //el lexico excede los 22 caracteres, entonces elimina todo lo que esta luego de este maximo
				lexico.addError("Warning: el identificador fue truncado a 22 caracteres");
			}
			lexico.agregarLexemaATS();
		}
		return "ID";
	}
	

}
