// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.classes.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;

import javax.swing.JDesktopPane;
import java.awt.Component;
import java.awt.event.ComponentListener;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.util.concurrent.atomic.AtomicReference;
import java.beans.PropertyVetoException;

public interface DialogOperations {
	public void initiate(StaticContext context);
	public Point computePosition(AtomicReference<ExtendedCoordinates> actualCoordinates) throws UseDefaultLocation;
	public Rectangle computeParentLayoutSize();
	public void setClosable(boolean b);
	public void setResizable(boolean b);
	public void setMaximizable(boolean b);
	public void setIconifiable(boolean b);
	public void setDefaultCloseOperation(int operation);
	public Component add(Component comp);
	public void addComponentListener(ComponentListener l);
	public void addToDesktop(JDesktopPane desktop);
	public void pack();
	public void setVisible(boolean b);
	public void dispose();
	public void safelyMaximize();
	public void safelyMinimize();
	public void safelyRestore();
	public boolean safelyIsMaximized();
	public boolean safelyIsMinimized();
	public boolean safelyIsRestored();
	// public boolean isVisible();
	public boolean isShowing();
	public void toFront();
	public void invalidate();
	public void validate();
	public void revalidate();
	public void repaint();
	public void repaintParent();
	public void doSuperLayout();
	public void setLocation(Point p);
	public Point getLocation();
	public void setLocationByPlatform(boolean locationByPlatform);
	public void setSize(Dimension d);
	public boolean isMaximum();
	public Dimension getSize();
	public Dimension getSize(Dimension rv);
	public Dimension getRealMinimumSize();
	public Dimension getRealPreferredSize();
	public void setTitle(String title);
	public String getTitle();
	public Color getForeground();
	public void setBackground(Color c);
	public Color getBackground();
}
