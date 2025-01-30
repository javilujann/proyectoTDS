package gui;

import javax.swing.*;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import controlador.Controlador;
import dominio.Contacto;
import dominio.TipoMensaje;
import tds.BubbleText;

public class VentanaPrincipal {
	private JFrame frame;
	DefaultListModel<Elemento> model = new DefaultListModel<>(); //Global para permitir actualizacion
	private boolean mensajesExceden = false;
    private int alturaAcumulada = 0;
    private Contacto seleccionado;
    
    
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
        JPanel leftPanel = createLeftPanel();
        frame.add(leftPanel, BorderLayout.WEST);
        
        // Right panel with chat messages
        JPanel rightPanel = createRightPanel();
        frame.add(rightPanel, BorderLayout.CENTER);
        
        //JSplitPane separador = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        // separador.setDividerLocation(350); // Posición inicial del divisor
        //separador.setResizeWeight(0.5); // Relación inicial de distribución del espacio
        //separador.setOneTouchExpandable(true); // Botones para contraer/expandir paneles
        //separador.setContinuousLayout(true); // Actualiza en tiempo real al mover el divisor

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
        // Agregar el JSplitPane al marco
        //frame.add(separador);

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
          
        	// Boton de busquedad de mensajes
        JButton searchButton = new JButton("Buscar Mensajes");
        searchButton.addActionListener(e -> {
        	DialogoBusquedaMensajes dialogo = new DialogoBusquedaMensajes(frame);
        	dialogo.setVisible(true);
        });
        topPanel.add(searchButton);
        
        	//Boton de seleccion de contacto
        JButton contactsButton = new JButton("Buscar Contactos");
        contactsButton.addActionListener(e -> {
        	DialogoBuscarContacto dialogo = new DialogoBuscarContacto(frame);
        	dialogo.setVisible(true);
        });
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(contactsButton);
        
        	//Boton para añadir contactos
        JButton newContactsButton = new JButton("Añadir Contacto");
        newContactsButton.addActionListener(e -> {
        	DialogoCrearContacto dialogo = new DialogoCrearContacto(frame);
        	dialogo.setVisible(true);
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
        JList<Elemento> lista = new JList<>();
        actualizarListaContactos();
        
        lista.setModel(model);
        lista.setCellRenderer(new ElementoListRenderer());
        
        	// Listener para selección
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Elemento seleccionado = lista.getSelectedValue();
                if (seleccionado != null) {
                    System.out.println("Seleccionado: " + seleccionado.getContacto());
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
        JPanel messageArea = new JPanel();
        messageArea.setLayout(new BoxLayout(messageArea,BoxLayout.Y_AXIS));
        messageArea.setPreferredSize(new Dimension(550, 400));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane messageScroll = new JScrollPane(messageArea);
        //messageScroll.setLayout(new BoxLayout(messageArea, BoxLayout.Y_AXIS));
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
            if (!message.isEmpty()) {
                enviarMensaje(messageArea, message);
                messageInput.setText("");
                SwingUtilities.invokeLater(() -> {
                    JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
                    verticalBar.setValue(verticalBar.getMaximum());
                });
            }
        });
        
        JButton emoteButton = new JButton(":)");
        emoteButton.addActionListener(e->{
        	int emote = ThreadLocalRandom.current().nextInt(0, BubbleText.MAXICONO+1);
        	enviarEmoticono(messageArea, emote, 18);
        	SwingUtilities.invokeLater(() -> {
                JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getMaximum());
            });
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
    
    private void enviarMensaje(JPanel panelChat, String cuerpo) {
    	BubbleText m = new BubbleText(panelChat, cuerpo, Color.CYAN, "Yo", BubbleText.SENT);
    	m.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	panelChat.add(m, BorderLayout.EAST);
    	panelChat.revalidate();
    	System.out.println(panelChat.getPreferredSize());
    	System.out.println(m.getHeight());
    	if(alturaAcumulada >= 400) {
    		panelChat.setPreferredSize(new Dimension(550, panelChat.getHeight()+m.getHeight()));
    	}else {
    		alturaAcumulada+=m.getHeight();
    		if(alturaAcumulada >= 400) {
    			panelChat.setPreferredSize(new Dimension(550, panelChat.getHeight()+alturaAcumulada-400));
    		}
    	}
    	m.setVisible(true);
    	panelChat.repaint();
    };
    
    private void enviarEmoticono(JPanel panelChat, int emote, int tamaño) {
    	BubbleText m = new BubbleText(panelChat, emote, Color.CYAN, "Yo", BubbleText.SENT, tamaño);
    	m.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	panelChat.add(m, BorderLayout.EAST);
    	panelChat.revalidate();
    	System.out.println(panelChat.getPreferredSize());
    	System.out.println(m.getHeight());
    	if(alturaAcumulada >= 400) {
    		panelChat.setPreferredSize(new Dimension(550, panelChat.getHeight()+m.getHeight()));
    	}else {
    		alturaAcumulada+=m.getHeight();
    		if(alturaAcumulada >= 400) {
    			panelChat.setPreferredSize(new Dimension(550, panelChat.getHeight()+alturaAcumulada-400));
    		}
    	}
    	m.setVisible(true);
    	panelChat.repaint();
    	
    }
    
    
    /*TO DO
     * ARREGLAR MessaegCellrenderer con la nueva definicion por enteros de los emotes
     * Ampliar los action listener de envío de mensajes para que también actualicen las listas de mensajes del emisor y el receptor
     * Funcion para cargar los mensajes del usuiario al entrar al chat y transformarlos en mensajes gráficos
     */
    
    
    
 

}