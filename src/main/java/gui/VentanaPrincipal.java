package gui;



import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

import controlador.Controlador;
import dominio.Contacto;



public class VentanaPrincipal {
	private JFrame frame;
    
    public VentanaPrincipal() {
        frame = new JFrame("AppChat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

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
    }
    
    public void mostrarVentana() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    private static JPanel createTopPanel() {
    		//Creas el panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.CYAN);
        UtilsGui.fixSize(topPanel, 800, 60);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        
        	// Placeholder para ActionListeners
        ActionListener actionListener = e -> {
            System.out.println("Botón presionado: " + e.getActionCommand());
        };
        
        	// Boton de busquedad de mensajes
        JButton searchButton = new JButton("Buscar Mensajes");
        searchButton.addActionListener(actionListener);
        topPanel.add(searchButton);
        
        	//Boton de seleccion de contacto
        JButton contactsButton = new JButton("Buscar Contactos");
        contactsButton.addActionListener(actionListener);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(contactsButton);
        
        	//Boton para añadir contactos
        JButton newContactsButton = new JButton("Añadir Contacto");
        newContactsButton.addActionListener(actionListener);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(newContactsButton);
        
        	//Boton para gestionar grupos
        JButton newGroupsButton = new JButton("Gestionar Grupos");
        newGroupsButton.addActionListener(actionListener);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(newGroupsButton);
        
        	//Boton para gestionar premium
        JButton premiumButton = new JButton("Premium");
        premiumButton.addActionListener(actionListener);
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
        lblimagen.setHorizontalTextPosition(SwingConstants.LEFT);
        lblimagen.setHorizontalAlignment(SwingConstants.RIGHT);

        	// Agregar espacio flexible antes del label
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(lblimagen);

        return topPanel;
    }


    private static JPanel createLeftPanel() {
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
        
        DefaultListModel<Elemento> model = new DefaultListModel<>();
        for (Contacto c : Controlador.INSTANCE.getContactos()) {
            model.addElement(new Elemento(c));
        }
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

    private static JPanel createRightPanel() {
        // Panel principal
        JPanel rightPanel = new JPanel(new BorderLayout());
        UtilsGui.fixSize(rightPanel, 300, 540);

        // Área de mensajes (no editable)
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
                messageArea.append("Yo: " + message + "\n");
                messageInput.setText("");
            }
        });

        // Agregar componentes al panel de entrada
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

}

