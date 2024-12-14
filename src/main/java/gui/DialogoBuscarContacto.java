package gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class DialogoBuscarContacto extends JDialog {

	public DialogoBuscarContacto(Frame owner) {
		super(owner, "Tabla de Contactos", true);

		String[] columnNames = { "Nombre", "Teléfono", "Saludo" };
		String[][] data = { { "Irene master", "123456789", "Hola!" }, { "Diego Sevilla", "987654321", "Buenos días" },
				{ "Javier candel", "", "" }, { "Ahmed-Shadia", "", "" }, { "Jose Hoyos", "456123789", "Saludos" },
				{ "Paco seguros", "", "" }, { "Jean Cleve", "321654987", "Hi there!" }, { "Javier Bermudez", "", "" },
				{ "Futbol sabados", "", "" }, { "modelum", "", "" } };

		JTable contactTable = new JTable(data, columnNames);
		contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JButton selectButton = new JButton("Seleccionar");
		selectButton.addActionListener(e -> {
			int selectedRow = contactTable.getSelectedRow();
			if (selectedRow != -1) {
				String selectedContact = (String) contactTable.getValueAt(selectedRow, 0);
				JOptionPane.showMessageDialog(this, "Seleccionaste: " + selectedContact);
				dispose();
			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(contactTable), BorderLayout.CENTER);
		add(selectButton, BorderLayout.SOUTH);
		setSize(500, 300);
		setLocationRelativeTo(owner);
	}
}
