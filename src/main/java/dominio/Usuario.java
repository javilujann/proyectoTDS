package dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dao.ContactoInDAO;
import dao.DAOException;
import dao.FactoriaDAO;

public class Usuario {
	
	private int codigo;
	private String nombre;
	private String apellidos;
	private String movil;
	private String contraseña;
	private String imagen;				//url de la imagen usada//
	private Boolean premium;
	private List<Contacto> contactos;
	private String biografia;
	private Date fechaNacimiento;
	
	
	//CONSTRUCTOR
	
	public Usuario(String nombre, String apellidos, String movil, String contraseña, String imagen,
			String biografia,Date fechaNacimiento) {
		super();
		this.codigo = 0;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.movil = movil;
		this.contraseña = contraseña;
		this.imagen = imagen;
		this.premium = false;
		this.contactos = new ArrayList<Contacto>();
		this.biografia =  biografia;
		this.fechaNacimiento = fechaNacimiento;
		
	}

	//GETTER AND SETTER
		
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getMovil() {
		return movil;
	}


	public void setMovil(String movil) {
		this.movil = movil;
	}


	public String getContraseña() {
		return contraseña;
	}


	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}


	public String getImagen() {
		return imagen;
	}



	public void setImagen(String imagen) {
		this.imagen = imagen;
	}


	public Boolean isPremium() {
		return premium;
	}


	public void setPremium(Boolean premium) {
		this.premium = premium;
	}


	public List<Contacto> getContactos() {
		return contactos;
	}


	public void addContacto(Contacto contacto) {
		this.contactos.add(contacto);
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getBiografia() {
		return biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}



	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}



	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	//METODOS
	
	public boolean chekContraseña(String _contraseña) {
		return contraseña.equals(_contraseña);
	}
	
	public List<Mensaje> ultimosMensajes(){
		return contactos.stream().map(c -> c.ultimoMensaje()).filter(optional -> optional.isPresent()).
				map(present -> present.get()).collect(Collectors.toList());
	}	
	
	//MUY GRANDE VER SI SE PUEDE HACER MAS SENCILLO
	public void recibirMensaje(String movil, Mensaje mensaje) {
		ContactoInDAO adaptadorContacto;
		
		try {
			adaptadorContacto = FactoriaDAO.getInstancia().getContactoIDAO();
		} catch (DAOException e) {
			e.printStackTrace();
			return;
		}
		
		Optional<Contacto> optionalContacto = contactos.stream().filter(c -> c.corresponde(movil)).findFirst();
		ContactoIndividual contacto = (ContactoIndividual) optionalContacto.orElseGet(() -> {
		    ContactoIndividual nuevoContacto = new ContactoIndividual(movil, movil);
		    nuevoContacto.setAgregado(false);
		    adaptadorContacto.registrarContacto(nuevoContacto);
		    contactos.add(nuevoContacto); 
		    return nuevoContacto;
		});
		
		contacto.addMensaje(mensaje);
		adaptadorContacto.modificarContacto(contacto);
	}
	
	

	
}
