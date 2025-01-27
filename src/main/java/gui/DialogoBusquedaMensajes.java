package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import controlador.Controlador;
import dominio.Mensaje;
import dominio.TipoMensaje;

@SuppressWarnings("serial")
public class DialogoBusquedaMensajes extends JDialog {

	public DialogoBusquedaMensajes(Frame owner) {
		super(owner, "Buscar Mensajes", true);
		setSize(400, 400);
		setLocationRelativeTo(owner);

		// Componentes de búsqueda
		JLabel contactLabel = new JLabel("Contacto o Teléfono:");
		JTextField contactField = new JTextField();

		JLabel textLabel = new JLabel("Texto a buscar:");
		JTextField textField = new JTextField();

		JLabel typeLabel = new JLabel("Tipo de mensaje:");
		TipoMensaje[] messageTypes = { TipoMensaje.SENT, TipoMensaje.RECEIVED, TipoMensaje.BOTH };
		JComboBox<TipoMensaje> typeComboBox = new JComboBox<>(messageTypes);

		JButton searchButton = new JButton("Buscar");

		// Componente para mostrar los resultados (usamos JList para mostrar objetos)
		DefaultListModel<Mensaje> mensajeModel = new DefaultListModel<>();
		JList<Mensaje> mensajeList = new JList<>(mensajeModel);
		mensajeList.setCellRenderer(new MensajeCellRenderer());

		// Panel de entrada
		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		inputPanel.add(contactLabel);
		inputPanel.add(contactField);
		inputPanel.add(textLabel);
		inputPanel.add(textField);
		inputPanel.add(typeLabel);
		inputPanel.add(typeComboBox);

		// Panel inferior con distribución vertical
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(searchButton); 
		bottomPanel.add(Box.createRigidArea(new Dimension(0, 5))); 
		bottomPanel.add(new JScrollPane(mensajeList)); 

		// Layout principal
		setLayout(new BorderLayout(10, 10));
		add(inputPanel, BorderLayout.NORTH); 
		add(bottomPanel, BorderLayout.CENTER); 

		// Listener para búsqueda
		searchButton.addActionListener(e -> {
			String contact = contactField.getText().trim();
			String text = textField.getText().trim();
			TipoMensaje type = (TipoMensaje) typeComboBox.getSelectedItem();

			// Simular búsqueda de mensajes
			List<Mensaje> mensajes = Controlador.INSTANCE.buscarMensajes(contact, text, type);

			// Actualizar la lista con los resultados
			mensajeModel.clear();
			for (Mensaje mensaje : mensajes) {
				mensajeModel.addElement(mensaje);
			}
		});
	}

}