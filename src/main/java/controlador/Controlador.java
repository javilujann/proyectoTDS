package controlador;

import java.awt.Image;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import dao.ContactoDAO;
import dao.FactoriaDAO;
import dao.MensajeDAO;
import dao.UsuarioDAO;
import dao.DAOException;

import dominio.ContactoIndividual;
import dominio.Descuento;
import dominio.Grupo;
import dominio.Contacto;
import dominio.Mensaje;
import dominio.RepositorioUsuarios;
import dominio.TipoMensaje;
import dominio.Usuario;

public enum Controlador {
	INSTANCE;

	private static final float PRECIO_BASE = 250;
	private static final float PORCENTAJE_DESCUENTO = 20;

	private Usuario usuarioActual;
	private FactoriaDAO factoria;

	// CONSTRUCTOR

	private Controlador() {
		usuarioActual = null;
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// GETTERS AND SETTERS

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public List<Contacto> getContactos() {
		return usuarioActual.getContactos();
	}

	public List<Grupo> getGrupos() {
		return usuarioActual.getGrupos();
	}

	public Image getImage() {
		return usuarioActual.getImagen();
	}

	public float getPrecioPremium() {
		return usuarioActual.getDescuento().map(d -> d.aplicarDescuento(PRECIO_BASE, PORCENTAJE_DESCUENTO))
				.orElse(PRECIO_BASE);
	}
	
		//Hipoteticamente se llamaria desde alguna ventana por parte de administradores
	public void setDescuento(Supplier<Descuento> supplier) {
		usuarioActual.setDescuento(supplier.get());
	}

	// METODOS

	// PARA EL LOGIN

	public boolean loginUsuario(String movil, String password) {
		Usuario usuario = RepositorioUsuarios.getUnicaInstancia().getUsuario(movil);
		if (usuario != null && usuario.chekContraseña(password)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}

	// PARA EL REGISTRO
	public boolean esUsuarioRegistrado(String movil) {
		return RepositorioUsuarios.getUnicaInstancia().getUsuario(movil) != null;
	}

	public boolean registrarUsuario(String nombre, String apellidos, String movil, String contraseña, String imagen,
			String bio, Date fechaNacimiento) {

		if (esUsuarioRegistrado(movil))
			return false;
		Usuario usuario = new Usuario(nombre, apellidos, movil, contraseña, imagen, bio, fechaNacimiento);

		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO();
		usuarioDAO.registrarUsuario(usuario);

		RepositorioUsuarios.getUnicaInstancia().addUsuario(usuario);
		return true;
	}

	// PARA VENTANA PRINCIPAL
	// PANEL SUPERIOR
	public int añadirContacto(String nombre, String telefono) {
		Usuario asociado = RepositorioUsuarios.getUnicaInstancia().getUsuario(telefono);
		if (asociado == null)
			return -1;

		return usuarioActual.nuevoContactoIn(nombre, telefono, asociado);
	}

	public Grupo añadirGrupo(String nombre) {
		return usuarioActual.nuevoGrupo(nombre);
	}

	// PANEL IZQUIERDO ---------- ESPERAR A SABER SI ES DE MENSAJES O NO

	// SE NECESITARA UN METODO PARA AÑADIR CONTACTO, esta addContacto, quien lo crea
	// y maneja persistencias?

	// EN ESTE CASO ES PASAR DE UN NO_AGREGADO A AGREGADO, EL METODO SERA SOLO
	// MODIFICAR NOMBRE Y MANEJAR PERSISTENCIA

	// PANEL DERECHO
	// SE NECESITARA ALGO QUE FIJADO UN CONACTO DEL USUARIO LE DE TODOS LOS
	// MENSAJES,
	// REALMENTE SI TIENES EL CONTACTO YA TIENES LOS MENSAJES
	// ESPERAR A VER QUE ES LO QUE LLEGA REALMENTE

	// FUNCION PARA ENVIAR MENSAJE A UN CONTACTO
	// Voy a usar que esta funcion se llama desde la GUI para un contacto que tienes
	// en tu lista, luego este ya es valido

	public void enviarMensajeIndividual(ContactoIndividual contacto, String textoMensaje, String emoticono) {
		MensajeDAO adaptadorMensaje = factoria.getMensajeDAO();
		ContactoDAO adaptadorContacto = factoria.getContactoDAO(contacto.getClass());
		UsuarioDAO adaptadorUsuario = factoria.getUsuarioDAO();

		// Creas el mensaje
		Mensaje mensaje = new Mensaje(textoMensaje, LocalDateTime.now(), emoticono, contacto);

		// "Enviar" el mensaje para el contacto del usuario actual
		mensaje.setTipo(TipoMensaje.SENT);
		adaptadorMensaje.registrarMensaje(mensaje);
		contacto.addMensaje(mensaje);
		adaptadorContacto.modificarContacto(contacto);

		// "Recibir" el mensaje para el usuario al que corresponde el contaco
		Usuario receptor = RepositorioUsuarios.getUnicaInstancia().getUsuario(contacto.getMovil());
		mensaje.setTipo(TipoMensaje.RECEIVED);
		adaptadorMensaje.registrarMensaje(mensaje);

		receptor.recibirMensaje(usuarioActual.getMovil(), mensaje);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	// POSIBLE REFACTORING HACER ContactoDAO como interfaz unica, y que ambos
	// adaptadores hereden de hay
	// Menos problema de tipos aun asi hay dos adaptadores eso todavia no se como
	// resolverlo
	// posible solucion que contactoDAO reciba un String tipo que sea la clase

	// EL refactoring la parte primera la hace igual, la segunda he pensado en meter
	// el metodo en contacto
	// las dos posibles especificaciones, son en Grupo llamar a cada miembro y en
	// Individual hacer la logica
	// Para evitar duplicidad de mensajes pasar ya el mensaje guardado

	// Para empezar puedo hacer dos funciones una que reciba ya el mensaje, y evitas
	// toda la persistencia
	// Y la general gestiona esta y llama luego a la otra

	public void enviarMensajeGrupo(Grupo grupo, String textoMensaje, String emoticono) {
		MensajeDAO adaptadorMensaje = factoria.getMensajeDAO();
		ContactoDAO adaptadorGrupo = factoria.getContactoDAO(grupo.getClass());

		// Creas el mensaje
		Mensaje mensaje = new Mensaje(textoMensaje, LocalDateTime.now(), emoticono, grupo);

		// "Enviar" el mensaje para el grupo del usuario actual
		mensaje.setTipo(TipoMensaje.SENT);
		adaptadorMensaje.registrarMensaje(mensaje);
		grupo.addMensaje(mensaje);
		adaptadorGrupo.modificarContacto(grupo);

		// "Enviar y Recibir" el mensaje para cada usuario al que corresponde un miembro
		for (ContactoIndividual contacto : grupo.getMiembros()) {
			enviarMensajeIndividual(contacto, textoMensaje, emoticono);
			// ESTO ESTA CREANDO UNA COPIA DE MENSAJE ENVIADO Y RECIVIDO PARA CADA MIEMBRO
			// VAMOS QUE ESTA FATAL
		}

	}

	public void bajaPremium() {
		usuarioActual.bajaPremium();
	}

	public void altaPremium() {
		usuarioActual.altaPremium();
	}

	public List<Mensaje> buscarMensajes(String contact, String text, TipoMensaje type) {
		return usuarioActual.buscarMensajes(contact, text, type);
	}
	
	

	// He pensado en bajar toda la persistencia a dominio, no se si es posible, pero
	// en su caso limpia mucho esta capa/clase
}