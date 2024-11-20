package dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DAOException;
import dao.FactoriaDAO;
import dao.UsuarioDAO;

public class RepositorioUsuarios {
	
	private Map<String,Usuario> usuariosRegistrados;
	
	private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();
	private FactoriaDAO factoria;
	private UsuarioDAO daoUsuario;
	
	private RepositorioUsuarios() {
		try {
  			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
  			daoUsuario = factoria.getUsuarioDAO();
  			usuariosRegistrados = new HashMap<String,Usuario>();
  			this.cargarRepositorio();
  		} catch (DAOException eDAO) {
  			eDAO.printStackTrace();
  		}
	}
	
	public static RepositorioUsuarios getUnicaInstancia(){
		return unicaInstancia;
	}
	
	/*devuelve todos los usuarios*/
	public List<Usuario> getUsuarios(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario u: usuariosRegistrados.values()) 
			lista.add(u);
		return lista;
	}
	
	public Usuario getUsuario(int codigo) {
		for (Usuario u: usuariosRegistrados.values()) {
			if (u.getCodigo()==codigo) return u;
		}
		return null;
	}
	
	public Usuario getUsuario(String movil) {
		return usuariosRegistrados.get(movil); 
	}
	
	public void addUsuario(Usuario usuario) {
		usuariosRegistrados.put(usuario.getMovil(),usuario);
	}
	public void removeUsuario (Usuario usuario) {
		usuariosRegistrados.remove(usuario.getMovil());
	}
	
	/*Recupera todos los usuarios para trabajar con ellos en memoria*/
	private void cargarRepositorio() throws DAOException {
		 List<Usuario> usuariosBD = daoUsuario.recuperarTodosUsuarios();
		 for (Usuario usuario: usuariosBD) 
			     usuariosRegistrados.put(usuario.getMovil(),usuario);
	}
}
