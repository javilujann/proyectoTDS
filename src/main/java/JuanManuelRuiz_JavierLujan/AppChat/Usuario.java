package JuanManuelRuiz_JavierLujan.AppChat;

import java.util.ArrayList;

public class Usuario {
	private String nombre;
	private String movil;
	private String contraseña;
	private String imagen;				//url de la imagen usada//
	private Boolean isPremium;
	private ArrayList<Contacto> listaContactos;
	
	
	public Usuario(String nombre, String movil, String contraseña, String imagen, Boolean isPremium) {
		super();
		this.nombre = nombre;
		this.movil = movil;
		this.contraseña = contraseña;
		this.imagen = imagen;
		this.isPremium = isPremium;
	}

	
}
