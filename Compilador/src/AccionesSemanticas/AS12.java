package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

public class AS12 extends AS {
	
	public AS12() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.disminuirContador();
		lexico.addError("Error : Token invalido \""+lexico.getLexema()+"\".");
		return "ERROR";
	}

}
