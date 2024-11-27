package dominio;

public abstract class Contacto {
	
	private int codigo;

	public Contacto() {
		super();
		this.codigo = 0;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	

}
