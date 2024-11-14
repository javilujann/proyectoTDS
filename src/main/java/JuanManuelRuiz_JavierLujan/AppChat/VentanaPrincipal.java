package JuanManuelRuiz_JavierLujan.AppChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import tds.BubbleText;

//llega bien

public class VentanaPrincipal extends JFrame{
	private JPanel contenido;
	private JPanel cajaIzq, cajaDer, cajaArriba;
	private JPanel chat;
	
	
	public VentanaPrincipal() {
		BubbleText.noZoom();
		this.setSize(800,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contenido=(JPanel) this.getContentPane();
		
		cajaArriba=new JPanel(); cajaArriba.setBackground(Color.LIGHT_GRAY);
		cajaIzq=new JPanel(); 	cajaIzq.setBackground(Color.LIGHT_GRAY);
		cajaDer=new JPanel(); 	cajaDer.setBackground(Color.LIGHT_GRAY);
		cajaIzq.setBorder(new LineBorder(Color.BLACK));
		cajaArriba.setBorder(new LineBorder(Color.BLACK));
		cajaDer.setBorder(new LineBorder(Color.BLACK));
		fixSize(cajaArriba, 800, 100);
		fixSize(cajaIzq, 400, 500);
		fixSize(cajaDer, 400, 500);
		
		cajaIzq.setLayout(new BoxLayout(cajaIzq, BoxLayout.Y_AXIS));
		cajaDer.setLayout(new BoxLayout(cajaDer, BoxLayout.Y_AXIS));
		
		contenido.add(cajaArriba, BorderLayout.NORTH);
		contenido.add(cajaIzq, BorderLayout.WEST);
		contenido.add(cajaDer, BorderLayout.CENTER);
		
		
		JPanel cajaEmoji=new JPanel();
		fixSize(cajaEmoji, 240, 120);
		cajaEmoji.setBackground(Color.WHITE);
		
		JLabel e1=new JLabel();
		e1.setIcon(BubbleText.getEmoji(0));
		cajaEmoji.add(e1);
		cajaIzq.add(cajaEmoji);
		
		//-----------------------------------------------------------
		
		chat=new JPanel();
		chat.setBackground(new Color(150,140,140));
		chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
		//fixSize(chat, 300, 400);
		JScrollPane scroll= new JScrollPane(chat);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		fixSize(scroll, 360,400);
		cajaDer.add(scroll);
		
		
		setVisible(true);
		
		BubbleText m1=new BubbleText(chat, "Hola", Color.WHITE, "Antonio 16:07", BubbleText.SENT);
		chat.add(m1);
		BubbleText m2=new BubbleText(chat, "HOLA", Color.WHITE, "Antonio 16:08", BubbleText.SENT);
		chat.add(m2);
		BubbleText m3=new BubbleText(chat, "Sucia PUTA", new Color(200,210,200), "Cerezal 16:09", BubbleText.RECEIVED);
		chat.add(m3);
		BubbleText m4=new BubbleText(chat, "bciuewjbgipewuboivberiobvworeoiqbgioerbqvilberilbvierbviobeqriobvioerbviobeoivboeib el texto se ajusta solo", new Color(200,210,200), "Cerezal 16:09", BubbleText.RECEIVED);
		chat.add(m4);
		BubbleText m5=new BubbleText(chat, "bciuewjbgipewuboivberiobvworeoiqbgioerbqvilberilbvierbviobeqriobvioerbviobeoivboeib el texto se ajusta solo", new Color(200,210,200), "Cerezal 16:09", BubbleText.RECEIVED);
		chat.add(m5);
		BubbleText m6=new BubbleText(chat, BubbleText.MAXICONO, new Color(200,210,200), "Cerezal 16:09", BubbleText.RECEIVED, 12);
		chat.add(m6);
		BubbleText m7=new BubbleText(chat, 7, Color.WHITE, "Antonio 16:11", BubbleText.SENT, 12);
		chat.add(m7);
		
		chat.scrollRectToVisible(new Rectangle(0,chat.getHeight()+m7.getHeight(),1,1));
		repaint();
		revalidate();
		
	}
	
	
	private void fixSize(JComponent c, int x, int y) {
        c.setMinimumSize(new Dimension(x,y));
        c.setMaximumSize(new Dimension(x,y));
        c.setPreferredSize(new Dimension(x,y));
    }

}
