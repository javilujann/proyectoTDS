package dominio;

import java.util.Date;

public class Mensaje {
	private int codigo;
	private String texto;
	private Date hora;
	private String emoticon;			//url de la imagen
	private TipoMensaje tipo;
	
	
	public Mensaje(String texto, Date hora, String emoticon) {
		super();
		this.texto = texto;
		this.hora = hora;
		this.emoticon = emoticon;
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


	public Date getHora() {
		return hora;
	}


	public void setHora(Date hora) {
		this.hora = hora;
	}


	public String getEmoticon() {
		return emoticon;
	}


	public void setEmoticon(String emoticon) {
		this.emoticon = emoticon;
	}


	public TipoMensaje getTipo() {
		return tipo;
	}


	public void setTipo(TipoMensaje tipo) {
		this.tipo = tipo;
	}
	

}
