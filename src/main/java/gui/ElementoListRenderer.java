package gui;

//Para completar
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

@SuppressWarnings("serial")
public class ElementoListRenderer extends DefaultListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList<?> list, 
						Object value, int index, boolean isSelected, 
						boolean cellHasFocus) {
		
		if (value!=null && value instanceof Elemento) {
			Elemento ele=(Elemento) value;
			if (isSelected) {
				ele.setBackground(Color.CYAN);
			} else ele.setBackground(Color.LIGHT_GRAY); //list.getBackground()
			
			return ele;
		} 
		else return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 

	}
}

