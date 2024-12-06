package dominio;

import java.util.ArrayList;
import java.util.Optional;

public abstract class Contacto {
	
	private int codigo;
	private String nombre;
	private ArrayList<Mensaje> listaMensajes;

	public Contacto(String nombre) {
		super();
		this.nombre = nombre;
		this.codigo = 0;
		this.listaMensajes = new ArrayList<Mensaje>();
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	
	//METODOS
	
	//True si el movil pasado corresponde al contacto
	public abstract boolean corresponde(String movil);
	
	public Optional<Mensaje> ultimoMensaje() {
		if(listaMensajes.isEmpty()) return  Optional.empty(); 
		Mensaje mensaje =  listaMensajes.get(listaMensajes.size() - 1); //accedemos al ultimo indice
		return  Optional.of(mensaje);
	}
	
}
