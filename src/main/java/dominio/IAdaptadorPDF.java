package dominio;

public interface IAdaptadorPDF {
	void exportarMensajes(Usuario usuario, Contacto contacto) throws Exception;
}
