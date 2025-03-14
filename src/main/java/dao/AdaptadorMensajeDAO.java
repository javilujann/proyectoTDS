package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import dominio.Contacto;
import dominio.Mensaje;
import dominio.TipoMensaje;

public class AdaptadorMensajeDAO implements MensajeDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeDAO unicaInstancia = null;
	
	public static AdaptadorMensajeDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorMensajeDAO();
		return unicaInstancia;
	}

	private AdaptadorMensajeDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		try {
			FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	/* cuando se registra un mensaje se le asigna un identificador unico */
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eMensaje != null)
			return;

		// registrar primero los atributos que son objetos
		AdaptadorContactoInDAO.getUnicaInstancia().registrarContacto(mensaje.getContacto());
		
		// crear entidad Mensaje
		eMensaje = new Entidad();
		eMensaje.setNombre("Mensaje");
		eMensaje.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("texto", mensaje.getTexto()),
				new Propiedad("emoticono", Integer.toString(mensaje.getEmoticon())),
				new Propiedad("tipo", mensaje.getTipo().toString()),
				new Propiedad("hora", mensaje.getHora().toString()),
				new Propiedad("contacto",Integer.toString(mensaje.getContacto().getCodigo())))));

		// registrar entidad Mensaje
		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		mensaje.setCodigo(eMensaje.getId());
	}

	public void borrarMensaje(Mensaje mensaje) {
		// No se comprueban restricciones de integridad con Contacto 
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		servPersistencia.borrarEntidad(eMensaje);
	}

	public void modificarMensaje(Mensaje mensaje) {

		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());

		for (Propiedad prop : eMensaje.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(mensaje.getCodigo()));
			} else if (prop.getNombre().equals("texto")) {
				prop.setValor(mensaje.getTexto());
			} else if (prop.getNombre().equals("hora")) {
				prop.setValor(mensaje.getHora().toString());
			} else if (prop.getNombre().equals("emoticon")) {
				prop.setValor(Integer.toString(mensaje.getEmoticon()));
			} else if (prop.getNombre().equals("tipo")) {
				prop.setValor(mensaje.getTipo().toString());
			} else if (prop.getNombre().equals("contacto")) {
				prop.setValor(Integer.toString(mensaje.getContacto().getCodigo()));
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Mensaje recuperarMensaje(int codigo) {

		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
		//int emoticon = Integer.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje,"emoticon"));
		String preEmote = servPersistencia.recuperarPropiedadEntidad(eMensaje, "emoticono");
		int emoticon = Optional.ofNullable(preEmote)
		                       .filter(v -> !v.isEmpty())
		                       .map(Integer::parseInt)
		                       .orElse(-1);
		
		TipoMensaje tipo = TipoMensaje.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje, "tipo"));
		LocalDateTime hora = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "hora"));
		int codigoContacto = Integer.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje, "contacto"));
		Contacto contacto = AdaptadorContactoInDAO.getUnicaInstancia().recuperarContacto(codigoContacto); //Bidireccion?
		Mensaje mensaje = new Mensaje(texto, hora,emoticon,contacto); 
		mensaje.setCodigo(codigo);
		mensaje.setTipo(tipo);

		return mensaje;
	}

}
