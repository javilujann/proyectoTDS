package dominio;

import java.util.ArrayList;

public class Grupo extends Contacto{
	private ArrayList<ContactoIndividual> miembros;

	public Grupo() {
		super();
		this.miembros = new ArrayList<ContactoIndividual>();
	}

	public ArrayList<ContactoIndividual> getMiembros() {
		return miembros;
	}

	public void setMiembros(ArrayList<ContactoIndividual> miembros) {
		this.miembros = miembros;
	}
	
	public void addMiembro(ContactoIndividual miembro) {
		this.miembros.add(miembro);
	}

}
