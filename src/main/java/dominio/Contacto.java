package dominio;

import java.awt.Image;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public boolean isGroup() {
		return false;
	}

	public abstract String[] obtenerDetalles();

	public abstract Image getImage();

	// METODOS

	// True si el movil pasado corresponde al contacto
	public abstract boolean corresponde(String movil);

	public Optional<Mensaje> ultimoMensaje() {
		// Cambiar a -1 y todavia mejor metodo de clase
		return listaMensajes.stream().filter(m -> !m.isEmoticon()) // Filtrar mensajes sin emoticono
				.reduce((primero, segundo) -> segundo); // Mantener solo el último
	}

	public boolean comparar(String contact) {
		return contact.equals(nombre);
	}

	public List<Mensaje> buscarMensajes(String text, TipoMensaje type) {
		return listaMensajes.stream().filter(m -> m.filtro(text, type)).collect(Collectors.toList());
	}
	
	public Mensaje enviarMensaje(String texto, int emoticono,Contacto contacto) {
		Mensaje mensaje = new Mensaje(texto,LocalDateTime.now(),emoticono,contacto);
		mensaje.setTipo(TipoMensaje.SENT);
		listaMensajes.add(mensaje);
		return mensaje;
	}

}
