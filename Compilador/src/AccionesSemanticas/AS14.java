package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//Agregar al buffer no vacio

public class AS14 extends AS{

	
	public AS14() {
		
	}
	
	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.agregarCaracter(c);
		return null;
	}
	

}
