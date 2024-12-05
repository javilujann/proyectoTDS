package dominio;

import java.util.ArrayList;

public class Grupo extends Contacto{
	private ArrayList<ContactoIndividual> miembros;

	public Grupo(String nombre) {
		super(nombre);
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
	
	public boolean corresponde(String movil) {
		return false; //Los grupos no manejan moviles
	}

}
