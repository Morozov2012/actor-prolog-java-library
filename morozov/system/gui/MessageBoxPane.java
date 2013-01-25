// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import javax.swing.JOptionPane;

public class MessageBoxPane extends JOptionPane {
	public MessageBoxPane(Object message, int messageType) {
		super(message,messageType);
	}
	public int getMaxCharactersPerLineCount() {
		return 90;
	}
}

