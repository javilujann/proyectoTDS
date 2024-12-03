package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import dominio.Mensaje;
import dominio.TipoMensaje;

public class AdaptadorMensajeDAO {
	
	private static ServicioPersistencia servPersistencia;
	private SimpleDateFormat dateFormat; // para formatear la hora en la base de datos
	private static AdaptadorMensajeDAO unicaInstancia = null;

	public static AdaptadorMensajeDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorMensajeDAO();
		else
			return unicaInstancia;
	}

	private AdaptadorMensajeDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		dateFormat = new SimpleDateFormat("HH:mm:ss"); 
	}

	/* cuando se registra un mensaje se le asigna un identificador unico */
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		} catch (NullPointerException e) {}
		if (eMensaje != null) return;

		// registrar primero los atributos que son objetos, Mensaje no tiene es el mas primitivo
		
		// crear entidad Mensaje
		eMensaje = new Entidad();
		eMensaje.setNombre("Mensaje");
		eMensaje.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("texto", mensaje.getTexto()), new Propiedad("emoticon", mensaje.getEmoticon()),
						new Propiedad("tipo",mensaje.getTipo().toString()), new Propiedad("hora",dateFormat.format(mensaje.getHora())) )));

		// registrar entidad Mensaje
		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		mensaje.setCodigo(eMensaje.getId());
	}

	public void borrarMensaje(Mensaje mensaje) {
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
			    prop.setValor(dateFormat.format(mensaje.getHora()));
			} else if (prop.getNombre().equals("emoticon")) {
			    prop.setValor(mensaje.getEmoticon());
			} else if (prop.getNombre().equals("tipo")) {
			    prop.setValor(mensaje.getTipo().toString());
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Mensaje recuperarMensaje(int codigo) {

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
		String emoticon = servPersistencia.recuperarPropiedadEntidad(eMensaje, "emoticon");
		TipoMensaje tipo = TipoMensaje.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje, "tipo")) ;
		Date hora = null;
		try {
			hora = dateFormat.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "hora"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Mensaje mensaje = new Mensaje(texto,hora,emoticon);
		mensaje.setCodigo(codigo);
		mensaje.setTipo(tipo);
		
		return mensaje;
	}

}
