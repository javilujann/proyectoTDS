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
	private Descuento descuento;
	
	
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
		this.descuento = null; //POSIBLE QUE SEA OBLIGATORIO
		
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
	
	public List<Grupo> getGrupos(){
		return contactos.stream().filter(c -> c.isGroup()).map(c -> (Grupo) c).toList();
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
	
	public Optional<Descuento> getDescuento(){
		return Optional.ofNullable(descuento);
	}
	
	public void setDescuento(Descuento descuento) {
		this.descuento = descuento;
	}

	//METODOS
	
	public boolean chekContraseña(String _contraseña) {
		return contraseña.equals(_contraseña);
	}
	
	public void bajaPremium() {
		premium = false; //Posible inlcusion de codigo necesario para la baja, como gestion de pagos		
	}
	
	public void altaPremium() {
		premium = true; //Posible inlcusion de codigo necesario para la alta, como gestion de pagos
	}
	
	public List<Mensaje> ultimosMensajes(){
		return contactos.stream().map(c -> c.ultimoMensaje()).filter(optional -> optional.isPresent()).
				map(present -> present.get()).collect(Collectors.toList());
	}
	
	public List<Mensaje> buscarMensajes(String contact, String text, TipoMensaje type) {
		return contactos.stream().filter(c -> contact.equals("") || c.comparar(contact))
				.flatMap(c -> c.buscarMensajes(text,type).stream()).collect(Collectors.toList());
	}
	
/*<<<<<<< HEAD
	//MUY GRANDE VER SI SE PUEDE HACER MAS SENCILLO
	public ContactoIndividual recibirMensaje(Usuario usuario) {
		String movil = usuario.getMovil();
        Optional<Contacto> optionalContacto = contactos.stream().filter(c -> c.corresponde(movil)).findFirst();

        ContactoIndividual contacto = (ContactoIndividual) optionalContacto.orElseGet(() -> {
            ContactoIndividual nuevoContacto = new ContactoIndividual(movil, movil, usuario);
            nuevoContacto.setAgregado(false);
            contactos.add(nuevoContacto); 
            return nuevoContacto;
        });

        return contacto;
=======*/
	public ContactoIndividual recibirMensaje(Usuario usuario) {
		String movil = usuario.getMovil();
		Optional<Contacto> optionalContacto = contactos.stream().filter(c -> c.corresponde(movil)).findFirst();
		
		ContactoIndividual contacto = (ContactoIndividual) optionalContacto.orElseGet(() -> {
		    ContactoIndividual nuevoContacto = new ContactoIndividual(movil, movil, usuario);
		    nuevoContacto.setAgregado(false);
		    contactos.add(nuevoContacto); 
		    return nuevoContacto;
		});
		
		return contacto;
//>>>>>>> branch 'main' of https://gitlab.com/tds1341744/appchat_tds.git
	}
	
	public Contacto nuevoContactoIn(String nombre, String telefono, Usuario asociado) {
		for(Contacto c : contactos) {
			if(c.corresponde(telefono)) return null; //Puede que de problema con los noAgregados, solucionarlo alli
		}
		
		ContactoIndividual nuevoContacto = new ContactoIndividual(nombre,telefono,asociado);
		nuevoContacto.setAgregado(true);
		contactos.add(nuevoContacto);
		return nuevoContacto;
	}

	public Grupo nuevoGrupo(String nombre) {
		for(Contacto c : contactos) {
			if(c.getNombre().equals(nombre)) return null;
		}
		
		Grupo nuevoGrupo = new Grupo(nombre);
		contactos.add(nuevoGrupo);
		return nuevoGrupo;
	}
	
	

	
}
