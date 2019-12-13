// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.run.*;

import java.awt.GraphicsConfiguration;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ComponentListener;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;

public interface DialogOperations {
	//
	public void initiate(StaticContext context);
	public Window getWindow();
	//
	public void setDefaultCloseOperation(int operation);
	public void setClosable(boolean b);
	public void setResizable(boolean b);
	public void setMaximizable(boolean b);
	public void setIconifiable(boolean b);
	//
	public void pack();
	public Component add(Component comp);
	public void addComponentListener(ComponentListener l);
	public void addToDesktopIfNecessary(StaticContext context);
	//
	public void safelySetVisible(boolean b);
	public void safelyDispose();
	public void safelyMaximize();
	public void safelyMinimize();
	public void safelyRestore();
	public boolean safelyIsVisible();
	public boolean safelyIsHidden();
	public boolean safelyIsMaximized();
	public boolean safelyIsMinimized();
	public boolean safelyIsRestored();
	public void safelySetAlwaysOnTop(boolean b);
	//
	public Rectangle computeParentLayoutSize();
	public GraphicsConfiguration getGraphicsConfiguration();
	public boolean isShowing();
	public void toFront();
	//
	public void invalidate();
	public void validate();
	public void revalidate();
	public void repaint();
	public void repaintParent();
	//
	public void doSuperLayout();
	//
	public void setLocationByPlatform(boolean locationByPlatform);
	public void setLocation(Point p);
	public Point getLocation();
	public void setSize(Dimension d);
	public Dimension getSize();
	public Dimension getSize(Dimension rv);
	public Dimension getRealMinimumSize();
	public Dimension getRealPreferredSize();
	//
	public void setTitle(String title);
	public String getTitle();
	public void setBackground(Color c);
	public Color getBackground();
	public Color getForeground();
}
