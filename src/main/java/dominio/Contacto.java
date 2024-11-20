package dominio;

import java.util.ArrayList;

public abstract class Contacto {
	
	private int codigo;
	private ArrayList<Mensaje> listaMensajes;

	public Contacto(ArrayList<Mensaje> listaMensajes) {
		super();
		this.listaMensajes = listaMensajes;
		this.codigo = 0;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public ArrayList<Mensaje> getListaMensajes() {
		return listaMensajes;
	}


	public void setListaMensajes(ArrayList<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}

	

}
