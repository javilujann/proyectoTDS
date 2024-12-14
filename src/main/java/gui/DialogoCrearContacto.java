package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class DialogoCrearContacto extends JDialog {
	private JTextField txtNombre, txtTelefono;

	public DialogoCrearContacto(JFrame parent) {
		super(parent, "Crear Contacto", true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(300, 150);
		setLayout(new GridLayout(3, 2));

		JLabel lblNombre = new JLabel("Nombre:");
		txtNombre = new JTextField();
		JLabel lblTelefono = new JLabel("Teléfono:");
		txtTelefono = new JTextField();

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombre = txtNombre.getText();
				String telefono = txtTelefono.getText();
				if (!nombre.isEmpty() && !telefono.isEmpty()) {
					// Aquí guardarías el contacto (en una lista, base de datos, etc.)
					JOptionPane.showMessageDialog(DialogoCrearContacto.this, "Contacto creado correctamente.");
					dispose(); // Cerrar el diálogo
				} else {
					JOptionPane.showMessageDialog(DialogoCrearContacto.this, "Debe rellenar todos los campos.");
				}
			}
		});

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(e -> dispose());

		add(lblNombre);
		add(txtNombre);
		add(lblTelefono);
		add(txtTelefono);
		add(btnGuardar);
		add(btnCancelar);

		setLocationRelativeTo(parent); // Centrar respecto al JFrame principal
	}
}
