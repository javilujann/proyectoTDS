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

	public DialogoCrearContacto(JFrame parent, ContactoIndividual contacto) {
		super(parent, "Crear Contacto", true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(300, 150);
		setLayout(new GridLayout(3, 2));

		JLabel lblNombre = new JLabel("Nombre:");
		txtNombre = new JTextField();
		JLabel lblTelefono = new JLabel("Teléfono:");
		txtTelefono = new JTextField();
		
		 // Verificamos si el contacto es nuevo o no agregado
        if (contacto != null) { 
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
					int error = 0;
					if(contacto == null) error = Controlador.INSTANCE.añadirContacto(nombre, telefono);
					else Controlador.INSTANCE.agregarContacto(contacto, nombre);
					switch(error) {
						case 0:
							JOptionPane.showMessageDialog(DialogoCrearContacto.this, "Contacto creado correctamente.");
							dispose(); // Cerrar el diálogo
							break;
						case -1:
							JOptionPane.showMessageDialog(DialogoCrearContacto.this, "No hay ningun Usuario asociado a dicho número.");				
							break;
						case -2:
							JOptionPane.showMessageDialog(DialogoCrearContacto.this, "Ya hay un contacto agregado con dicho número.");
							break;
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

		setLocationRelativeTo(parent); // Centrar respecto al JFrame principal
		
	}
	
}
