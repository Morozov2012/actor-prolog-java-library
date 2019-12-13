// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.special;

import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;

public class SpecialUtils {
	public static void centre(JDialog dialog) {
		Dimension d1= dialog.getSize();
		GraphicsConfiguration gc= dialog.getGraphicsConfiguration();
		Rectangle bounds= gc.getBounds();
		int x1= Math.max(0,(bounds.width-d1.width)/2);
		int y1= Math.max(0,(bounds.height-d1.height)/2);
		dialog.setBounds(bounds.x+x1,bounds.y+y1,d1.width,d1.height);
	}
}
