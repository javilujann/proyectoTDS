package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import dominio.Contacto;
import dominio.Mensaje;



@SuppressWarnings("serial")
public class Elemento extends JPanel{
	
	private Contacto contacto;
	
	public Elemento(Contacto _contacto) {
		contacto = _contacto;
		
		//Ajustamos el Panel
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		UtilsGui.fixSize(this,280,100);
		this.setBackground(Color.LIGHT_GRAY);
		this.setOpaque(true);
		this.setBorder(new TitledBorder(contacto.getNombre()));
		
		//Añadimos la imagen
		JLabel lblimagen=new JLabel();
		Image imagen = contacto.getImage();
		lblimagen.setIcon(new ImageIcon(imagen.getScaledInstance(75, 84, Image.SCALE_SMOOTH)));
		UtilsGui.fixSize(lblimagen,75,84);
		lblimagen.setBorder(new LineBorder(Color.BLACK,2));
		this.add(lblimagen);
		
		// Añadimos el último mensaje
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        UtilsGui.fixSize(messagePanel, 180, 84); // Ajusta según sea necesario
        
        JLabel lastMessageLabel = new JLabel();
        lastMessageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lastMessageLabel.setForeground(Color.DARK_GRAY);

        // Obtener el último mensaje
        String lastMessage = contacto.ultimoMensaje()
                                     .map(Mensaje::getTexto)
                                     .map(this::truncateMessage) // Truncar si es muy largo
                                     .orElse("Sin mensajes");
        
        lastMessageLabel.setText("<html><i>" + lastMessage + "</i></html>");
        messagePanel.add(lastMessageLabel, BorderLayout.CENTER);
        
        this.add(Box.createHorizontalStrut(10));
        this.add(messagePanel);
    }

    private String truncateMessage(String mensaje) {
        final int MAX_LENGTH = 50;
        return mensaje.length() > MAX_LENGTH ? mensaje.substring(0, MAX_LENGTH) + "..." : mensaje;
    }
	
	public Contacto getContacto() {
		return contacto;
	}
	
	
}
