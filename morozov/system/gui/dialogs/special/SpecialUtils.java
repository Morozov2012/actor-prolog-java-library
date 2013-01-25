// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.special;

import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class SpecialUtils {
	public static void centre(JDialog dialog) {
		Dimension d1= dialog.getSize();
		GraphicsEnvironment env= GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds= env.getMaximumWindowBounds();
		// The rectangle object contains the width and height you desire...
		// setBounds(0,0,bounds.width,bounds.height);
		// int x= Math.max((d2.width-d1.width)/2, 0);
		// int y= Math.max((d2.height-d1.height)/2, 0);
		// int x= 10;
		// int y= 10;
		int x= Math.max((bounds.width-d1.width)/2, 0);
		int y= Math.max((bounds.height-d1.height)/2, 0);
		dialog.setBounds(x,y,d1.width,d1.height);
	}
}
