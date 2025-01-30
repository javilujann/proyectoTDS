package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


import dominio.Mensaje;

@SuppressWarnings("serial")
public class MensajeCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {

    private JLabel tipoLabel;
    private JLabel contactoLabel;
    private JLabel contenidoLabel;
    private JLabel fechaLabel;
    private JLabel horaLabel;

    public MensajeCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        tipoLabel = new JLabel();
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contactoLabel = new JLabel();
        contactoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        topPanel.add(tipoLabel, BorderLayout.WEST);
        topPanel.add(contactoLabel, BorderLayout.EAST);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        fechaLabel = new JLabel();
        fechaLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        horaLabel = new JLabel();
        horaLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        bottomPanel.add(fechaLabel, BorderLayout.WEST);
        bottomPanel.add(horaLabel, BorderLayout.EAST);

        // Contenido
        contenidoLabel = new JLabel();
        contenidoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contenidoLabel.setHorizontalAlignment(JLabel.CENTER);

        // Añadir los paneles
        add(topPanel, BorderLayout.NORTH);
        add(contenidoLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)); // Añadir el borde inferior
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        // Configurar tipo de mensaje
        tipoLabel.setText(mensaje.getTipo().toString());

        // Configurar nombre del contacto
        if (mensaje.getContacto().getNombre() != null) {
            contactoLabel.setText(mensaje.getContacto().getNombre());
        } else {
            contactoLabel.setText("Sin contacto");
        }

        // Configurar contenido (texto o emoticón)
        //DADA LA NUEVA DEFINCIION DE LOS EMOTES CON ENTEROS, VER COMO ADAPTAR PARA QUE APAREZCA COMO ULTIMO MENSAJE
        
        /*if (mensaje.getEmoticon() >= null && !mensaje.getEmoticon().isEmpty()) {
            contenidoLabel.setIcon(new ImageIcon(mensaje.getEmoticon()));
            contenidoLabel.setText("");
        } else {
            contenidoLabel.setIcon(null);
            contenidoLabel.setText(mensaje.getTexto());
        }*/

        // Formatear fecha y hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        fechaLabel.setText(mensaje.getHora().toLocalDate().format(dateFormatter));
        horaLabel.setText(mensaje.getHora().toLocalTime().format(timeFormatter));
        
        return this;
    }
}
