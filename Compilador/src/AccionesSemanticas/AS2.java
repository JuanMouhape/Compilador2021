package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//Agregar al buffer inicializando el lexema

public class AS2 extends AS{
	
	public AS2() {
		
	}

	@Override
	public String aplicar (AnalizadorLexico lexico, char c) {
		lexico.inicializarLexema();
		lexico.agregarCaracter(c);
		return null;
	}

}
