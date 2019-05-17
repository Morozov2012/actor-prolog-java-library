// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.run.*;
import morozov.system.gui.*;

import javax.swing.JDialog;
import java.awt.Component;
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
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class ExtendedJDialog
		extends JDialog
		implements
			DialogOperations,
			WindowListener,
			MouseListener {
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
		addWindowListener(this);
		addMouseListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initiate(StaticContext context) {
		staticContext= context;
	}
	//
	public Window getWindow() {
		return this;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void addToDesktopIfNecessary(StaticContext context) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setClosable(boolean b) {
	}
	public void setMaximizable(boolean b) {
	}
	public void setIconifiable(boolean b) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelySetVisible(boolean b) {
		DesktopUtils.safelySetVisible(b,this);
	}
	public void safelyDispose() {
		DesktopUtils.safelyDispose(this);
	}
	public void safelyMaximize() {
	}
	public void safelyMinimize() {
	}
	public void safelyRestore() {
	}
	public boolean safelyIsVisible() {
		return DesktopUtils.safelyIsVisible(this);
	}
	public boolean safelyIsHidden() {
		return DesktopUtils.safelyIsHidden(this);
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
	public void safelySetAlwaysOnTop(boolean b) {
		DesktopUtils.safelySetAlwaysOnTop(b,this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Rectangle computeParentLayoutSize() {
		GraphicsEnvironment env= GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device= env.getDefaultScreenDevice();
		GraphicsConfiguration gc= device.getDefaultConfiguration();
		Rectangle bounds= gc.getBounds();
		return bounds;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void repaintParent() {
		Container container= getParent();
		if (container != null) {
			container.repaint();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void doSuperLayout() {
		super.doLayout();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Dimension getRealMinimumSize() {
		Insets insets= getInsets();
		return new Dimension(insets.left+insets.right,insets.top+insets.bottom);
	}
	public Dimension getRealPreferredSize() {
		return getLayout().preferredLayoutSize(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void doLayout() {
		dialog.doLayout();
	}
	//
	public void windowActivated(WindowEvent e) {
		// Invoked when the Window is set to be
		// the active Window.
	}
	public void windowClosed(WindowEvent e) {
		// Invoked when a window has been closed as the
		// result of calling dispose on the window.
	}
	public void windowClosing(WindowEvent e) {
		// Invoked when the user attempts to close the
		// window from the window's system menu.
		dialog.sendTheWindowClosingOrWindowClosedMessage();
	}
	public void windowDeactivated(WindowEvent e) {
		// Invoked when a Window is no longer the
		// active Window.
	}
	public void windowDeiconified(WindowEvent e) {
		// Invoked when a window is changed from a
		// minimized to a normal state.
	}
	public void windowIconified(WindowEvent e) {
		// Invoked when a window is changed from a normal
		// to a minimized state.
	}
	public void windowOpened(WindowEvent e) {
		// Invoked the first time a window is made visible.
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
				Component desktop= StaticDesktopAttributes.retrieveTopLevelWindowOrDesktopPane(staticContext);
				if (desktop==null) {
					return;
				};
				synchronized (isMaximum) {
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
	//
	public void safelySetLocationAndSize(final Point location, final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetLocationAndSize(location,size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetLocationAndSize(location,size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklySetLocationAndSize(Point location, Dimension size) {
		setLocation(location);
		setSize(size);
	}
	//
	// public boolean isMaximum() {
	//	return false;
	// }
}
