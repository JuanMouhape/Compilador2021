package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//fin de comentario

public class AS4 extends AS{
	
	public AS4() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.aumentarFila();
		return null;
	}

}
