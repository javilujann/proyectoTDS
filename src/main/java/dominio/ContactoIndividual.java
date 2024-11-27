package dominio;

import java.util.ArrayList;

public class ContactoIndividual extends Contacto{
	private String nombre;
	private String movil;
	private ArrayList<Mensaje> listaMensajes;

	public ContactoIndividual(String nombre, String movil) {
		super();
		this.nombre = nombre;
		this.movil = movil;
		this.listaMensajes = new ArrayList<Mensaje>();
		// TODO Auto-generated constructor stub
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
	
	public ArrayList<Mensaje> getListaMensajes() {
		return listaMensajes;
	}

	public void setListaMensajes(ArrayList<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}
	
	public void addMensaje(Mensaje mensaje) {
		this.listaMensajes.add(mensaje);
	}
	
	
	
	

}
