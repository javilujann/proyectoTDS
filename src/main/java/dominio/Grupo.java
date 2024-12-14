package dominio;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Grupo extends Contacto{
	private static final String URLGrupo = "https://media.traveler.es/photos/64822b893d22fe2978e8fe36/16:9/w_1920,c_limit/manyminions.0.jpg";
	private ArrayList<ContactoIndividual> miembros;

	public Grupo(String nombre) {
		super(nombre);
		this.miembros = new ArrayList<ContactoIndividual>();
	}

	public ArrayList<ContactoIndividual> getMiembros() {
		return miembros;
	}

	public void setMiembros(ArrayList<ContactoIndividual> miembros) {
		this.miembros = miembros;
	}
	
	public void addMiembro(ContactoIndividual miembro) {
		this.miembros.add(miembro);
	}
	
	public boolean corresponde(String movil) {
		return false; //Los grupos no manejan moviles
	}
	
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
	
}
