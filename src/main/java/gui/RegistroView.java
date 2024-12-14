package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import controlador.Controlador;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class RegistroView extends JDialog {

	//private JFrame frmRegistroView;
	private JFrame owner;
	private JLabel lblNombre;
	private JLabel lblApellidos;
	private JLabel lblFechaNacimiento;
	private JLabel lblMovil;
	private JLabel lblBio;
	private JLabel lblImg;
	private JLabel lblUsuario;
	private JLabel lblPassword;
	private JLabel lblPasswordChk;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtImagen;
	private JDateChooser txtFechaNacimiento;
	private JTextField txtMovil;
	private JTextArea txtBio;
	private File imagen;
	private JPasswordField txtPassword;
	private JPasswordField txtPasswordChk;
	private JButton btnRegistrar;
	private JButton btnCancelar;
	private JButton btnImagen;
	private JButton btnURL;

	private JLabel lblNombreError;
	private JLabel lblApellidosError;
	private JLabel lblFechaNacimientoError;
	private JLabel lblMovilError;
	private JLabel lblBioError;
	private JLabel lblImagenError;
	//private JLabel lblUsuarioError;
	private JLabel lblPasswordError;
	private JPanel panelCampoNombre;
	private JPanel panel;
	private JPanel panelCampoApellidos;
	private JPanel panelCamposMovil;
	private JPanel panelCampoBio;
	//private JPanel panelCamposUsuario;
	private JPanel panelCamposFechaNacimiento;
	private JPanel panelCampoImg;
	private boolean errorImg;

	public RegistroView(JFrame owner){
		super(owner, "Registro Usuario", true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		this.crearPanelRegistro();
		this.owner = owner;
	}

	private void crearPanelRegistro() {
		this.getContentPane().setLayout(new BorderLayout());

		JPanel datosPersonales = new JPanel();
		this.getContentPane().add(datosPersonales);
		datosPersonales.setBorder(new TitledBorder(null, "Datos de Registro", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		datosPersonales.setLayout(new BoxLayout(datosPersonales, BoxLayout.Y_AXIS));

		datosPersonales.add(creaLineaNombre());
		datosPersonales.add(crearLineaApellidos());
		datosPersonales.add(crearLineaMovil());
		datosPersonales.add(crearLineaPassword());
		datosPersonales.add(crearLineaFechaNacimiento());
		datosPersonales.add(crearLineaBio());
		datosPersonales.add(crearLineaImagen());
		//datosPersonales.add(crearLineaUsuario());
		JScrollPane scroll = new JScrollPane(datosPersonales); 
		this.add(scroll);
		
		this.crearPanelBotones();

		this.ocultarErrores();

		this.revalidate();
		this.pack();
	}

	private JPanel creaLineaNombre() {
		JPanel lineaNombre = new JPanel();
		lineaNombre.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaNombre.setLayout(new BorderLayout(0, 0));
		
		panelCampoNombre = new JPanel();
		lineaNombre.add(panelCampoNombre, BorderLayout.CENTER);
		
		lblNombre = new JLabel("Nombre: ", JLabel.RIGHT);
		panelCampoNombre.add(lblNombre);
		fixedSize(lblNombre, 75, 20);
		txtNombre = new JTextField();
		panelCampoNombre.add(txtNombre);
		fixedSize(txtNombre, 270, 20);
		
		lblNombreError = new JLabel("El nombre es obligatorio", SwingConstants.CENTER);
		lineaNombre.add(lblNombreError, BorderLayout.SOUTH);
		fixedSize(lblNombreError, 224, 15);
		lblNombreError.setForeground(Color.RED);
		
		return lineaNombre;
	}

	private JPanel crearLineaApellidos() {
		JPanel lineaApellidos = new JPanel();
		lineaApellidos.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaApellidos.setLayout(new BorderLayout(0, 0));
		
		panelCampoApellidos = new JPanel();
		lineaApellidos.add(panelCampoApellidos);
		
		lblApellidos = new JLabel("Apellidos: ", JLabel.RIGHT);
		panelCampoApellidos.add(lblApellidos);
		fixedSize(lblApellidos, 75, 20);
		txtApellidos = new JTextField();
		panelCampoApellidos.add(txtApellidos);
		fixedSize(txtApellidos, 270, 20);

		
		lblApellidosError = new JLabel("Los apellidos son obligatorios", SwingConstants.CENTER);
		lineaApellidos.add(lblApellidosError, BorderLayout.SOUTH);
		fixedSize(lblApellidosError, 255, 15);
		lblApellidosError.setForeground(Color.RED);
		
		return lineaApellidos;
	}

	private JPanel crearLineaMovil() {
		JPanel lineaMovil = new JPanel();
		lineaMovil.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaMovil.setLayout(new BorderLayout(0, 0));
		
		panelCamposMovil = new JPanel();
		lineaMovil.add(panelCamposMovil, BorderLayout.CENTER);
		
		lblMovil = new JLabel("Móvil: ", JLabel.RIGHT);
		panelCamposMovil.add(lblMovil);
		fixedSize(lblMovil, 75, 20);
		txtMovil = new JTextField();
		panelCamposMovil.add(txtMovil);
		fixedSize(txtMovil, 270, 20);
		lblMovilError = new JLabel("El teléfono móvil es obligatorio", SwingConstants.CENTER);
		fixedSize(lblMovilError, 150, 15);
		lblMovilError.setForeground(Color.RED);
		lineaMovil.add(lblMovilError, BorderLayout.SOUTH);
		
		return lineaMovil;
	}
	
	private JPanel crearLineaBio() {
		JPanel lineaBio = new JPanel();
		lineaBio.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaBio.setLayout(new BorderLayout(0, 0));
		
		panelCampoBio = new JPanel();
		lineaBio.add(panelCampoBio, BorderLayout.CENTER);
		
		lblBio = new JLabel("Biografía: ", JLabel.RIGHT);
		panelCampoBio.add(lblBio);
		fixedSize(lblBio, 75, 20);
		txtBio = new JTextArea();
		txtBio.setLineWrap(true);
		txtBio.setWrapStyleWord(true);
		
		panelCampoBio.add(txtBio);
		fixedSize(txtBio, 300, 100);
		lblBioError = new JLabel("La biografía no puede quedar vacía", SwingConstants.CENTER);
		fixedSize(lblBioError, 150, 15);
		lblBioError.setForeground(Color.RED);
		lineaBio.add(lblBioError, BorderLayout.SOUTH);
		
		return lineaBio;
		
	}

	private JPanel crearLineaPassword() {
		JPanel lineaPassword = new JPanel();
		lineaPassword.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaPassword.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		lineaPassword.add(panel, BorderLayout.CENTER);
		
		lblPassword = new JLabel("Password: ", JLabel.RIGHT);
		panel.add(lblPassword);
		fixedSize(lblPassword, 75, 20);
		txtPassword = new JPasswordField();
		panel.add(txtPassword);
		fixedSize(txtPassword, 100, 20);
		lblPasswordChk = new JLabel("Otra vez:", JLabel.RIGHT);
		panel.add(lblPasswordChk);
		fixedSize(lblPasswordChk, 60, 20);
		txtPasswordChk = new JPasswordField();
		panel.add(txtPasswordChk);
		fixedSize(txtPasswordChk, 100, 20);

		lblPasswordError = new JLabel("Error al introducir las contrase�as", JLabel.CENTER);
		lineaPassword.add(lblPasswordError, BorderLayout.SOUTH);
		lblPasswordError.setForeground(Color.RED);
		
		return lineaPassword;
	}

	private JPanel crearLineaFechaNacimiento() {
		JPanel lineaFechaNacimiento = new JPanel();
		lineaFechaNacimiento.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaFechaNacimiento.setLayout(new BorderLayout(0, 0));
		
		panelCamposFechaNacimiento = new JPanel();
		lineaFechaNacimiento.add(panelCamposFechaNacimiento, BorderLayout.CENTER);
		
		lblFechaNacimiento = new JLabel("Fecha de Nacimiento: ", JLabel.RIGHT);
		panelCamposFechaNacimiento.add(lblFechaNacimiento);
		fixedSize(lblFechaNacimiento, 130, 20);
		txtFechaNacimiento = new JDateChooser();
		panelCamposFechaNacimiento.add(txtFechaNacimiento);
		fixedSize(txtFechaNacimiento, 215, 20);
		
		return lineaFechaNacimiento;
	}
	
	private JPanel crearLineaImagen() {
		//
		JPanel lineaImagen = new JPanel();
		lineaImagen.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaImagen.setLayout(new BorderLayout(0, 0));
		
		panelCampoImg = new JPanel();
		lineaImagen.add(panelCampoImg, BorderLayout.CENTER);
		
		lblImg = new JLabel("URL de imagen: ", JLabel.RIGHT);
		panelCampoImg.add(lblImg);
		fixedSize(lblImg, 200, 20);
		txtImagen = new JTextField();
		
		btnURL = new JButton("Confirmar");
		panelCampoImg.add(btnURL, BorderLayout.EAST);
		this.crearManejadorBotonURL();
		
		panelCampoImg.add(txtImagen);
		fixedSize(txtImagen, 100, 20);

		lblImagenError = new JLabel("Error al introducir la imagen", JLabel.CENTER);
		lineaImagen.add(lblImagenError, BorderLayout.SOUTH);
		lblImagenError.setForeground(Color.RED);
		
		//
		//lblImg = new JLabel("Icono de usuario:", JLabel.RIGHT);
		//panelCampoImg.add(lblImg);
		actualizarImagen();
		fixedSize(lblImg, 100, 100);
		
		if(imagen != null) actualizarImagen();
		return lineaImagen;

	}

	private void crearPanelBotones() {
		JPanel lineaBotones = new JPanel(); 
		this.getContentPane().add(lineaBotones, BorderLayout.SOUTH);
		lineaBotones.setBorder(new EmptyBorder(5, 0, 0, 0));
		lineaBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		btnRegistrar = new JButton("Registrar");
		lineaBotones.add(btnRegistrar);
		
		btnCancelar = new JButton("Cancelar");
		lineaBotones.add(btnCancelar);

		btnImagen = new JButton("Añadir imagen");
		lineaBotones.add(btnImagen);
		
		this.crearManejadorBotonRegistrar();
		this.crearManejadorBotonCancelar();
		this.crearManejadorBotonImg();
	}
	
	private void crearManejadorBotonURL() {
		btnURL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                String urlText = txtImagen.getText();
                if (isValidImageUrl(urlText)) {
                    ImageIcon imageIcon = new ImageIcon(urlText);
                    lblImg.setIcon(imageIcon);
                } else {
                    lblImg.setIcon(null); // Limpia cualquier imagen previa
                    lblImagenError.setText("URL no válida o imagen no accesible.");
                    errorImg = true;
                }
                lblImg.revalidate();
                lblImg.repaint();
            }
		});
	}

	//Cambiar para que el registro guarde los campos de nuestro usuario correctamente
	private void crearManejadorBotonRegistrar() {
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean OK = false;
				OK = checkFields();
				if (OK) {
					boolean registrado = false;
					registrado = Controlador.INSTANCE.registrarUsuario(
							txtNombre.getText(),
							txtApellidos.getText(), 
							txtMovil.getText(), 
							new String(txtPassword.getPassword()), 
							UtilsGui.getRutaResourceFromFile(imagen),
							txtBio.getText(),
							(Date) txtFechaNacimiento.getDate()
					);
					if (registrado) {
						JOptionPane.showMessageDialog(RegistroView.this, "Usuario registrado correctamente.", "Registro",
								JOptionPane.INFORMATION_MESSAGE);
						
						LoginView loginView = new LoginView();
						loginView.mostrarVentana();
						RegistroView.this.dispose();
					} else {
						JOptionPane.showMessageDialog(RegistroView.this, "No se ha podido llevar a cabo el registro.\n",
								"Registro", JOptionPane.ERROR_MESSAGE);
						RegistroView.this.setTitle("Login Gestor Eventos");
					}
				}
			}
		});
	}

	private void crearManejadorBotonCancelar() {
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginView loginView = new LoginView();
				loginView.mostrarVentana();
				RegistroView.this.dispose();
			}
		});
	}
	
	private void crearManejadorBotonImg() {
		btnImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanelArrastraImagen emergente = new PanelArrastraImagen(owner);
				emergente.setSize(400, 400);
				if(!emergente.showDialog().isEmpty()) imagen =  emergente.showDialog().get(0);
				else imagen = null;
				if(imagen != null) {
					JOptionPane.showMessageDialog(emergente, "Imagen aceptada correctamente.", "Añadir imagen",
							JOptionPane.INFORMATION_MESSAGE);
					
					actualizarImagen();
					/*LoginView loginView = new LoginView();
					loginView.mostrarVentana();*/
					emergente.dispose();
				} else {
					JOptionPane.showMessageDialog(RegistroView.this, "No se ha añadido la imagen.\n",
							"Añadir imagen", JOptionPane.ERROR_MESSAGE);
					//RegistroView.this.setTitle("Login Gestor Eventos");
					emergente.dispose();
				}
			}
		});
	}

	/**
	 * Comprueba que los campos de registro están bien
	 */
	private boolean checkFields() {
		boolean salida = true;
		/* borrar todos los errores en pantalla */
		ocultarErrores();
		if (txtNombre.getText().trim().isEmpty()) {
			lblNombreError.setVisible(true);
			lblNombre.setForeground(Color.RED);
			txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (txtApellidos.getText().trim().isEmpty()) {
			lblApellidosError.setVisible(true);
			lblApellidos.setForeground(Color.RED);
			txtApellidos.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (txtMovil.getText().trim().isEmpty()) {
			lblMovilError.setVisible(true);
			lblMovil.setForeground(Color.RED);
			txtMovil.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		String password = new String(txtPassword.getPassword());
		String password2 = new String(txtPasswordChk.getPassword());
		String imagenRuta;
		if(imagen != null) {
			imagenRuta = new String(UtilsGui.getRutaResourceFromFile(imagen));
		}else {imagenRuta = "";}
		
		if (password.isEmpty()) {
			lblPasswordError.setText("El password no puede estar vacio");
			lblPasswordError.setVisible(true);
			lblPassword.setForeground(Color.RED);
			txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		} 
		if (password2.isEmpty()) {
			lblPasswordError.setText("El password no puede estar vacio");
			lblPasswordError.setVisible(true);
			lblPasswordChk.setForeground(Color.RED);
			txtPasswordChk.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		} 
		if (!password.equals(password2)) {
			lblPasswordError.setText("Los dos passwords no coinciden");
			lblPasswordError.setVisible(true);
			lblPassword.setForeground(Color.RED);
			lblPasswordChk.setForeground(Color.RED);
			txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			txtPasswordChk.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		/* Comprobar que no exista otro usuario con igual login */
		if (!lblMovilError.getText().isEmpty() && Controlador.INSTANCE.esUsuarioRegistrado(txtMovil.getText())) {
			lblMovilError.setText("Ya existe ese usuario");
			lblMovilError.setVisible(true);
			lblMovil.setForeground(Color.RED);
			txtMovil.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (imagenRuta == "") {
			lblImagenError = new JLabel("Se ha de selecccionar una imagen");
			lblImagenError.setVisible(true);
			btnImagen.setForeground(Color.RED);
			salida = false;
		} 
		if (errorImg) {
			lblImagenError = new JLabel("No es una imagen válida");
			lblImagenError.setVisible(true);
			btnImagen.setForeground(Color.RED);
			txtImagen.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
			
		}

		this.revalidate();
		this.pack();
		
		return salida;
	}

	/**
	 * Oculta todos los errores que pueda haber en la pantalla
	 */
	private void ocultarErrores() {
		lblNombreError.setVisible(false);
		lblApellidosError.setVisible(false);
		lblMovilError.setVisible(false);
		lblBioError.setVisible(false);
		lblPasswordError.setVisible(false);
		
		Border border = new JTextField().getBorder();
		txtNombre.setBorder(border);
		txtApellidos.setBorder(border);
		txtMovil.setBorder(border);
		txtBio.setBorder(border);
		txtPassword.setBorder(border);
		txtPasswordChk.setBorder(border);
		txtPassword.setBorder(border);
		txtPasswordChk.setBorder(border);
		txtFechaNacimiento.setBorder(border);
		
		lblNombre.setForeground(Color.BLACK);
		lblApellidos.setForeground(Color.BLACK);
		lblMovil.setForeground(Color.BLACK);
		lblPassword.setForeground(Color.BLACK);
		lblBio.setForeground(Color.BLACK);
		lblPasswordChk.setForeground(Color.BLACK);
		lblFechaNacimiento.setForeground(Color.BLACK);
	}

	/**
	 * Fija el tamaño de un componente
	 */
	private void fixedSize(JComponent o, int x, int y) {
		Dimension d = new Dimension(x, y);
		o.setMinimumSize(d);
		o.setMaximumSize(d);
		o.setPreferredSize(d);
	}
	
	private void actualizarImagen() {
		if (imagen != null) {
	        ImageIcon icono = new ImageIcon(UtilsGui.getRutaResourceFromFile(imagen));
	        Image imgEscalada = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Ajusta el tamaño
	        lblImg.setOpaque(true);
	        lblImg.setMinimumSize(new Dimension(240, 240));
			lblImg.setMaximumSize(new Dimension(240, 240));
	        lblImg.setIcon(new ImageIcon(imgEscalada));
	    } else {
	        lblImg.setIcon(null); // Si no hay imagen, quita el ícono
	    }
	    panelCampoImg.revalidate();
	    panelCampoImg.repaint();
	    
	}
	
	private boolean isValidImageUrl(String urlText) {
        try {
            URL url = new URL(urlText);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000); // Tiempo de espera
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();

            return responseCode == HttpURLConnection.HTTP_OK && contentType.startsWith("image/");
        } catch (Exception e) {
            return false; // Si hay un error, no es válida
        }
    }
	
}