package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//Aumentar linea y agregar al buffer no vacio

public class AS15 extends AS{
	
	public AS15() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.agregarCaracter(c);
		lexico.aumentarFila();
		return null;
	}

}
