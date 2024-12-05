package controlador;

import java.time.LocalDateTime;
import java.util.Date;

import dao.AdaptadorMensajeDAO;
import dao.DAOException;
import dao.FactoriaDAO;
import dao.MensajeDAO;
import dao.UsuarioDAO;
import dominio.ContactoIndividual;
import dominio.Mensaje;
import dominio.RepositorioUsuarios;
import dominio.TipoMensaje;
import dominio.Usuario;

public enum Controlador {
	INSTANCE;
	private Usuario usuarioActual;
	private FactoriaDAO factoria;
	
	//CONSTRUCTOR

	private Controlador() {
		usuarioActual = null;
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	//GETTERS AND SETTERS

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}
	
	//METODOS

	//PARA EL LOGIN

	public boolean loginUsuario(String movil, String password) {
		Usuario usuario = RepositorioUsuarios.getUnicaInstancia().getUsuario(movil);
		if (usuario != null && usuario.getContraseña
				().equals(password)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}
	
	//PARA EL REGISTRO
	
	public boolean esUsuarioRegistrado(String movil) {
		return RepositorioUsuarios.getUnicaInstancia().getUsuario(movil) != null;
	}

	public boolean registrarUsuario(String nombre, String apellidos, String movil, String contraseña, String imagen, String bio, Date fechaNacimiento) {

		if (esUsuarioRegistrado(movil))
			return false;
		Usuario usuario = new Usuario(nombre, apellidos, movil, contraseña, imagen, bio, fechaNacimiento);

		UsuarioDAO usuarioDAO = factoria
				.getUsuarioDAO(); /* Adaptador DAO para almacenar el nuevo Usuario en la BD */
		usuarioDAO.registrarUsuario(usuario);

		RepositorioUsuarios.getUnicaInstancia().addUsuario(usuario);
		return true;
	}
	
	//PARA VENTANA PRINCIPAL
		//PANEL IZQUIERDO ---------- ESPERAR A SABER SI ES DE MENSAJES O NO
			//SE NECESITARA ALGO QUE PROPORCIONE LA LISTA DE ULTIMOS MENSJES DEL USUARIO ACTUAL, NO NECESARIAMENTE DE CONTACTOS GUARDADOS
			//SE NECESITARA UN METODO PARA AÑADIR CONTACTO SI ES DE UN NO AGREGADO,ESTO ES MAS GENERAL, EN ESTE CASO SE AUTORELLENA EL TELF
		//PANEL DERECHO
			//SE NECESITARA ALGO QUE FIJADO UN CONACTO DEL USUARIO LE DE TODOS LOS MENSAJES
			//FUNCION PARA ENVIAR MENSAJE A UN CONTACTO
		//appChat.enviarMensaje(c2, "", 2, TipoMensaje.ENVIADO);
	public boolean enviarMensajeIndividual(ContactoIndividual contacto, String textoMensaje, String emoticono ) {
		Mensaje mensaje = new Mensaje(textoMensaje,LocalDateTime.now(),emoticono);
		mensaje.setTipo(TipoMensaje.SENT); 
		
		MensajeDAO AdaptadorMensaje = factoria.getMensajeDAO(); 
		AdaptadorMensaje.registrarMensaje(mensaje);
		
		usuarioActual.getContactos(); //buscar el contacto y enviarle el mensaje; mejor que lo haga el cliente
		Usuario receptor = RepositorioUsuarios.getUnicaInstancia().getUsuario(contacto.getMovil());
		mensaje.setTipo(TipoMensaje.RECEIVED); receptor.getContactos();
		return false;
	}
			//PARA UN GRUPO SE DEBE ACCEDER A EL DESDE ARRIBA; SI NO ES COMPLICADO MOSTRAR TODOS LOS ENVIDOS; CUANDO SE ENVIE
			
	
	//VENTANA CONTACTOS
		//PARA LAS LISTAS MOSTRAR LOS CONTACTOS DEL USUARIO Y LOS DE UN GRUPO
		//FUNCIONES PARA AÑADIR UN CONTACTO Y UN GRUPO A UN USUARIO
		//FUNCIONES PARA AÑADIR Y ELIMINAR CONTACTOS DE UN GRUPO
	
	
}