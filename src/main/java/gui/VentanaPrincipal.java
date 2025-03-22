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
	DefaultListModel<Elemento> model = new DefaultListModel<>(); 
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

        frame.setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();						//PANEL SUPERIOR (Botones JDialog, gestión de contactos y grupos, usuario)
        frame.add(topPanel, BorderLayout.NORTH);

        leftPanel = createLeftPanel();							//PANEL IZQUIERDO (Lista de chats, con contactos, grupos, último mensaje...)
        frame.add(leftPanel, BorderLayout.WEST);
        
        rightPanel = createRightPanel();						//PANEL DERECHO (Panel de chat)
        frame.add(rightPanel, BorderLayout.CENTER);

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
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.CYAN);
        UtilsGui.fixSize(topPanel, 800, 60);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
          
        	// Buscar Mensajes Button
        JButton searchButton = new JButton("Buscar Mensajes");
        searchButton.addActionListener(e -> {
        	DialogoBusquedaMensajes dialogo = new DialogoBusquedaMensajes(this);
        	dialogo.setVisible(true);
        });
        topPanel.add(searchButton);
        
        	// Buscar Contactos Button
        JButton contactsButton = new JButton("Buscar Contactos");
        contactsButton.addActionListener(e -> {
        	DialogoBuscarContacto dialogo = new DialogoBuscarContacto(this);
        	dialogo.setVisible(true);
        });
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(contactsButton);
        
        	// Añadir Contacto Button
        JButton newContactsButton = new JButton("Añadir Contacto");
        newContactsButton.addActionListener(e -> {
        	DialogoCrearContacto dialogo = new DialogoCrearContacto(this,null);
        	dialogo.setVisible(true);
        	SwingUtilities.invokeLater(() -> {
    			leftPanel.revalidate();
    			leftPanel.repaint();
    		});
        });
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(newContactsButton);
        
        	// Gestionar Grupos Button
        JButton newGroupsButton = new JButton("Gestionar Grupos");
        newGroupsButton.addActionListener(e -> showGroupMenu(newGroupsButton));
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(newGroupsButton);
        
        	// Premium Button
        JButton premiumButton = new JButton("Premium");
        premiumButton.addActionListener(e -> {
        	DialogoPremium dialogo = new DialogoPremium(this,Controlador.INSTANCE.getUsuarioActual().isPremium());
        	dialogo.setVisible(true);
        });
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(premiumButton);
        
        	// Etiqueta de User
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

        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(lblimagen);

        return topPanel;
    }


    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        UtilsGui.fixSize(leftPanel, 300, 540);
 
        JLabel encabezado = new JLabel("Mensajes", SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 16));
        encabezado.setForeground(Color.WHITE);
        encabezado.setOpaque(true);
        encabezado.setBackground(Color.DARK_GRAY);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(encabezado, BorderLayout.NORTH);

        // Lista de chats
        lista = new JList<>();
        cargarListaContactos();
        
        lista.setModel(model);
        lista.setCellRenderer(new ElementoListRenderer());
        
        	// Selección de chat
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Elemento seleccionado = lista.getSelectedValue();
                if (seleccionado != null) {
                	seleccionarContacto(seleccionado.getContacto());
                	SwingUtilities.invokeLater(() -> {
            			JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
            			verticalBar.setValue(verticalBar.getMaximum());
            		});
                }
            }
        });
        
        	// Agregar contactos no agregados previamente
        lista.addMouseListener(new MouseAdapter() {
     		@Override
     		public void mouseClicked(MouseEvent e) {
     			if (e.getClickCount() == 2) {
     				Elemento seleccionado = lista.getSelectedValue();
     				if(seleccionado != null) {
     					Contacto contacto = seleccionado.getContacto();
     					if (!contacto.isGroup() && ! ((ContactoIndividual) contacto).isAgregado() ) {
     						DialogoCrearContacto dialogo = new DialogoCrearContacto(VentanaPrincipal.this , (ContactoIndividual) contacto);
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
        JPanel rightPanel = new JPanel(new BorderLayout());
        UtilsGui.fixSize(rightPanel, 600, 540);

        // Area de mensajes
        messageArea = new JPanel();
        configurarAreaMensajes(messageArea);

        messageScroll = new JScrollPane(messageArea);
        messageScroll.setLayout(new ScrollPaneLayout());
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        rightPanel.add(messageScroll, BorderLayout.CENTER);

        // Panel de entrada (Campo de texto + Enviar Button + Emote Button)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField messageInput = new JTextField();
        messageInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> {
            String message = messageInput.getText().trim();
            if (!message.isEmpty() && seleccionado!=null) {
                enviarYRecibirMensaje(messageArea, message, -1, -1, BubbleText.SENT);									
                messageInput.setText("");
                SwingUtilities.invokeLater(() -> {
                    JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
                    verticalBar.setValue(verticalBar.getMaximum());
                });
                Controlador.INSTANCE.enviarYrecibirMensaje(seleccionado, message, -1);			
                actualizarListaContactos(seleccionado);
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
        	actualizarListaContactos(seleccionado);
        	}
        });
        
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(emoteButton, BorderLayout.WEST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        return rightPanel;
    }
    
    // Menú Gestionar Grupos Button
    private void showGroupMenu(JButton btn) {
        JPopupMenu popupMenu = new JPopupMenu();

        // Crear Grupo
        JMenuItem crearGrupoItem = new JMenuItem("Crear Grupo");
        crearGrupoItem.addActionListener(e -> {
        	DialogoGestionarGrupos dialogo = new DialogoGestionarGrupos(this,true);
            dialogo.setLocationRelativeTo(frame);
            dialogo.setVisible(true);
        });
        popupMenu.add(crearGrupoItem);

        // Modificar Grupo
        JMenuItem modificarGrupoItem = new JMenuItem("Modificar Grupo");
        modificarGrupoItem.addActionListener(e -> {
        	DialogoGestionarGrupos dialogo = new DialogoGestionarGrupos(this,false);
            dialogo.setVisible(true);
        });
        popupMenu.add(modificarGrupoItem);

        // Eliminar Grupo (deshabilitada)
        JMenuItem eliminarGrupoItem = new JMenuItem("Eliminar Grupo");
        eliminarGrupoItem.setEnabled(false);
        popupMenu.add(eliminarGrupoItem);

        popupMenu.show(btn, 0, btn.getHeight());
    }
    
    // Métodos para la gestión de la lista de chats del panel izquierdo:
    
   public void cargarListaContactos() {
        model.clear(); 
        for (Contacto c : Controlador.INSTANCE.getContactos()) {
            model.addElement(new Elemento(c)); 
        }
    }
    
   public void actualizarListaContactos(Contacto c) {
    	for (int i = 0; i < model.getSize(); i++) {
        	Elemento elemento = model.getElementAt(i);
        	if (elemento.getContacto().equals(c)) {
        		model.set(i, new Elemento(c)); 
            	return;
        	}
    	}
	}
    
	public void añadirListaContactos(Contacto nuevoContacto) {
		for (int i = 0; i < model.getSize(); i++) {
        	if (model.getElementAt(i).getContacto().equals(nuevoContacto)) {
            	return; 
        	}
    	}
		
    	model.addElement(new Elemento(nuevoContacto));
	}
	
	//Selección de un nuevo contacto, activando las consecuentes acciones en el panel de la derecha
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
    
    // Función encargada del envío y la recepción de mensajes en el chat
    
    private void enviarYRecibirMensaje(JPanel panelChat, String cuerpo, int emote, int tamaño, int tipo) {
    	BubbleText m;
    	String nombre;
    	
    	Optional<String> textoMensaje = Optional.of(cuerpo);
    	nombre = tipo == BubbleText.SENT  ? Controlador.INSTANCE.getUsuarioActual().getNombre() : seleccionado.getNombre();
    	
    	if(emote>=0) {
    		m = new BubbleText(panelChat, emote, Color.CYAN, nombre,tipo, tamaño);
    		
    	} else {
    		m = new BubbleText(panelChat, textoMensaje.get(), Color.CYAN, nombre, tipo);
    	}
    	
    	alturaAcumulada += m.getHeight();
    	UtilsGui.fixSize(panelChat, 550, Math.max(alturaAcumulada, alturaPredeterminada) + 10);
    	panelChat.add(m /*, BorderLayout.EAST */);
    	m.setVisible(true);

    	panelChat.revalidate();
    	panelChat.repaint();
    };
    
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