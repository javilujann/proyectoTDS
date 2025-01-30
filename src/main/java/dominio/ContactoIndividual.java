package dominio;

import java.awt.Image;

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
	
	@Override
    public String[] obtenerDetalles() {
        return new String[]{getNombre(), movil, usuarioAsociado.getBiografia()};
    }

	
	public void enviarMensaje(Mensaje mensaje) {
		//VACIO
	}

	@Override
	public boolean corresponde(String _movil) {
		return this.movil.equals(_movil);
	}
	
	@Override 
	public boolean comparar(String contact) {
		return super.comparar(contact) || contact.equals(movil);
		
	}
	
	public Image getImage() {
		return usuarioAsociado.getImagen();
	}

}
