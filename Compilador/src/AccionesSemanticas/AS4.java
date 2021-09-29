package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//aumentar fila

public class AS4 extends AS{
	
	public AS4() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.aumentarFila();
		return null;
	}

}
