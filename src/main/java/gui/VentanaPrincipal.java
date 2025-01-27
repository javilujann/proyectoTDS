package gui;

import javax.swing.*;

import java.awt.*;

import controlador.Controlador;
import dominio.Contacto;

public class VentanaPrincipal {
	private JFrame frame;
    
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
        
        JSplitPane separador = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        separador.setDividerLocation(350); // Posición inicial del divisor
        //separador.setResizeWeight(0.5); // Relación inicial de distribución del espacio
        //separador.setOneTouchExpandable(true); // Botones para contraer/expandir paneles
        separador.setContinuousLayout(true); // Actualiza en tiempo real al mover el divisor

        // Establecer tamaños mínimos para cada panel
        leftPanel.setMinimumSize(new Dimension(200, 0));
        rightPanel.setMinimumSize(new Dimension(250, 0));

        // Agregar el JSplitPane al marco
        frame.add(separador);

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
        newGroupsButton.addActionListener(e -> showMenu(newGroupsButton));
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

    private JPanel createRightPanel() {
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
                enviarMensaje(messageArea, message);
                messageInput.setText("");
            }
        });

        // Agregar componentes al panel de entrada
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        return rightPanel;
    }
    
    private void showMenu(JButton btn) {
        // Crear el menú contextual
        JPopupMenu popupMenu = new JPopupMenu();

        // Opción "Crear Grupo"
        JMenuItem crearGrupoItem = new JMenuItem("Crear Grupo");
        crearGrupoItem.addActionListener(e -> {
            String nombreGrupo = JOptionPane.showInputDialog(frame, "Introduzca el nombre del grupo:");
            if (nombreGrupo != null && !nombreGrupo.trim().isEmpty()) {
                // Llamar a la ventana de gestión de miembros
                openGestionarMiembros("Crear Grupo", nombreGrupo);
            }
        });
        popupMenu.add(crearGrupoItem);

        // Opción "Modificar Grupo"
        JMenuItem modificarGrupoItem = new JMenuItem("Modificar Grupo");
        modificarGrupoItem.addActionListener(e -> {
            // Obtener la lista de grupos existentes (ejemplo)
            String[] grupos = {"Grupo 1", "Grupo 2", "Grupo 3"}; // Esto debería ser dinámico en tu caso
            String grupoSeleccionado = (String) JOptionPane.showInputDialog(frame, "Seleccione un grupo:",
                    "Modificar Grupo", JOptionPane.QUESTION_MESSAGE, null, grupos, grupos[0]);

            if (grupoSeleccionado != null) {
                // Llamar a la ventana de gestión de miembros para modificar
                openGestionarMiembros("Modificar Grupo", grupoSeleccionado);
            }
        });
        popupMenu.add(modificarGrupoItem);

        // Opción "Eliminar Grupo" (deshabilitada)
        JMenuItem eliminarGrupoItem = new JMenuItem("Eliminar Grupo");
        eliminarGrupoItem.setEnabled(false);  // Deshabilitada
        popupMenu.add(eliminarGrupoItem);

        // Mostrar el menú contextual
        popupMenu.show(btn, 0, btn.getHeight());
    }

    // Método para abrir la ventana de gestión de miembros
    private void openGestionarMiembros(String tipo, String grupo) {
    	DialogoGestionarGrupos dialogo = new DialogoGestionarGrupos(frame);
        dialogo.setLocationRelativeTo(frame);
        dialogo.setVisible(true);
    }
    
 

}

