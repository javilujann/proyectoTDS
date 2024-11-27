package dominio;

import java.util.ArrayList;

public class Grupo extends Contacto{
	private ArrayList<ContactoIndividual> miembros;

	public Grupo(ArrayList<Mensaje> listaMensajes) {
		super();
		this.miembros = new ArrayList<ContactoIndividual>();
		// TODO Auto-generated constructor stub
	}

}
