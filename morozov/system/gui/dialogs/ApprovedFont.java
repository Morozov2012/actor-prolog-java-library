// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import java.awt.Font;
import java.awt.Dimension;

public class ApprovedFont {
	public Font font;
	public Dimension preferredSize;
	public ApprovedFont(Font f, Dimension d) {
		font= f;
		preferredSize= d;
	}
}
