package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import controlador.Controlador;
import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Mensaje;
import dominio.TipoMensaje;
import tds.BubbleText;

public class VentanaPrincipal {
	private JFrame frame;
	DefaultListModel<Elemento> model = new DefaultListModel<>(); //Global para permitir actualizacion
    private int alturaAcumulada = 0;
    private static int alturaPredeterminada = 400;
    private Contacto seleccionado;
    private JPanel leftPanel, rightPanel, messageArea;
    private JScrollPane messageScroll;
    private JList<Elemento> lista;
    
    public VentanaPrincipal() {
        frame = new JFrame("AppChat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        // Main layout
        frame.setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = createTopPanel();
        frame.add(topPanel, BorderLayout.NORTH);

        // Left panel with contacts and last messages
        leftPanel = createLeftPanel();
        frame.add(leftPanel, BorderLayout.WEST);
        
        // Right panel with chat messages
        rightPanel = createRightPanel();
        frame.add(rightPanel, BorderLayout.CENTER);

        // Establecer tamaños mínimos para cada panel
        leftPanel.setMinimumSize(new Dimension(300, 540));
        leftPanel.setMaximumSize(new Dimension(300, 540));
        leftPanel.setPreferredSize(new Dimension(300, 540));
        leftPanel.setSize(new Dimension(300, 540));

        rightPanel.setMinimumSize(new Dimension(600, 540));
        rightPanel.setMaximumSize(new Dimension(600, 540));
        rightPanel.setPreferredSize(new Dimension(600, 540));
        rightPanel.setSize(new Dimension(600, 540));

        frame.setResizable(false);
       

    }
    
    public void mostrarVentana() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    private JPanel createTopPanel() {
    		//Creas el panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.CYAN);
        UtilsGui.fixSize(topPanel, 800, 60);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
          
        	// Boton de busquedas de mensajes
        JButton searchButton = new JButton("Buscar Mensajes");
        searchButton.addActionListener(e -> {
        	DialogoBusquedaMensajes dialogo = new DialogoBusquedaMensajes(frame);
        	dialogo.setVisible(true);
        });
        topPanel.add(searchButton);
        
        	//Boton de seleccion de contacto
        JButton contactsButton = new JButton("Buscar Contactos");
        contactsButton.addActionListener(e -> {
        	DialogoBuscarContacto dialogo = new DialogoBuscarContacto(this);
        	dialogo.setVisible(true);
        });
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(contactsButton);
        
        	//Boton para añadir contactos
        JButton newContactsButton = new JButton("Añadir Contacto");
        newContactsButton.addActionListener(e -> {
        	DialogoCrearContacto dialogo = new DialogoCrearContacto(frame,null);
        	dialogo.setVisible(true);
        	SwingUtilities.invokeLater(() -> {
    			leftPanel.revalidate();
    			leftPanel.repaint();
    		});
        });
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(newContactsButton);
        
        	//Boton para gestionar grupos
        JButton newGroupsButton = new JButton("Gestionar Grupos");
        newGroupsButton.addActionListener(e -> showGroupMenu(newGroupsButton));
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(newGroupsButton);
        
        	//Boton para gestionar premium
        JButton premiumButton = new JButton("Premium");
        premiumButton.addActionListener(e -> {
        	DialogoPremium dialogo = new DialogoPremium(frame,Controlador.INSTANCE.getUsuarioActual().isPremium());
        	dialogo.setVisible(true);
        });
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(premiumButton);

        
        	// Etiqueta de usuario
        JLabel lblimagen = new JLabel(Controlador.INSTANCE.getUsuarioActual().getNombre());
        lblimagen.setOpaque(true);
        lblimagen.setBackground(Color.CYAN);
        UtilsGui.fixSize(lblimagen, 100, 60);
        lblimagen.setFont(new Font("Arial", Font.BOLD, 16));

        Image imagenEscalada = Controlador.INSTANCE.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        lblimagen.setIcon(iconoEscalado);
        lblimagen.setIconTextGap(10);
        lblimagen.setPreferredSize(new Dimension(200,40));
        lblimagen.setHorizontalTextPosition(SwingConstants.LEFT);
        lblimagen.setHorizontalAlignment(SwingConstants.RIGHT);

        	// Agregar espacio flexible antes del label
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(lblimagen);

        return topPanel;
    }


    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        UtilsGui.fixSize(leftPanel, 300, 540);

        // Encabezado 
        JLabel encabezado = new JLabel("Mensajes", SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 16));
        encabezado.setForeground(Color.WHITE);
        encabezado.setOpaque(true);
        encabezado.setBackground(Color.DARK_GRAY);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(encabezado, BorderLayout.NORTH);

        // Lista 
        lista = new JList<>();
        actualizarListaContactos();
        
        lista.setModel(model);
        lista.setCellRenderer(new ElementoListRenderer());
        
        	// Listener para selección del chat
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Elemento seleccionado = lista.getSelectedValue();
                if (seleccionado != null) {
                	seleccionarContacto(seleccionado.getContacto());
                	SwingUtilities.invokeLater(() -> {
            			JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
            			verticalBar.setValue(verticalBar.getMaximum());
            		});//No basta con crear right panel cada vez, va a haber que llamar desde arriba
                }
            }
        });
        
        	// Listener para los no agregados
        lista.addMouseListener(new MouseAdapter() {
     		@Override
     		public void mouseClicked(MouseEvent e) {
     			if (e.getClickCount() == 2) {
     				Elemento seleccionado = lista.getSelectedValue();
     				if(seleccionado != null) {
     					Contacto contacto = seleccionado.getContacto();
     					if (!contacto.isGroup() && ! ((ContactoIndividual) contacto).isAgregado() ) {
     						DialogoCrearContacto dialogo = new DialogoCrearContacto(frame, (ContactoIndividual) contacto);
     						dialogo.setVisible(true);
     					}	
     				}
     			}
     		}
     	});

        // Scroll 
        JScrollPane scroll = new JScrollPane(lista);
        UtilsGui.fixSize(scroll, 300, 500);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        leftPanel.add(scroll, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        // Panel principal
        JPanel rightPanel = new JPanel(new BorderLayout());
        UtilsGui.fixSize(rightPanel, 600, 540);

        // Área de mensajes (no editable)
        messageArea = new JPanel();
        configurarAreaMensajes(messageArea);

        messageScroll = new JScrollPane(messageArea);
        messageScroll.setLayout(new ScrollPaneLayout());
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        rightPanel.add(messageScroll, BorderLayout.CENTER);

        // Panel de entrada (campo de texto + botón)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campo de texto con placeholder
        JTextField messageInput = new JTextField();
        messageInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Botón "Enviar" con acción
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> {
            String message = messageInput.getText().trim();
            if (!message.isEmpty() && seleccionado!=null) {
                enviarYRecibirMensaje(messageArea, message, -1, -1, BubbleText.SENT);									//Se envía el mensaje al chat de forma gráfica	
                messageInput.setText("");
                SwingUtilities.invokeLater(() -> {
                    JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
                    verticalBar.setValue(verticalBar.getMaximum());
                });
                Controlador.INSTANCE.enviarYrecibirMensaje(seleccionado, message, -1);			//Se envía el mensaje de forma real al contacto seleccionado
            }
        });
        
        JButton emoteButton = new JButton(":)");
        emoteButton.addActionListener(e->{
        	int emote = ThreadLocalRandom.current().nextInt(0, BubbleText.MAXICONO+1);
        	if (seleccionado!=null) {
        		enviarYRecibirMensaje(messageArea, "", emote, 18, BubbleText.SENT);
        		SwingUtilities.invokeLater(() -> {
        			JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
        			verticalBar.setValue(verticalBar.getMaximum());
        		});
        	Controlador.INSTANCE.enviarYrecibirMensaje(seleccionado, "", emote);
        	}
        });
        

        // Agregar componentes al panel de entrada
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(emoteButton, BorderLayout.WEST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        return rightPanel;
    }
    
  //Metodo para crear el menu contextual de la gestion de grupos
    private void showGroupMenu(JButton btn) {
        // Crear el menú contextual
        JPopupMenu popupMenu = new JPopupMenu();

        // Opción "Crear Grupo"
        JMenuItem crearGrupoItem = new JMenuItem("Crear Grupo");
        crearGrupoItem.addActionListener(e -> {
        	DialogoGestionarGrupos dialogo = new DialogoGestionarGrupos(frame,true);
            dialogo.setLocationRelativeTo(frame);
            dialogo.setVisible(true);
            actualizarListaContactos();
        });
        popupMenu.add(crearGrupoItem);

        // Opción "Modificar Grupo"
        JMenuItem modificarGrupoItem = new JMenuItem("Modificar Grupo");
        modificarGrupoItem.addActionListener(e -> {
        	DialogoGestionarGrupos dialogo = new DialogoGestionarGrupos(frame,false);
            dialogo.setVisible(true);
        });
        popupMenu.add(modificarGrupoItem);

        // Opción "Eliminar Grupo" (deshabilitada)
        JMenuItem eliminarGrupoItem = new JMenuItem("Eliminar Grupo");
        eliminarGrupoItem.setEnabled(false);  // Deshabilitada
        popupMenu.add(eliminarGrupoItem);

        // Mostrar el menú contextual
        popupMenu.show(btn, 0, btn.getHeight());
    }
    
    	//Metodo para actualizar la lista del panel izquierdo
    public void actualizarListaContactos() {
        model.clear(); 
        for (Contacto c : Controlador.INSTANCE.getContactos()) {
            model.addElement(new Elemento(c)); 
        }
    }
    
    /*
    public void actualizarContacto(Contacto c) {
    	for (int i = 0; i < model.getSize(); i++) {
        	Elemento elemento = model.getElementAt(i);
        	if (elemento.getContacto().equals(c)) {
            	elemento.repaint(); // Redibuja solo este elemento
            	return;
        	}
    	}
	}
	
	Posible mejora, tambien una para agreagar y llmar a la otra cargar por ejemplo
	
	 public void agregarContacto(Contacto nuevoContacto) {
    	for (int i = 0; i < model.getSize(); i++) {
        	if (model.getElementAt(i).getContacto().equals(nuevoContacto)) {
            	return; // El contacto ya está en la lista, no lo añadimos
        	}
    	}
    	model.addElement(new Elemento(nuevoContacto));
	}
    */
    
    private void enviarYRecibirMensaje(JPanel panelChat, String cuerpo, int emote, int tamaño, int tipo) {
    	BubbleText m;
    	Optional<String> textoMensaje = Optional.of(cuerpo);
    	
    	if(emote>=0) {
    		m = new BubbleText(panelChat, emote, Color.CYAN, "Yo",tipo, tamaño);
    		
    	} else {
    		m = new BubbleText(panelChat, textoMensaje.get(), Color.CYAN, "Yo", tipo);
    	}
    	
    	alturaAcumulada += m.getHeight();
    	UtilsGui.fixSize(panelChat, 550, Math.max(alturaAcumulada, alturaPredeterminada) + 10);
    	panelChat.add(m /*, BorderLayout.EAST */);
    	m.setVisible(true);

    	panelChat.revalidate();
    	panelChat.repaint();
    };
    
    //Función para seleccionar un nuevo contacto, activando las consecuentes acciones en el panel de la derecha
    private void seleccionarContacto(Contacto contacto) {
    	
    	seleccionado = contacto;
    	
    	messageArea = new JPanel();
    	configurarAreaMensajes(messageArea);
    	
    	messageScroll.setViewportView(messageArea);
    	messageScroll.revalidate();
    	messageScroll.repaint();
    	
    	alturaAcumulada = 0;
    	
    	for (Mensaje m:contacto.getListaMensajes()) {
    		if((m.getTexto()!=null && !m.getTexto().isEmpty()) ^ m.getEmoticon()>=0) {
    			int tipo = m.getTipo().equals(TipoMensaje.SENT) ? BubbleText.SENT : BubbleText.RECEIVED;
    			enviarYRecibirMensaje(messageArea, m.getTexto(), m.getEmoticon(), 18, tipo);
    			messageArea.revalidate();messageArea.repaint();
    		}
    	}

    	return;
    }
    
    private void configurarAreaMensajes(JPanel messageArea) {
    	messageArea.setLayout(new BoxLayout(messageArea,BoxLayout.Y_AXIS));
    	UtilsGui.fixSize(messageArea, 550, Math.max(alturaAcumulada, alturaPredeterminada));
    	messageArea.setSize(new Dimension(550, Math.max(alturaAcumulada, 400)));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public void setIndex(int index) {
    	lista.setSelectedIndex(index);
    }
    

}