package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controlador.Controlador;
import dominio.Contacto;

@SuppressWarnings("serial")
public class DialogoBuscarContacto extends JDialog {

    public DialogoBuscarContacto(Frame owner) {
        super(owner, "Tabla de Contactos", true);
       // setSize(400, 400);
		setLocationRelativeTo(owner);

        // Columnas de la tabla
        String[] columnNames = { "Nombre", "Teléfono", "Saludo" };

        // Obtener la lista de contactos desde el controlador
        List<Contacto> contactos = Controlador.INSTANCE.getContactos();

        // Convertir la lista de contactos en un array para la tabla
        String[][] data = new String[contactos.size()][3];
        for (int i = 0; i < contactos.size(); i++) {
            Contacto contacto = contactos.get(i);
            String[] detalles = contacto.obtenerDetalles(); // Método que ya implementaste
            data[i][0] = detalles[0]; // Nombre
            data[i][1] = detalles[1]; // Teléfono
            data[i][2] = detalles[2]; // Saludo
        }

        // Crear el modelo de tabla
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que las celdas no sean editables
            }
        };

        // Crear la tabla
        JTable contactTable = new JTable(tableModel);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Botón "Seleccionar"
        JButton selectButton = new JButton("Seleccionar");
        selectButton.addActionListener(e -> {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow != -1) {
                // Obtener el objeto Contacto correspondiente
                Contacto selectedContact = contactos.get(selectedRow);
                JOptionPane.showMessageDialog(this, "Seleccionaste: " + selectedContact);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un contacto.");
            }
        });

        // Configurar el diseño
        setLayout(new BorderLayout());
        add(new JScrollPane(contactTable), BorderLayout.CENTER);
        add(selectButton, BorderLayout.SOUTH);
        setSize(500, 300);
        setLocationRelativeTo(owner);
    }
}
