// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import javax.swing.JTextPane;

public class TextPaneNoWrap extends JTextPane {
	public TextPaneNoWrap() {
		setEditorKit(new NoWrapEditorKit());
	}
}
