package dominio;

import java.util.ArrayList;

public class Usuario {
	private String nombre;
	private String apellidos;
	private String movil;
	private String contraseña;
	private String imagen;				//url de la imagen usada//
	private Boolean isPremium;
	private ArrayList<Contacto> listaContactos;
	private String username;
	
	
	public Usuario(String nombre, String apellidos, String movil, String contraseña, String imagen, String username) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.movil = movil;
		this.contraseña = contraseña;
		this.imagen = imagen;
		this.username = username;
	}

	public String getUsername() {
		String log = this.username;
		return log;
	}

	public String getContraseña() {
		String psswd = this.contraseña;
		return psswd;
	}

	
}
