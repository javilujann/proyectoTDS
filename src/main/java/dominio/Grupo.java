package dominio;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Grupo extends Contacto{
	//CONSTANTES
	private static final String URLGrupo = "https://media.traveler.es/photos/64822b893d22fe2978e8fe36/16:9/w_1920,c_limit/manyminions.0.jpg";
	
	//DEFINICION DE CLASE
	private List<ContactoIndividual> miembros;

	public Grupo(String nombre) {
		super(nombre);
		this.miembros = new ArrayList<ContactoIndividual>();
	}
	
	//Getters y Setters

	public List<ContactoIndividual> getMiembros() {
		return miembros;
	}

	public void setMiembros(List<ContactoIndividual> miembros) {
		this.miembros = miembros;
	}
	
	public void addMiembro(ContactoIndividual miembro) {
		this.miembros.add(miembro);
	}
	
	//Implemnetacion de Herencia
	
	@Override
    public String[] obtenerDetalles() {
        return new String[]{getNombre(), "", ""};
    }
	
	@Override
	public Image getImage() {
		URL url = null;
		
		try {
			url = new URL(URLGrupo);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	@Override
	public boolean corresponde(String movil) {
		return false; //Los grupos no manejan moviles
	}
	
	//Metodos Normales
	
	@Override
	public boolean isGroup() {
		return true;
	}
	
	
	
}
