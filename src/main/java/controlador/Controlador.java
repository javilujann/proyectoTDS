package controlador;

import java.util.Date;

import dao.DAOException;
import dao.FactoriaDAO;
import dao.UsuarioDAO;
import dominio.RepositorioUsuarios;
import dominio.Usuario;

public enum Controlador {
	INSTANCE;
	private Usuario usuarioActual;
	private FactoriaDAO factoria;

	private Controlador() {
		usuarioActual = null;
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public boolean esUsuarioRegistrado(String movil) {
		return RepositorioUsuarios.getUnicaInstancia().getUsuario(movil) != null;
	}

	public boolean loginUsuario(String movil, String password) {
		Usuario usuario = RepositorioUsuarios.getUnicaInstancia().getUsuario(movil);
		if (usuario != null && usuario.getContraseña
				().equals(password)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}

	public boolean registrarUsuario(String nombre, String apellidos, String movil, String contraseña, String imagen, String bio, Date fechaNacimiento) {

		if (esUsuarioRegistrado(movil))
			return false;
		Usuario usuario = new Usuario(nombre, apellidos, movil, contraseña, imagen, bio, fechaNacimiento);

		UsuarioDAO usuarioDAO = factoria
				.getUsuarioDAO(); /* Adaptador DAO para almacenar el nuevo Usuario en la BD */
		usuarioDAO.registrarUsuario(usuario);

		RepositorioUsuarios.getUnicaInstancia().addUsuario(usuario);
		return true;
	}

	public boolean borrarUsuario(Usuario usuario) {
		if (!esUsuarioRegistrado(usuario.getMovil()))
			return false;

		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO(); /* Adaptador DAO para borrar el Usuario de la BD */
		usuarioDAO.borrarUsuario(usuario);

		RepositorioUsuarios.getUnicaInstancia().removeUsuario(usuario);
		return true;
	}
}