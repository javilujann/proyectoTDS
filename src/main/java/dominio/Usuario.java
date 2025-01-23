package dominio;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import dao.ContactoDAO;
import dao.DAOException;
import dao.FactoriaDAO;

public class Usuario {
	
	private int codigo;
	private String nombre;
	private String apellidos;
	private String movil;
	private String contraseña;
	private String imagen;				//url de la imagen usada
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


	public Image getImagen() {
			URL url = null;
			BufferedImage image = null;
			
			try {
				url = new URL(imagen);
				image = ImageIO.read(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return image;
	}
	
	public String getURL() {
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
	
	public void bajaPremium() {
		premium = false;
		//Posible inlcusion de codigo necesario para la baja, como gestion de pagos
	}
	
	public void altaPremium() {
		premium = true;
		//Posible inlcusion de codigo necesario para la alta, como gestion de pagos
	}
	
	public List<Mensaje> ultimosMensajes(){
		return contactos.stream().map(c -> c.ultimoMensaje()).filter(optional -> optional.isPresent()).
				map(present -> present.get()).collect(Collectors.toList());
	}
	
	public List<Mensaje> buscarMensajes(String contact, String text, TipoMensaje type) {
		return contactos.stream().filter(c -> contact.equals("") || c.comparar(contact))
				.flatMap(c -> c.buscarMensajes(text,type).stream()).collect(Collectors.toList());
	}
	
	//MUY GRANDE VER SI SE PUEDE HACER MAS SENCILLO
	public void recibirMensaje(String movil, Mensaje mensaje) {
		ContactoDAO adaptadorContacto;
		
		try {
			adaptadorContacto = FactoriaDAO.getInstancia().getContactoDAO(ContactoIndividual.class);
		} catch (DAOException e) {
			e.printStackTrace();
			return;
		}
		
		Optional<Contacto> optionalContacto = contactos.stream().filter(c -> c.corresponde(movil)).findFirst();
		ContactoIndividual contacto = (ContactoIndividual) optionalContacto.orElseGet(() -> {
		    ContactoIndividual nuevoContacto = new ContactoIndividual(movil, movil,null); //REPOSITORIO GETUSER
		    nuevoContacto.setAgregado(false);
		    adaptadorContacto.registrarContacto(nuevoContacto);
		    contactos.add(nuevoContacto); 
		    return nuevoContacto;
		});
		
		contacto.addMensaje(mensaje);
		adaptadorContacto.modificarContacto(contacto);
	}
	
	public int nuevoContactoIn(String nombre, String telefono, Usuario asociado) {
		for(Contacto c : contactos) {
			if(c.corresponde(telefono)) return -2;
		}
		
		ContactoIndividual nuevoContacto = new ContactoIndividual(nombre,telefono,asociado);
		nuevoContacto.setAgregado(true);
		contactos.add(nuevoContacto);
		return 0;
		//Se esta creando pero no guardando el contacto ni actualizando al usuario
	}
	
	

	
}
