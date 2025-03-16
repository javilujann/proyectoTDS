package gui;

import javax.swing.*;

import controlador.Controlador;
import dominio.ContactoIndividual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class DialogoCrearContacto extends JDialog {
	private JTextField txtNombre, txtTelefono;
	private ContactoIndividual contacto;

	public DialogoCrearContacto(VentanaPrincipal parent, ContactoIndividual _contacto) {
		super(parent.getFrame(), "Crear Contacto", true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(300, 150);
		setLayout(new GridLayout(3, 2));

		JLabel lblNombre = new JLabel("Nombre:");
		txtNombre = new JTextField();
		JLabel lblTelefono = new JLabel("Teléfono:");
		txtTelefono = new JTextField();

		// Verificamos si el contacto es nuevo o no agregado
		contacto = _contacto;
		boolean nuevo = contacto == null;
		if (!nuevo) {
			txtTelefono.setText(contacto.getMovil());
			txtTelefono.setEditable(false);
		}

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombre = txtNombre.getText();
				String telefono = txtTelefono.getText();
				if (!nombre.isEmpty() && !telefono.isEmpty()) {
					if (nuevo)
						contacto = (ContactoIndividual) Controlador.INSTANCE.añadirContacto(nombre, telefono);
					else
						Controlador.INSTANCE.agregarContacto(contacto, nombre);

					if (contacto == null) {
						JOptionPane.showMessageDialog(DialogoCrearContacto.this,
								"Error\n O no hay ningun Usuario asociado a dicho número\n O ya hay un contacto agregado con dicho número");
					} else {
						if (nuevo)
							parent.añadirListaContactos(contacto);
						else
							parent.actualizarListaContactos(contacto);
						dispose();
					}

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

		setLocationRelativeTo(parent.getFrame()); // Centrar respecto al JFrame principal

	}

}
