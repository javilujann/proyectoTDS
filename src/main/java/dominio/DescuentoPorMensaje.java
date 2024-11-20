package dominio;

public class DescuentoPorMensaje implements Descuento{
	private int numeroMensajes;
	private int threshold;
	
	
	public DescuentoPorMensaje(int numeroMensajes, int threshold) {
		super();
		this.numeroMensajes = numeroMensajes;
		this.threshold = threshold;
	}

	@Override
	public float aplicarDescuento(float precioBase, float porcentaje) {
		if(numeroMensajes < threshold) return precioBase; 
		return precioBase*(porcentaje/100);
	}


}
