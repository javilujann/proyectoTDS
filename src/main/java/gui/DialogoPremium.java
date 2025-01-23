package gui;

import javax.swing.*;

import controlador.Controlador;
import dominio.Contacto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class DialogoPremium extends JDialog {

	public DialogoPremium(JFrame owner, boolean isPremium) {
		super(owner, "Premium", true);
		if (!isPremium)
			initializePayWindow();
		else
			initializePremiumFuncionality();
		setSize(400, 300);
		setLocationRelativeTo(owner);
	}

	private void initializePayWindow() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(10, 10));

		int precio = Controlador.INSTANCE.getUsuarioActual().getCodigo(); // Cambiar a getPrecio, donde se aplica el
		// Descuento
		JLabel label = new JLabel("Accede a Premium por solo " + precio + " al mes", JLabel.CENTER);
		panel.add(label, BorderLayout.CENTER);

		JButton payButton = new JButton("Pagar");
		UtilsGui.fixSize(payButton, 100, 30);
		payButton.addActionListener(e -> {
			JOptionPane.showMessageDialog(DialogoPremium.this, "Se ha dado de alta con exito", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			Controlador.INSTANCE.altaPremium();
			dispose();
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(payButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		add(panel);
	}

	private void initializePremiumFuncionality() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(10, 10));

		// Obtener la lista de contactos del controlador
		List<Contacto> contactos = Controlador.INSTANCE.getContactos();

		// Crear un mapa para asociar los nombres de los contactos con los objetos
		// Contacto
		Map<String, Contacto> contactosMap = new HashMap<>();
		for (Contacto contacto : contactos) {
			contactosMap.put(contacto.getNombre(), contacto);
		}

		// Crear un array con los nombres de los contactos
		String[] contactNames = contactosMap.keySet().toArray(new String[0]);

		// Seleccionador de contactos
		JPanel contactPanel = new JPanel();
		contactPanel.setLayout(new BorderLayout());
		JLabel contactLabel = new JLabel("Selecciona un contacto:");
		JList<String> contactList = new JList<>(contactNames);
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane contactScrollPane = new JScrollPane(contactList);

		contactPanel.add(contactLabel, BorderLayout.NORTH);
		contactPanel.add(contactScrollPane, BorderLayout.CENTER);

		// Botones en la parte inferior
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		JButton createPdfButton = new JButton("Crear PDF");
		createPdfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedContactName = contactList.getSelectedValue();
				if (selectedContactName != null) {
					Contacto selectedContact = contactosMap.get(selectedContactName);

					// Obtener los mensajes del contacto seleccionado
					List<String> mensajes = selectedContact.getListaMensajes().stream().map(m -> m.getTexto()).toList();

					// Aquí se podrían procesar los mensajes para crear el PDF
					JOptionPane.showMessageDialog(DialogoPremium.this,
							"Creando PDF para: " + selectedContactName + "\nMensajes: " + mensajes, "Información",
							JOptionPane.INFORMATION_MESSAGE);

					dispose();

					// Llamada al controlador para generar el PDF
					// Controlador.INSTANCE.generarPdf(selectedContact);
				} else {
					JOptionPane.showMessageDialog(DialogoPremium.this,
							"Por favor, selecciona un contacto antes de continuar.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton unsubscribeButton = new JButton("Darse de Baja");
		unsubscribeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Controlador.INSTANCE.bajaPremium();
				JOptionPane.showMessageDialog(DialogoPremium.this, "Se ha dado de baja con exito", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				Controlador.INSTANCE.bajaPremium();
				dispose();

			}
		});
		

		buttonPanel.add(createPdfButton);
		buttonPanel.add(unsubscribeButton);

		// Añadir componentes al panel principal
		panel.add(contactPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		add(panel);

	}

}
