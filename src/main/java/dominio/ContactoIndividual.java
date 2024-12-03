package dominio;


public class ContactoIndividual extends Contacto{
	private String nombre;
	private String movil;

	public ContactoIndividual(String nombre, String movil) {
		super();
		this.nombre = nombre;
		this.movil = movil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

}
