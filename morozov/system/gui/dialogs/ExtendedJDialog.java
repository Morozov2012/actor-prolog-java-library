// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.classes.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class ExtendedJDialog
		extends JDialog
		implements DialogOperations, MouseListener {
	//
	protected AbstractDialog dialog;
	protected StaticContext staticContext;
	protected AtomicBoolean isMaximum= new AtomicBoolean(false);
	protected AtomicReference<Point> restoredLocation= new AtomicReference<Point>(new Point());
	protected AtomicReference<Dimension> restoredSize= new AtomicReference<Dimension>(new Dimension());
	//
	public ExtendedJDialog(AbstractDialog d, Window w, ModalityType type) {
		super(w,type);
		// super(null,type);
		dialog= d;
		addMouseListener(this);
	}
	//
	public void initiate(StaticContext context) {
		staticContext= context;
	}
	//
	public Point computePosition(AtomicReference<ExtendedCoordinates> actualCoordinates) throws UseDefaultLocation {
		//
		Dimension initialSize= getSize();
		//
		int initialWidth= initialSize.width;
		int initialHeight= initialSize.height;
		//
		int x= 0;
		int y= 0;
		//
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		//
		Rectangle bounds= computeParentLayoutSize();
		ExtendedCoordinates actualPoint= actualCoordinates.get();
		x= DialogUtils.calculateRealCoordinate(actualPoint.x,bounds.x,bounds.width,gridX,initialWidth);
		y= DialogUtils.calculateRealCoordinate(actualPoint.y,bounds.y,bounds.height,gridY,initialHeight);
		//
		return new Point(x,y);
	}
	public Rectangle computeParentLayoutSize() {
		GraphicsEnvironment env= GraphicsEnvironment.getLocalGraphicsEnvironment();
		// Rectangle bounds= env.getMaximumWindowBounds();
		GraphicsDevice device= env.getDefaultScreenDevice();
		GraphicsConfiguration gc= device.getDefaultConfiguration();
		Rectangle bounds= gc.getBounds();
		// return new Dimension(bounds.width,bounds.height);
		return bounds;
	}
	//
	public void setClosable(boolean b) {
	}
	public void setMaximizable(boolean b) {
	}
	public void setIconifiable(boolean b) {
	}
	public void safelyMaximize() {
	}
	public void safelyMinimize() {
	}
	public void safelyRestore() {
	}
	public boolean safelyIsVisible() {
		return isVisible();
	}
	public boolean safelyIsHidden() {
		return !isVisible();
	}
	public boolean safelyIsMaximized() {
		return false;
	}
	public boolean safelyIsMinimized() {
		return false;
	}
	public boolean safelyIsRestored() {
		return true;
	}
	public void addToDesktop(JDesktopPane desktop) {
	}
	// public void setMaximum(boolean b) {
	// }
	//
	public Dimension getRealMinimumSize() {
		// return getUI().getMinimumSize(this);
		// return getMinimumSize();
		// return getLayout().minimumLayoutSize(this);
		// return new Dimension(0,0);
		Insets insets= getInsets();
		return new Dimension(insets.left+insets.right,insets.top+insets.bottom);
	}
	public Dimension getRealPreferredSize() {
		// return getUI().getPreferredSize(this);
		// return getPreferredSize();
		return getLayout().preferredLayoutSize(this);
	}
	//
	public void repaintParent() {
		Container container= getParent();
		if (container != null) {
			container.repaint();
		}
	}
	public void doSuperLayout() {
		super.doLayout();
	}
	//
	public void doLayout() {
		dialog.doLayout();
	}
	//
	public void mouseClicked(MouseEvent event) {
	}
	public void mouseEntered(MouseEvent event) {
	}
	public void mouseExited(MouseEvent event) {
	}
	public void mousePressed(MouseEvent event) {
		mousePressedOrReleased(event);
	}
	public void mouseReleased(MouseEvent event) {
		// mousePressedOrReleased(event);
	}
	//
	private void mousePressedOrReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			if (event.getClickCount() > 1) {
				MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
				if (desktop==null) {
					return;
				};
				synchronized(isMaximum) {
					if (isMaximum.get()) {
						safelySetLocationAndSize(restoredLocation.get(),restoredSize.get());
						isMaximum.set(false);
					} else {
						Point currentLocation= new Point();
						Dimension currentSize= new Dimension();
						DesktopUtils.safelyGetComponentLocation(this,currentLocation,currentSize);
						restoredLocation.set(currentLocation);
						restoredSize.set(currentSize);
						Point newLocation= new Point();
						Dimension newSize= new Dimension();
						DesktopUtils.safelyGetComponentLocationOnScreen(desktop,newLocation,newSize);
						safelySetLocationAndSize(newLocation,newSize);
						isMaximum.set(true);
					}
				}
			}
		}
	}
	public void safelySetLocationAndSize(final Point location, final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			setLocationAndSize(location,size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						setLocationAndSize(location,size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void setLocationAndSize(Point location, Dimension size) {
		setLocation(location);
		setSize(size);
	}
	//
	public boolean isMaximum() {
		return false;
	}
}
