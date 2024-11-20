package dominio;

import java.util.ArrayList;

public abstract class Contacto {
	private ArrayList<Mensaje> listaMensajes;

	public Contacto(ArrayList<Mensaje> listaMensajes) {
		super();
		this.listaMensajes = listaMensajes;
	}
	

}
