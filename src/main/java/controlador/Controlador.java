package controlador;

import java.awt.Image;
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
import dominio.IAdaptadorPDF;
import dominio.ItextAdapter;
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

	public List<Grupo> getGrupos() {
		return usuarioActual.getGrupos();
	}

	public Image getImage() {
		return usuarioActual.getImagen();
	}
	
	public List<Contacto> getContactos() {
		return usuarioActual.getContactos();
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
	public List<Mensaje> buscarMensajes(String contact, String text, TipoMensaje type) {
		return usuarioActual.buscarMensajes(contact, text, type);
	}
	
	public int añadirContacto(String nombre, String telefono) {
		Usuario asociado = RepositorioUsuarios.getUnicaInstancia().getUsuario(telefono);
		if (asociado == null)
			return -1;

		Contacto nuevo = usuarioActual.nuevoContactoIn(nombre, telefono, asociado);
		if(nuevo == null) return -2;
		
		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO();
		ContactoDAO contactoDAO = factoria.getContactoDAO(nuevo.getClass());
		
		contactoDAO.registrarContacto(nuevo);
		usuarioDAO.modificarUsuario(usuarioActual);
		
		return 0;
	}

	public Grupo añadirGrupo(String nombre) {
		Grupo nuevo = usuarioActual.nuevoGrupo(nombre);
		if(nuevo == null) return null;
		
		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO();
		ContactoDAO contactoDAO = factoria.getContactoDAO(nuevo.getClass());
		
		contactoDAO.registrarContacto(nuevo);
		usuarioDAO.modificarUsuario(usuarioActual);
		
		return nuevo;
	}
	
	public void modificarGrupo(Grupo grupoModificar,List<ContactoIndividual> nuevosMiembros){
		grupoModificar.setMiembros(nuevosMiembros);
		
		ContactoDAO contactoDAO = factoria.getContactoDAO(grupoModificar.getClass());
		contactoDAO.modificarContacto(grupoModificar);
	}
	
	public void altaPremium() {
		usuarioActual.altaPremium();
	}
	
	public void bajaPremium() {
		usuarioActual.bajaPremium();
	}
	
	public void crearPDF(Contacto contacto) {
		 IAdaptadorPDF exportador = new ItextAdapter(); //Usar factoria para crearlo
		 try {
	            exportador.exportarMensajes(usuarioActual, contacto);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error al exportar el PDF.");
	        }
	}

	// PANEL IZQUIERDO 
	public void agregarContacto(ContactoIndividual contacto, String nombre) {
		contacto.setNombre(nombre);
		contacto.setAgregado(true); //Hacer metodo en el contacto
		
		ContactoDAO contactoDAO = factoria.getContactoDAO(contacto.getClass());
		contactoDAO.modificarContacto(contacto);
	}

	// PANEL DERECHO
	public void enviarYrecibirMensaje(Contacto contacto, String texto, int emoticono) {
/*<<<<<<< HEAD
=======*/
		enviarMensaje(contacto,texto,emoticono);
		recibirMensaje(contacto,texto,emoticono);
	}
	
	/*private void enviarMensaje(Contacto contacto, String texto, int emoticono) {
//>>>>>>> branch 'main' of https://gitlab.com/tds1341744/appchat_tds.git
		 if (contacto == null) {
		        System.err.println("Error: El contacto es null. No se puede enviar el mensaje.");
		        return;
		    }
		enviarMensaje(contacto,texto,emoticono);
		recibirMensaje(contacto,texto,emoticono);
	}*/
	
	private void enviarMensaje(Contacto contacto, String texto, int emoticono) {
		 
		Mensaje mensaje = contacto.enviarMensaje(texto,emoticono,contacto);
		
		MensajeDAO mensajeDAO = factoria.getMensajeDAO();
		mensajeDAO.registrarMensaje(mensaje);
		
		ContactoDAO contactoDAO = factoria.getContactoDAO(contacto.getClass());
		contactoDAO.modificarContacto(contacto);
		
		if(contacto.isGroup()) {
			for(ContactoIndividual c : ((Grupo)contacto).getMiembros()) {
				enviarMensaje(c,texto,emoticono);
			}
		}
	}
	
	private void recibirMensaje(Contacto contacto, String texto, int emoticono) {
/*<<<<<<< HEAD
		  if(contacto.isGroup()) {
	            for(ContactoIndividual c : ((Grupo)contacto).getMiembros()) {
	                recibirMensaje(c,texto,emoticono);
	            }
	            return; //Grupos no reciben mensajes
	        }

	        ContactoIndividual contactoIn = (ContactoIndividual) contacto;

	        Usuario receptor = RepositorioUsuarios.getUnicaInstancia().getUsuario(contactoIn.getMovil());
	        ContactoIndividual asociado = receptor.recibirMensaje(usuarioActual);

	        if(asociado.getCodigo() == 0) {
	            factoria.getContactoDAO(asociado.getClass()).registrarContacto(asociado);
	            factoria.getUsuarioDAO().modificarUsuario(receptor);
	        }

	        Mensaje mensaje = asociado.recibirMensaje(texto,emoticono,contacto);

	        MensajeDAO mensajeDAO = factoria.getMensajeDAO();
	        mensajeDAO.registrarMensaje(mensaje);

	        ContactoDAO contactoDAO = factoria.getContactoDAO(asociado.getClass());
	        contactoDAO.modificarContacto(asociado);	
=======*/
		
		if(contacto.isGroup()) {
			for(ContactoIndividual c : ((Grupo)contacto).getMiembros()) {
				recibirMensaje(c,texto,emoticono);
			}
			return; //Grupos no reciben mensajes
		}
		
		ContactoIndividual contactoIn = (ContactoIndividual) contacto;
		
		Usuario receptor = RepositorioUsuarios.getUnicaInstancia().getUsuario(contactoIn.getMovil());
		ContactoIndividual asociado = receptor.recibirMensaje(usuarioActual);
		
		if(asociado.getCodigo() == 0) {
			factoria.getContactoDAO(asociado.getClass()).registrarContacto(asociado);
			factoria.getUsuarioDAO().modificarUsuario(receptor);
		}
		
		Mensaje mensaje = asociado.recibirMensaje(texto,emoticono,contacto);
		
		MensajeDAO mensajeDAO = factoria.getMensajeDAO();
		mensajeDAO.registrarMensaje(mensaje);
		
		ContactoDAO contactoDAO = factoria.getContactoDAO(asociado.getClass());
		contactoDAO.modificarContacto(asociado);
//>>>>>>> branch 'main' of https://gitlab.com/tds1341744/appchat_tds.git
	}

}