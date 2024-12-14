package gui;

import java.awt.Color;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import dominio.Contacto;



@SuppressWarnings("serial")
public class Elemento extends JPanel{
	
	private Contacto contacto;
	
	public Elemento(Contacto _contacto) {
		contacto = _contacto;
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		UtilsGui.fixSize(this,280,100);
		this.setBackground(Color.LIGHT_GRAY);
		this.setOpaque(true);
		this.setBorder(new TitledBorder(contacto.getNombre()));
		
		JLabel lblimagen=new JLabel();
		Image imagen = contacto.getImage();
		lblimagen.setIcon(new ImageIcon(imagen.getScaledInstance(75, 84, Image.SCALE_SMOOTH)));
		UtilsGui.fixSize(lblimagen,75,84);
		lblimagen.setBorder(new LineBorder(Color.BLACK,2));
		this.add(lblimagen);
		
		//Aqui la logica para mostrar el ultimo mensaje
	}
	
	public Contacto getContacto() {
		return contacto;
	}
	
	
}
