package dominio;

import java.time.LocalDateTime;

public class Mensaje {
	private String texto;
	private LocalDateTime hora;
	private String emoticon;			//url de la imagen
	private TipoMensaje tipo;
	
	
	public Mensaje(String texto, LocalDateTime hora, String emoticon) {
		super();
		this.texto = texto;
		this.hora = hora;
		this.emoticon = emoticon;
	}
	
	

}
