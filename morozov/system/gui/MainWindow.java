// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.run.*;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;

public class MainWindow extends JFrame {
	MainWindow(StaticContext context) {
		GraphicsEnvironment env= GraphicsEnvironment.getLocalGraphicsEnvironment();
		// Rectangle bounds= env.getMaximumWindowBounds();
		GraphicsDevice device= env.getDefaultScreenDevice();
		GraphicsConfiguration gc= device.getDefaultConfiguration();
		Rectangle bounds= gc.getBounds();
		setSize(bounds.width,bounds.height);
		MainDesktopPane desktop= new MainDesktopPane(context);
		StaticDesktopAttributes.setDesktopPane(desktop,context);
		setContentPane(desktop);
		boolean exitOnClose= StaticDesktopAttributes.retrieveExitOnClose(context);
		try {
			if (exitOnClose) {
				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			} else {
				// setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}
		} catch (SecurityException e) {
			// setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}
}
