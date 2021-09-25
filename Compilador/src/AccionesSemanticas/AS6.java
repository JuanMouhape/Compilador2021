package AccionesSemanticas;

import java.math.BigDecimal;

import Analizadores.AnalizadorLexico;

//Tipo LONG

public class AS6 extends AS{
	
	public static final long rangoMaximoPermitido = 2147483647; //(2^31)-1
	public static final long rangoMinimoPermitido = (2147483647 *(-1) -1); //(-2^31)

	
	public AS6() {
		
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		
		lexico.disminuirContador(); //ultimo caracter leido
		BigDecimal bd = new BigDecimal(lexico.getLexema().toString());
		if ((new BigDecimal (this.rangoMaximoPermitido).compareTo(bd) >= 0) && (new BigDecimal (this.rangoMinimoPermitido).compareTo(bd) <= 0)) { //esta dentro del rango permitido
			lexico.agregarLexemaATS(); //agrego a tabla de simbolos
			lexico.agregarAtributoLexema(bd.toString(), "Tipo", "long");//agrego el nombre del tipo de constante que agregue anteriormente
			return "CONSTANTE";
		}
		lexico.addError("Error: constante long fuera de rango"); 
		return "ERROR";
	}

}
