package dao;


public class TDSFactoriaDAO extends FactoriaDAO {
	
	public TDSFactoriaDAO () {}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAO.getUnicaInstancia();
	}


	@Override
	public MensajeDAO getMensajeDAO() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public GrupoDAO getGrupoDAO() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ContactoInDAO getContactoIDAO() {
		// TODO Auto-generated method stub
		return null;
	}

}
