package gui;

import javax.swing.*;

import controlador.Controlador;
import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class DialogoGestionarGrupos extends JDialog {

	private DefaultListModel<ContactoIndividual> contactsModel;
	private DefaultListModel<ContactoIndividual> addedContactsModel;
	private Grupo grupoModificar = null;
	private VentanaPrincipal owner;
	
	public DialogoGestionarGrupos(VentanaPrincipal _owner, boolean isNew) {		
		super(_owner.getFrame(), "Seleccionar Contactos", true);
		owner = _owner;
		setSize(400, 300);
		setLocationRelativeTo(owner.getFrame());

		if (isNew)
			initializeGroupCreator();
		else
			initializeGroupSelector();

		SwingUtilities.invokeLater(() -> {
			if (grupoModificar != null) {
				initializeGroupManager();
				revalidate();
				repaint();
			} else {
				dispose();
			}
		});
	}

	private void initializeGroupCreator() {
		String nombreGrupo = JOptionPane.showInputDialog(this, "Introduzca el nombre del grupo:");
		if (nombreGrupo == null)
			return;

		if (nombreGrupo.trim().isEmpty()) {
			JOptionPane.showMessageDialog(DialogoGestionarGrupos.this, "El nombre no puede ser vacio.");
			return;
		}

		grupoModificar = Controlador.INSTANCE.añadirGrupo(nombreGrupo);
		if (grupoModificar == null) {
			JOptionPane.showMessageDialog(DialogoGestionarGrupos.this, "Ya hay un contacto con dicho nombre.");
			return;
		}

		JOptionPane.showMessageDialog(DialogoGestionarGrupos.this, "Grupo creado con exito.");
		owner.añadirListaContactos(grupoModificar);

	}

	private void initializeGroupSelector() {
		List<Grupo> listaGrupos = Controlador.INSTANCE.getGrupos();
		if (listaGrupos.isEmpty()) {
			JOptionPane.showMessageDialog(DialogoGestionarGrupos.this, "No tienes ningun grupo.");
			return;
		}

		Map<String, Grupo> mapaGrupos = listaGrupos.stream()
				.collect(Collectors.toMap(Grupo::getNombre, grupo -> grupo));
		String[] nombresGrupos = mapaGrupos.keySet().toArray(new String[0]);

		// Mostrar diálogo para seleccionar un grupo
		String grupoSeleccionadoNombre = (String) JOptionPane.showInputDialog(this, "Seleccione un grupo:",
				"Modificar Grupo", JOptionPane.QUESTION_MESSAGE, null, nombresGrupos, nombresGrupos[0]);

		// Obetner el objeto Grupo desde el mapa
		grupoModificar = mapaGrupos.get(grupoSeleccionadoNombre);
	}

	private void initializeGroupManager() {

		// Modelos para las listas
		contactsModel = new DefaultListModel<ContactoIndividual>();
		addedContactsModel = new DefaultListModel<ContactoIndividual>();

		// Listas
		JList<ContactoIndividual> contactsList = new JList<>(contactsModel);
		JList<ContactoIndividual> addedContactsList = new JList<>(addedContactsModel);

		// Renderizador para mostrar los nombres de los contactos en lugar de los objetos
	    contactsList.setCellRenderer(new ContactoCellRenderer());
	    addedContactsList.setCellRenderer(new ContactoCellRenderer());
	    
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

		// Listeners para mover elementos
		addButton.addActionListener(e -> moveSelectedItems(contactsList, contactsModel, addedContactsModel));
		removeButton.addActionListener(e -> moveSelectedItems(addedContactsList, addedContactsModel, contactsModel));

		// Listeners
		acceptButton.addActionListener(e -> {
			// Actualizar miembros del grupo solo al aceptar
			List<ContactoIndividual> nuevosMiembros = new ArrayList<>();
			for (int i = 0; i < addedContactsModel.getSize(); i++) {
				nuevosMiembros.add((ContactoIndividual) addedContactsModel.getElementAt(i));
			}
			
			Controlador.INSTANCE.modificarGrupo(grupoModificar,nuevosMiembros); // Actualizar miembros del grupo
			JOptionPane.showMessageDialog(this, "Cambios guardados con éxito.");
			dispose();
		});

		cancelButton.addActionListener(e -> dispose());

	}

	private void initializeContacts() {

		List<Contacto> todosLosContactos = Controlador.INSTANCE.getContactos();
		List<ContactoIndividual> miembrosGrupo = grupoModificar.getMiembros();

		// Filtrar contactos que no están en el grupo y añadirlos al modelo de contactos
		todosLosContactos.stream().filter(c -> !c.isGroup()).map(c -> (ContactoIndividual) c)
				.filter(contacto -> !miembrosGrupo.contains(contacto)) // Filtrar contactos ya añadidos
				.forEach(contactsModel::addElement);

		// Añadir al modelo de contactos añadidos los miembros actuales del grupo
		miembrosGrupo.forEach(addedContactsModel::addElement);
	}

	// Método para mover elementos entre listas
	private void moveSelectedItems(JList<ContactoIndividual> sourceList,
			DefaultListModel<ContactoIndividual> sourceModel, DefaultListModel<ContactoIndividual> targetModel) {
		List<ContactoIndividual> selectedItems = sourceList.getSelectedValuesList();
		for (ContactoIndividual item : selectedItems) {
			sourceModel.removeElement(item);
			targetModel.addElement(item);
		}
	}
	
	// Clase personalizada para mostrar los nombres de los contactos en las listas
	private static class ContactoCellRenderer extends DefaultListCellRenderer {
	    @Override
	    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        if (value instanceof Contacto) {
	            value = ((Contacto) value).getNombre(); // Mostrar el nombre del contacto
	        }
	        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    }
	}

}
