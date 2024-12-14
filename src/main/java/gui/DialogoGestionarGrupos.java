package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DialogoGestionarGrupos extends JDialog {

	private DefaultListModel<String> contactsModel;
	private DefaultListModel<String> addedContactsModel;

	public DialogoGestionarGrupos(Frame owner) {
		super(owner, "Seleccionar Contactos", true);

		// Modelos para las listas
		contactsModel = new DefaultListModel<>();
		addedContactsModel = new DefaultListModel<>();

		// Listas
		JList<String> contactsList = new JList<>(contactsModel);
		JList<String> addedContactsList = new JList<>(addedContactsModel);

		// Encabezados
		JPanel contactsPanel = new JPanel(new BorderLayout());
		contactsPanel.add(new JLabel("Contactos"), BorderLayout.NORTH);
		contactsPanel.add(new JScrollPane(contactsList), BorderLayout.CENTER);

		JPanel addedContactsPanel = new JPanel(new BorderLayout());
		addedContactsPanel.add(new JLabel("Contactos añadidos"), BorderLayout.NORTH);
		addedContactsPanel.add(new JScrollPane(addedContactsList), BorderLayout.CENTER);

		// Botones para mover contactos
		JButton addButton = new JButton("->");
		JButton removeButton = new JButton("<-");

		// Botones de acción
		JButton acceptButton = new JButton("Aceptar");
		JButton cancelButton = new JButton("Cancelar");

		// Paneles
		JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
		JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		// Añadir contactos iniciales a la lista
		initializeContacts();

		// Configurar layout
		listsPanel.add(contactsPanel);
		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);
		listsPanel.add(buttonsPanel);
		listsPanel.add(addedContactsPanel);

		actionsPanel.add(acceptButton);
		actionsPanel.add(cancelButton);

		setLayout(new BorderLayout(10, 10));
		add(listsPanel, BorderLayout.CENTER);
		add(actionsPanel, BorderLayout.SOUTH);

		// Listeners
		addButton.addActionListener(e -> moveSelectedItems(contactsList, contactsModel, addedContactsModel));
		removeButton.addActionListener(e -> moveSelectedItems(addedContactsList, addedContactsModel, contactsModel));

		acceptButton.addActionListener(e -> {
			List<String> selectedContacts = new ArrayList<>();
			for (int i = 0; i < addedContactsModel.getSize(); i++) {
				selectedContacts.add(addedContactsModel.getElementAt(i));
			}
			JOptionPane.showMessageDialog(this, "Contactos seleccionados: " + selectedContacts);
			dispose();
		});

		cancelButton.addActionListener(e -> dispose());

		// Configuración final del diálogo
		setSize(400, 300);
		setLocationRelativeTo(owner);
	}

	private void initializeContacts() {
		// Agregar contactos iniciales
		String[] initialContacts = { "Irene master", "Diego Sevilla", "Javier candel", "Ahmed-Shadia", "Jose Hoyos",
				"Paco seguros", "Jean Cleve", "Javier Bermudez", "Futbol sabados", "modelum" };
		for (String contact : initialContacts) {
			contactsModel.addElement(contact);
		}
	}

	private void moveSelectedItems(JList<String> sourceList, DefaultListModel<String> sourceModel,
			DefaultListModel<String> targetModel) {
		List<String> selectedItems = sourceList.getSelectedValuesList();
		for (String item : selectedItems) {
			sourceModel.removeElement(item);
			targetModel.addElement(item);
		}
	}

}
