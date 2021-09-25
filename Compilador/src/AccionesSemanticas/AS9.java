package AccionesSemanticas;

//Simbolos de un solo caracter

import Analizadores.AnalizadorLexico;

public class AS9 extends AS{
	
	public AS9() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.disminuirContador();
		return null;
	}
	

}
