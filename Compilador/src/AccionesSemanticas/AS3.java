package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//Fin de cadena

public class AS3 extends AS{
	
	public AS3() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.agregarLexemaATS();
		return "CADENA";
	}
	
}
