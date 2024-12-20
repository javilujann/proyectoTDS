package gui;

import javax.swing.*;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class DialogoPremium extends JDialog {

    public DialogoPremium(JFrame owner, boolean isPremium) {
        super(owner, "Premium", true);
        if (!isPremium) initializePayWindow();
        else initializePremiumFuncionality();
        setSize(400, 300);
        setLocationRelativeTo(owner);
    }

    private void initializePayWindow() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        
        
        int precio = Controlador.INSTANCE.getUsuarioActual().getCodigo();	//Cambiar a getPrecio, donde se aplica el Descuento
        JLabel label = new JLabel("Accede a Premium por solo " + precio +" al mes", JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        
        JButton payButton = new JButton("Pagar");
        UtilsGui.fixSize(payButton, 100, 30); 
        payButton.addActionListener(e -> {
        	JOptionPane.showMessageDialog(DialogoPremium.this,
                    "Se ha dado de alta con exito",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            getContentPane().removeAll(); 
            initializePremiumFuncionality(); 
            revalidate(); 
            repaint(); 
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel para centrar el botón
        buttonPanel.add(payButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        add(panel);
    }

    private void initializePremiumFuncionality() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Seleccionador de contactos
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BorderLayout());
        JLabel contactLabel = new JLabel("Selecciona un contacto:");
        JList<String> contactList = new JList<>(new String[]{"Contacto 1", "Contacto 2", "Contacto 3"});
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane contactScrollPane = new JScrollPane(contactList);

        contactPanel.add(contactLabel, BorderLayout.NORTH);
        contactPanel.add(contactScrollPane, BorderLayout.CENTER);

        // Botones en la parte inferior
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton createPdfButton = new JButton("Crear PDF");
        createPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 String selectedContact = contactList.getSelectedValue();
                JOptionPane.showMessageDialog(DialogoPremium.this,
                        "Creando PDF para: " + selectedContact,
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                // Aquí llamaría al controlador para generar el PDF
            }
        });

        JButton unsubscribeButton = new JButton("Darse de Baja");
        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Controlador.INSTANCE.bajaPremium();
                JOptionPane.showMessageDialog(DialogoPremium.this,
                        "Se ha dado de baja con exito",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
               
            }
        });

        buttonPanel.add(createPdfButton);
        buttonPanel.add(unsubscribeButton);

        // Añadir componentes al panel principal
        panel.add(contactPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }
}

