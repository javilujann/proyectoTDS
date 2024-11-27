package dao;

import dominio.Mensaje;

public interface MensajeDAO {
	
	public void registrarMensaje(Mensaje mensaje);
	public void borrarMensaje(Mensaje mensaje);
	public void modificarMensaje(Mensaje mensaje);
	public Mensaje recuperarMensaje(int codigo);
}
