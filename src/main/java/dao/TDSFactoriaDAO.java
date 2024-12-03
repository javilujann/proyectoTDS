package dao;


public class TDSFactoriaDAO extends FactoriaDAO {
	
	public TDSFactoriaDAO () {}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAO.getUnicaInstancia();
	}


	@Override
	public MensajeDAO getMensajeDAO() {
		return AdaptadorMensajeDAO.getUnicaInstancia();
	}


	@Override
	public GrupoDAO getGrupoDAO() {
		return AdaptadorGrupoDAO.getUnicaInstancia();
	}


	@Override
	public ContactoInDAO getContactoIDAO() {
		return AdaptadorContactoInDAO.getUnicaInstancia();
	}

}
