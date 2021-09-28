package AccionesSemanticas;

import Analizadores.AnalizadorLexico;

//Fin float

public class AS5 extends AS{
	
	
	static final float minFloatPositivos = Float.MIN_NORMAL;
	static final float minFloatNegativos = Float.MAX_VALUE * (-1);
	static final float maxFloatPositivos = Float.MAX_VALUE;
	static final float maxFloatNegativos = Float.MIN_NORMAL * (-1);
	
	public AS5() {
		
	}
	
	public boolean rangoPermitido(float f) {
		if ((f >= this.minFloatPositivos && f<= this.maxFloatPositivos) || (f >= this.minFloatNegativos && f<= this.maxFloatNegativos) || ( f== 0.0))
			return true;
		return false;
	}
	
	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.disminuirContador();
		if (this.rangoPermitido(Float.valueOf(lexico.getLexema().toString().replace('S', 'E')))) {
			lexico.agregarLexemaATS();
			lexico.agregarAtributoLexema(lexico.getLexema().toString(), "Tipo", "Single");
		return "CTE";	
		}
		lexico.addError("Error: constante long fuera de rango"); 
		return "ERROR";

	}
	
}
