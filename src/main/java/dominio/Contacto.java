package dominio;

import java.util.ArrayList;

public abstract class Contacto {
	
	private int codigo;
	private ArrayList<Mensaje> listaMensajes;

	public Contacto() {
		super();
		this.listaMensajes = new ArrayList<Mensaje>();
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
	
	public void addMensaje(Mensaje mensaje) {
		this.listaMensajes.add(mensaje);
	}

	

}
