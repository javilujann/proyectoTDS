package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;
import dominio.Mensaje;
import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupoDAO implements ContactoDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorGrupoDAO unicaInstancia = null;

	public static AdaptadorGrupoDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorGrupoDAO();
		return unicaInstancia;
	}

	private AdaptadorGrupoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	@Override
	public void registrarContacto(Contacto _grupo) {
		Entidad eGrupo = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eGrupo = servPersistencia.recuperarEntidad(_grupo.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eGrupo != null)
			return;

		Grupo grupo = (Grupo) _grupo;

		// registrar primero los atributos que son objetos(En este caso es la lista de
		// contactos individuales y la lista de los mensajes(Vacia))
		AdaptadorContactoInDAO adaptadorContacto = AdaptadorContactoInDAO.getUnicaInstancia();
		for (ContactoIndividual c : grupo.getMiembros())
			adaptadorContacto.registrarContacto(c);

		// crear entidad Grupo
		eGrupo = new Entidad();
		eGrupo.setNombre("Grupo");
		eGrupo.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", grupo.getNombre()),
				new Propiedad("miembros", obtenerCodigosMiembros(grupo.getMiembros())),
				new Propiedad("listaMensajes", obtenerCodigosMensajes(grupo.getListaMensajes())))));

		// registrar entidad Grupo
		eGrupo = servPersistencia.registrarEntidad(eGrupo);

		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		grupo.setCodigo(eGrupo.getId());
	}

	@Override
	public void borrarContacto(Contacto grupo) {
		// No se comprueban restricciones de integridad con Contacto y Mensaje
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		servPersistencia.borrarEntidad(eGrupo);
	}

	@Override
	public void modificarContacto(Contacto _grupo) {

		Entidad eGrupo = servPersistencia.recuperarEntidad(_grupo.getCodigo());

		Grupo grupo = (Grupo) _grupo;

		for (Propiedad prop : eGrupo.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(grupo.getCodigo()));
			} else if (prop.getNombre().equals("miembros")) {
				String miembros = obtenerCodigosMiembros(grupo.getMiembros());
				prop.setValor(miembros);
			} else if (prop.getNombre().equals("listaMensajes")) {
				String mensajes = obtenerCodigosMensajes(grupo.getListaMensajes());
				prop.setValor(mensajes);
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(grupo.getNombre());
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Contacto recuperarContacto(int codigo) {

		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			Object objeto = PoolDAO.getUnicaInstancia().getObjeto(codigo);
			if (objeto instanceof Grupo)
				return (Grupo) objeto;
			else
				return null;
		}

		// recuperar entidad
		Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);

		if (!eGrupo.getNombre().equals("Grupo"))
			return null; // En caso de que lo llamen para contactoIndividual

		// recuperar propiedades que no son objetos
		String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, "nombre");
		Grupo grupo = new Grupo(nombre);
		grupo.setCodigo(codigo);

		// IMPORTANTE:añadir el Grupo al pool antes de llamar a otros adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, grupo);

		// recuperar propiedades que son objetos llamando a adaptadores
		List<ContactoIndividual> miembros = new ArrayList<ContactoIndividual>();
		miembros = obtenerMiembrosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, "miembros"));
		for (ContactoIndividual c : miembros)
			grupo.addMiembro(c);

		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		mensajes = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, "listaMensajes"));
		for (Mensaje m : mensajes)
			grupo.addMensaje(m);

		return grupo;
	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosMiembros(List<ContactoIndividual> miembros) {
		String aux = "";
		for (ContactoIndividual c : miembros) {
			aux += c.getCodigo() + " ";
		}
		return aux.trim();
	}

	private List<ContactoIndividual> obtenerMiembrosDesdeCodigos(String miembros) {
		List<ContactoIndividual> listaMiembros = new ArrayList<ContactoIndividual>();
		StringTokenizer strTok = new StringTokenizer(miembros, " ");
		AdaptadorContactoInDAO adaptadorC = AdaptadorContactoInDAO.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaMiembros.add(
					(ContactoIndividual) adaptadorC.recuperarContacto(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaMiembros;
	}

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
