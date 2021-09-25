package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//Error por caracter invalido. Ej: cuando viene el !

public class AS11 extends AS{

	public AS11() {
		
	}
	
	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.addError("Error : Caracter invalido \""+c+"\".");
		return "ERROR";
	}
	

}
