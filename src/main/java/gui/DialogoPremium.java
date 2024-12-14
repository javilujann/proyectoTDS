package gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class DialogoPremium extends JDialog{
	
	public DialogoPremium(JFrame owner, boolean isPremium) {
		super(owner, "Premium", true);
		if(!isPremium) initializePayWindow();
		else initializePremiumFuncionality();
	}
	
	private void initializePayWindow() {
		//chat gpt
	}
	
	private void initializePremiumFuncionality() {
		//chat gpt
	}
}
