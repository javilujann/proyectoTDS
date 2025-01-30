package dominio;

import java.time.LocalDateTime;


public class Mensaje {
	private int codigo;
	private String texto;
	private LocalDateTime hora;
	private int emoticon;			//url de la imagen
	private TipoMensaje tipo;
	private Contacto contacto;
	
	
	public Mensaje(String texto, LocalDateTime hora, int emoticono, Contacto contacto) {
		super();
		this.texto = texto;
		this.hora = hora;
		this.emoticon = emoticono;
		this.contacto = contacto;
	}
	
	
	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public LocalDateTime getHora() {
		return hora;
	}


	public void setHora(LocalDateTime hora) {
		this.hora = hora;
	}


	public int getEmoticon() {
		return emoticon;
	}


	public void setEmoticon(int emoticon) {
		this.emoticon = emoticon;
	}


	public TipoMensaje getTipo() {
		return tipo;
	}


	public void setTipo(TipoMensaje tipo) {
		this.tipo = tipo;
	}
	
	public Contacto getContacto() {
		return contacto;
	}

	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	
	//Como posible expansion aplicar los filtros mediante el patron estrategia
	public boolean filtro(String text, TipoMensaje type) {
		return (text.equals("") || texto.contains(text)) && 
				(type.equals(TipoMensaje.BOTH) || tipo.equals(type));
	}

	
	

}
