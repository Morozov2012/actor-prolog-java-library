// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.classes.*;

import javax.swing.JFrame;

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
		try {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (SecurityException e) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
}
