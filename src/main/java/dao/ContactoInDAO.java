package dao;


import dominio.ContactoIndividual;

public interface ContactoInDAO {
	public void registrarContacto(ContactoIndividual contacto);
	public void borrarContacto(ContactoIndividual contacto);
	public void modificarContacto(ContactoIndividual contacto);
	public ContactoIndividual recuperarContacto(int codigo);
}
