package dominio;

import java.awt.Image;
import java.time.LocalDateTime;

public class ContactoIndividual extends Contacto{

	private String movil;
	private Boolean agregado;
	private Usuario usuarioAsociado;

	public ContactoIndividual(String nombre, String movil, Usuario user) {
		super(nombre);
		this.movil = movil;
		this.agregado = true;
		this.usuarioAsociado = user;
	}

	//Getters y Setters
	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public Boolean isAgregado() {
		return agregado;
	}

	public void setAgregado(Boolean agreagado) {
		this.agregado = agreagado;
	}
	
	public Usuario getUsuario() {
		return usuarioAsociado;
	}
	
	public void setUsuario(Usuario user) {
		usuarioAsociado = user;
	}
	
	//Implementacion de herenecia 
	
	@Override
    public String[] obtenerDetalles() {
        return new String[]{getNombre(), movil, usuarioAsociado.getBiografia()};
    }
	
	@Override
	public Image getImage() {
		return usuarioAsociado.getImagen();
	}

	@Override
	public boolean corresponde(String _movil) {
		return this.movil.equals(_movil);
	}
	
	//Metodos Normales
	
	@Override 
	public boolean comparar(String contact) {
		return super.comparar(contact) || contact.equals(movil);
		
	}
	
	public Mensaje recibirMensaje(String texto, int emoticono, Contacto contacto) {
        Mensaje mensaje = new Mensaje(texto,LocalDateTime.now(),emoticono,contacto);
        mensaje.setTipo(TipoMensaje.RECEIVED);
        super.addMensaje(mensaje);
        return mensaje;
    }
	
	public void agregar(String nombre) {
		this.setNombre(nombre);
		agregado = true;
	}


}
