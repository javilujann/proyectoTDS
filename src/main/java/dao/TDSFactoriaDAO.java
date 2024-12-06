package dao;

import java.util.HashMap;

import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;

public class TDSFactoriaDAO extends FactoriaDAO {
	
	 private HashMap<Class<? extends Contacto>, ContactoDAO> mapaAdaptadores;
	
	public TDSFactoriaDAO () {
		mapaAdaptadores = new HashMap<Class<? extends Contacto>, ContactoDAO>();
        mapaAdaptadores.put(Grupo.class, AdaptadorGrupoDAO.getUnicaInstancia());
        mapaAdaptadores.put(ContactoIndividual.class, AdaptadorContactoInDAO.getUnicaInstancia());
		
	}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAO.getUnicaInstancia();
	}


	@Override
	public MensajeDAO getMensajeDAO() {
		return AdaptadorMensajeDAO.getUnicaInstancia();
	}

	@Override
    public ContactoDAO getContactoDAO(Class<? extends Contacto> clase) {
    	return mapaAdaptadores.get(clase);
    }

}
