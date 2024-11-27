package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Mensaje;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorContactoInDAO implements ContactoInDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorContactoInDAO unicaInstancia = null;

	public static AdaptadorContactoInDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorContactoInDAO();
		else
			return unicaInstancia;
	}

	private AdaptadorContactoInDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un contacto se le asigna un identificador unico */
	public void registrarContacto(ContactoIndividual contacto) {
		Entidad eContacto = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eContacto != null)
			return;

		// registrar primero los atributos que son objetos(En este caso es la lista de
		// mensajes)
		AdaptadorMensajeDAO adaptadorMensaje = AdaptadorMensajeDAO.getUnicaInstancia();
		for (Mensaje m : contacto.getListaMensajes())
			adaptadorMensaje.registrarMensaje(m);

		// crear entidad Contacto
		eContacto = new Entidad();
		eContacto.setNombre("Contacto");
		eContacto.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", contacto.getNombre()),
				new Propiedad("movil", contacto.getMovil()),
				new Propiedad("listaMensajes", obtenerCodigosMensajes(contacto.getListaMensajes())))));

		// registrar entidad Contacto
		eContacto = servPersistencia.registrarEntidad(eContacto);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		contacto.setCodigo(eContacto.getId());
	}

	public void borrarContacto(ContactoIndividual contacto) {
		// No se comprueban restricciones de integridad con Mensaje //WARNING
		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		servPersistencia.borrarEntidad(eContacto);
	}

	public void modificarContacto(ContactoIndividual contacto) {

		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());

		for (Propiedad prop : eContacto.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(contacto.getCodigo()));
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(contacto.getNombre());
			} else if (prop.getNombre().equals("movil")) {
				prop.setValor(contacto.getMovil());
			} else if (prop.getNombre().equals("listaMensajes")) {
				String mensajes = obtenerCodigosMensajes(contacto.getListaMensajes());
				prop.setValor(mensajes);
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	public ContactoIndividual recuperarContacto(int codigo) {

		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eContacto = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
		String movil = servPersistencia.recuperarPropiedadEntidad(eContacto, "movil");
		ContactoIndividual contacto = new ContactoIndividual(nombre, movil);
		contacto.setCodigo(codigo);

		// IMPORTANTE:añadir el Contacto al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, contacto);

		// recuperar propiedades que son objetos llamando a adaptadores
		// contactos
		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		mensajes = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eContacto, "listaMensajes"));
		for (Mensaje m : mensajes)
			contacto.addMensaje(m); // PUEDE SER MEJOR QUE LO HAGA EL PROPIO USUARIO

		return contacto;
	}

	public List<Contacto> recuperarTodosContactos() {
		return servPersistencia.recuperarEntidades("Contacto").stream()
				.map(entidad -> recuperarContacto(entidad.getId())).collect(Collectors.toList());

	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosMensajes(List<Mensaje> listaMensajes) {
		String aux = "";
		for (Mensaje m : listaMensajes) {
			aux += m.getCodigo() + " ";
		}
		return aux.trim();
	}

	private List<Mensaje> obtenerMensajesDesdeCodigos(String mensajes) {
		List<Mensaje> listaMensajes = new ArrayList<Mensaje>();
		StringTokenizer strTok = new StringTokenizer(mensajes, " ");
		AdaptadorMensajeDAO adaptadorM = AdaptadorMensajeDAO.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaMensajes.add(adaptadorM.recuperarMensaje(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaMensajes;
	}

}
