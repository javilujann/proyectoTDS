package dao;


public class TDSFactoriaDAO extends FactoriaDAO {
	
	public TDSFactoriaDAO () {}
	
	
	//Implementar uno para cada clase persistente que vayas haciendo
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAO.getUnicaInstancia();
	}

}
