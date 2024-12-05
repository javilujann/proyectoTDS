package dominio;


public class ContactoIndividual extends Contacto{

	private String movil;
	private Boolean agregado;

	public ContactoIndividual(String nombre, String movil) {
		super(nombre);
		this.movil = movil;
		this.agregado = false;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public Boolean isAgregado() {
		return agregado;
	}

	public void setAgregado(Boolean agreagado) {
		this.agregado = agreagado;
	}
	
	public void enviarMensaje(Mensaje mensaje) {
		
	}

	@Override
	public boolean corresponde(String _movil) {
		return this.movil.equals(_movil);
	}

}
