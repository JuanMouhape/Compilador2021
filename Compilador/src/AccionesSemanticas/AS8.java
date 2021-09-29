package AccionesSemanticas;
import Analizadores.AnalizadorLexico;

//comentario

public class AS8 extends AS{

	public AS8() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String aplicar(AnalizadorLexico lexico, char c) {
		lexico.eliminarUltimoCaracter();
		lexico.aumentarFila();
		return null;
	}

}
