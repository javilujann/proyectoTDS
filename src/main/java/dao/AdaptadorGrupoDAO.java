package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import dominio.ContactoIndividual;
import dominio.Grupo;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupoDAO implements GrupoDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorGrupoDAO unicaInstancia = null;

	public static AdaptadorGrupoDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorGrupoDAO();
		else
			return unicaInstancia;
	}

	private AdaptadorGrupoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un grupo se le asigna un identificador unico */
	public void registrarGrupo(Grupo grupo) {
		Entidad eGrupo = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eGrupo != null)
			return;

		// registrar primero los atributos que son objetos(En este caso es la lista de
		// contactos individuales)
		AdaptadorContactoInDAO adaptadorContacto = AdaptadorContactoInDAO.getUnicaInstancia();
		for (ContactoIndividual c : grupo.getMiembros())
			adaptadorContacto.registrarContacto(c);

		// crear entidad Grupo
		eGrupo = new Entidad();
		eGrupo.setNombre("Grupo");
		eGrupo.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad("miembros", obtenerCodigosMiembros(grupo.getMiembros())))));

		// registrar entidad Grupo
		eGrupo = servPersistencia.registrarEntidad(eGrupo);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		grupo.setCodigo(eGrupo.getId());
	}

	public void borrarGrupo(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		servPersistencia.borrarEntidad(eGrupo);
	}

	public void modificarGrupo(Grupo grupo) {

		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());

		for (Propiedad prop : eGrupo.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(grupo.getCodigo()));
			} else if (prop.getNombre().equals("miembros")) {
				String miembros = obtenerCodigosMiembros(grupo.getMiembros());
				prop.setValor(miembros);
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Grupo recuperarGrupo(int codigo) {
		
		// recuperar entidad
		Entidad eGrupo= servPersistencia.recuperarEntidad(codigo);
		
		if(eGrupo.getNombre() != "Grupo") return null;

		// recuperar propiedades que no son objetos
		Grupo grupo = new Grupo();
		grupo.setCodigo(codigo);


		// recuperar propiedades que son objetos llamando a adaptadores
		List<ContactoIndividual> miembros = new ArrayList<ContactoIndividual>();
		miembros = obtenerMiembrosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, "miembros"));
		for (ContactoIndividual c : miembros)
			grupo.addMiembro(c); // PUEDE SER MEJOR QUE LO HAGA EL PROPIO GRUPO

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
			listaMiembros.add(adaptadorC.recuperarContacto(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaMiembros;
	}

}
