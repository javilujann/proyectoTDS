package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import dominio.Contacto;
import dominio.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;


//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuarioDAO implements UsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioDAO unicaInstancia = null;

	public static AdaptadorUsuarioDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuarioDAO();
		else
			return unicaInstancia;
	}

	private AdaptadorUsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un usuario se le asigna un identificador unico */
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {}
		if (eUsuario != null) return;

		// registrar primero los atributos que son objetos(En este caso es la lista de contactos,pero da igual para el Login)
		/*
		AdaptadorVentaTDS adaptadorVenta = AdaptadorVentaTDS.getUnicaInstancia();
		for (Venta v : Usuario.getVentas())
			adaptadorVenta.registrarVenta(v);
		*/
		
		// crear entidad Usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("Usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("nombre", usuario.getNombre()), new Propiedad("apellidos", usuario.getApellidos()),
						new Propiedad("movil",usuario.getMovil()),new Propiedad("contraseña",usuario.getContraseña()),
						new Propiedad("imagen",usuario.getImagen()),new Propiedad("Premium",String.valueOf(usuario.isPremium())),
						new Propiedad("contactos", obtenerCodigosContactos(usuario.getContactos())) )));

		// registrar entidad Usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		usuario.setCodigo(eUsuario.getId());
	}

	public void borrarUsuario(Usuario usuario) {
		// No se comprueban restricciones de integridad con Contacto //WARNING
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario usuario) {

		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
			    prop.setValor(String.valueOf(usuario.getCodigo()));
			} else if (prop.getNombre().equals("nombre")) {
			    prop.setValor(usuario.getNombre());
			} else if (prop.getNombre().equals("apellidos")) {
			    prop.setValor(usuario.getApellidos());
			} else if (prop.getNombre().equals("movil")) {
			    prop.setValor(usuario.getMovil());
			} else if (prop.getNombre().equals("contraseña")) {
			    prop.setValor(usuario.getContraseña());
			} else if (prop.getNombre().equals("imagen")) {
			    prop.setValor(usuario.getImagen());
			} else if (prop.getNombre().equals("premium")) {
			    prop.setValor(String.valueOf(usuario.isPremium())); // Convertir boolean a String
			} else if (prop.getNombre().equals("contactos")) {
			    String contactos = obtenerCodigosContactos(usuario.getContactos());
			    prop.setValor(contactos);
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Usuario recuperarUsuario(int codigo) {

		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		Entidad eUsuario;
		List<Contacto> contactos = new ArrayList<Contacto>();
		String nombre;
		String apellidos;
		String movil;
		String contraseña;
		String imagen;
		Boolean premium;

		// recuperar entidad
		eUsuario = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
		movil = servPersistencia.recuperarPropiedadEntidad(eUsuario, "movil");
		contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		imagen = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagen");
		premium = Boolean.valueOf(servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre"));

		Usuario Usuario = new Usuario(nombre,apellidos,movil,contraseña,imagen,premium);
		Usuario.setCodigo(codigo);

		// IMPORTANTE:aï¿½adir el Usuario al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, Usuario);

		// recuperar propiedades que son objetos llamando a adaptadores
		// contactos
		contactos = obtenerContactosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "ventas"));

		for (Contacto c : contactos)
			Usuario.addContacto(c);

		return Usuario;
	}

	public List<Usuario> recuperarTodosUsuarios() {

		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("Usuario");
		List<Usuario> Usuarios = new LinkedList<Usuario>();

		for (Entidad eUsuario : eUsuarios) {
			Usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return Usuarios;
	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosContactos(List<Contacto> listaContactos) {
		String aux = "";
		for (Contacto c : listaContactos) {
			aux += c.getCodigo() + " ";
		}
		return aux.trim();
	}

	private List<Contacto> obtenerContactosDesdeCodigos(String contactos) {
		/*
		List<Contacto> listaContactos = new LinkedList<Contacto>();
		StringTokenizer strTok = new StringTokenizer(contactos, " ");
		AdaptadorContactoDAO adaptadorC = AdaptadorContactoDAO.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaContactos.add(adaptadorC.recuperarContacto(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaContactos;
		*/
		return null;
	}
	
		
}

