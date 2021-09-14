package AccionesSemanticas;

//Agregar al buffer 
public class AS2 extends AS{

	@Override
	public int aplicar(StringBuffer buffer, char c) {
		buffer.append(c);
		return 0;
	}

}
