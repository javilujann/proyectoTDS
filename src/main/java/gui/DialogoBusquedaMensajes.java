package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DialogoBusquedaMensajes extends JDialog {

	public DialogoBusquedaMensajes(Frame owner) {
		super(owner, "Buscar Mensajes", true);

		// Componentes de búsqueda
		JLabel contactLabel = new JLabel("Contacto o Teléfono:");
		JTextField contactField = new JTextField();

		JLabel textLabel = new JLabel("Texto a buscar:");
		JTextField textField = new JTextField();

		JLabel typeLabel = new JLabel("Tipo de mensaje:");
		String[] messageTypes = { "Enviado", "Recibido", "Ambos" };
		JComboBox<String> typeComboBox = new JComboBox<>(messageTypes);

		JButton searchButton = new JButton("Buscar");
		JTextArea resultsArea = new JTextArea(10, 30);
		resultsArea.setEditable(false);

		// Layout
		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		inputPanel.add(contactLabel);
		inputPanel.add(contactField);
		inputPanel.add(textLabel);
		inputPanel.add(textField);
		inputPanel.add(typeLabel);
		inputPanel.add(typeComboBox);

		JPanel resultsPanel = new JPanel(new BorderLayout());
		resultsPanel.add(new JScrollPane(resultsArea), BorderLayout.CENTER);

		setLayout(new BorderLayout(10, 10));
		add(inputPanel, BorderLayout.NORTH);
		add(searchButton, BorderLayout.CENTER);
		add(resultsPanel, BorderLayout.SOUTH);

		// Listener para búsqueda
		searchButton.addActionListener(e -> {
			String contact = contactField.getText().trim();
			String text = textField.getText().trim();
			String type = (String) typeComboBox.getSelectedItem();

			// Simular búsqueda de mensajes
			StringBuilder results = new StringBuilder("Resultados de búsqueda:\n");
			results.append("(Simulación) Buscando mensajes de tipo '").append(type).append("' ").append("con texto '")
					.append(text).append("' para contacto '").append(contact).append("'.\n");

			// Aquí se implementaría la lógica real de búsqueda

			resultsArea.setText(results.toString());
		});

		// Configuración final del diálogo
		setSize(400, 400);
		setLocationRelativeTo(owner);
	}
}
