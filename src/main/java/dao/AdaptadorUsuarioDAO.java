package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.Contacto;
import dominio.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuarioDAO implements UsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private FactoriaDAO factoria;

	private SimpleDateFormat dateFormat; // para formatear la fecha de nacimiento en la base de datos
	private static AdaptadorUsuarioDAO unicaInstancia = null;

	public static AdaptadorUsuarioDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuarioDAO();
		return unicaInstancia;
	}

	private AdaptadorUsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	/* cuando se registra un usuario se le asigna un identificador unico */
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eUsuario != null)
			return;

		// registrar primero los atributos que son objetos(En este caso es la lista de
		// contactos= grupos + individuales)
		for (Contacto c : usuario.getContactos()) {
			factoria.getContactoDAO(c.getClass()).registrarContacto(c);
		}

		// crear entidad Usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("Usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", usuario.getNombre()),
				new Propiedad("apellidos", usuario.getApellidos()), new Propiedad("movil", usuario.getMovil()),
				new Propiedad("contraseña", usuario.getContraseña()), new Propiedad("imagen", usuario.getURL()),
				new Propiedad("Premium", String.valueOf(usuario.isPremium())),
				new Propiedad("contactos", obtenerCodigosContactos(usuario.getContactos())),
				new Propiedad("biografia", usuario.getBiografia()),
				new Propiedad("fechaNacimiento", dateFormat.format(usuario.getFechaNacimiento())))));

		// registrar entidad Usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);

		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		usuario.setCodigo(eUsuario.getId());
	}

	public void borrarUsuario(Usuario usuario) {
		// No se comprueban restricciones de integridad con Contacto
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
				prop.setValor(usuario.getURL());
			} else if (prop.getNombre().equals("premium")) {
				prop.setValor(String.valueOf(usuario.isPremium())); // Convertir boolean a String
			} else if (prop.getNombre().equals("contactos")) {
				String contactos = obtenerCodigosContactos(usuario.getContactos());
				prop.setValor(contactos);
			} else if (prop.getNombre().equals("biografia")) {
				prop.setValor(usuario.getBiografia());
			} else if (prop.getNombre().equals("fechaNacimiento")) {
				prop.setValor(dateFormat.format(usuario.getFechaNacimiento()));
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Usuario recuperarUsuario(int codigo) {

		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
		String movil = servPersistencia.recuperarPropiedadEntidad(eUsuario, "movil");
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		String imagen = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagen");
		Boolean premium = Boolean.valueOf(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));
		String biografia = servPersistencia.recuperarPropiedadEntidad(eUsuario, "biografia");
		Date fechaNacimiento = null;
		try {
			String fecha = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento");
			fechaNacimiento = dateFormat.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Usuario usuario = new Usuario(nombre, apellidos, movil, contraseña, imagen, biografia, fechaNacimiento);
		usuario.setCodigo(codigo);
		usuario.setPremium(premium);

		// IMPORTANTE:añadir el Usuario al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		// recuperar propiedades que son objetos llamando a adaptadores
		// contactos
		List<Contacto> contactos = new ArrayList<Contacto>();
		contactos = obtenerContactosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos"));
		for (Contacto c : contactos)
			usuario.addContacto(c);

		return usuario;
	}

	public List<Usuario> recuperarTodosUsuarios() {
		return servPersistencia.recuperarEntidades("Usuario").stream().map(entidad -> recuperarUsuario(entidad.getId()))
				.collect(Collectors.toList());

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

		List<Contacto> listaContactos = new ArrayList<Contacto>();
		StringTokenizer strTok = new StringTokenizer(contactos, " ");
		AdaptadorContactoInDAO adaptadorC = AdaptadorContactoInDAO.getUnicaInstancia();
		AdaptadorGrupoDAO adaptadorG = AdaptadorGrupoDAO.getUnicaInstancia();

		while (strTok.hasMoreTokens()) {
			int codigo = Integer.valueOf((String) strTok.nextElement());
			if (codigo != 0) {
				Contacto contacto = adaptadorG.recuperarContacto(codigo);

				if (contacto != null)
					listaContactos.add(contacto); // Era grupo
				else
					listaContactos.add(adaptadorC.recuperarContacto(codigo)); // Es Individual
			}
		}

		return listaContactos;
	}

}
