package dao;


import dominio.Contacto;

public interface ContactoDAO {
	public void registrarContacto(Contacto contacto);
	public void borrarContacto(Contacto contacto);
	public void modificarContacto(Contacto contacto);
	public Contacto recuperarContacto(int codigo);
}
