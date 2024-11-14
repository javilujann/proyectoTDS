package JuanManuelRuiz_JavierLujan.AppChat;

import java.util.ArrayList;

public class ContactoIndividual extends Contacto{
	private String nombre;
	private String numero;

	public ContactoIndividual(ArrayList<Mensaje> listaMensajes, String nombre, String numero) {
		super(listaMensajes);
		this.nombre = nombre;
		this.numero = numero;
		// TODO Auto-generated constructor stub
	}
	
	

}
