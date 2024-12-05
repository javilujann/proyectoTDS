package controlador;

import java.time.LocalDateTime;
import java.util.Date;

import dao.ContactoInDAO;
import dao.FactoriaDAO;
import dao.MensajeDAO;
import dao.UsuarioDAO;
import dao.DAOException;

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
		if (usuario != null && usuario.chekContraseña(password)) { 
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
					
	//Voy a usar que esta funcion se llama desde la GUI para un contacto que tienes en tu lista, luego este ya es valido
	public void enviarMensajeIndividual(ContactoIndividual contacto, String textoMensaje, String emoticono ) {
		MensajeDAO adaptadorMensaje = factoria.getMensajeDAO();
		ContactoInDAO adaptadorContacto = factoria.getContactoIDAO();
		UsuarioDAO adaptadorUsuario = factoria.getUsuarioDAO();
		
		//Creas el mensaje
		Mensaje mensaje = new Mensaje(textoMensaje,LocalDateTime.now(),emoticono);
		
		//"Enviar" el mensaje para el usuario actual
		mensaje.setTipo(TipoMensaje.SENT); 
		adaptadorMensaje.registrarMensaje(mensaje);
		
		contacto.addMensaje(mensaje);
		adaptadorContacto.modificarContacto(contacto);
		
		//"Recibir" el mensaje para el usuario al que corresponde el contaco
		Usuario receptor = RepositorioUsuarios.getUnicaInstancia().getUsuario(contacto.getMovil());
		mensaje.setTipo(TipoMensaje.RECEIVED);
		adaptadorMensaje.registrarMensaje(mensaje);
		
		receptor.recibirMensaje(usuarioActual.getMovil(),mensaje);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		
		//PERSISTENCIA?
	}
			//PARA UN GRUPO SE DEBE ACCEDER A EL DESDE ARRIBA; SI NO ES COMPLICADO MOSTRAR TODOS LOS ENVIDOS; CUANDO SE ENVIE
			
	
	//VENTANA CONTACTOS
		//PARA LAS LISTAS MOSTRAR LOS CONTACTOS DEL USUARIO Y LOS DE UN GRUPO
		//FUNCIONES PARA AÑADIR UN CONTACTO Y UN GRUPO A UN USUARIO
		//FUNCIONES PARA AÑADIR Y ELIMINAR CONTACTOS DE UN GRUPO
	
	
}