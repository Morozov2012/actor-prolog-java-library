// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.run.*;
import morozov.system.gui.*;

import javax.swing.JFrame;
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

public class ExtendedJFrame
		extends JFrame
		implements
			DialogOperations,
			WindowListener,
			MouseListener {
	//
	protected AbstractDialog dialog;
	protected StaticContext staticContext;
	protected AtomicBoolean isMaximum= new AtomicBoolean(false);
	protected AtomicReference<Point> restoredLocation= new AtomicReference<>(new Point());
	protected AtomicReference<Dimension> restoredSize= new AtomicReference<>(new Dimension());
	//
	public ExtendedJFrame(AbstractDialog d) {
		super();
		dialog= d;
		addWindowListener(this);
		addMouseListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void initiate(StaticContext context) {
		staticContext= context;
	}
	//
	@Override
	public Window getWindow() {
		return this;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setClosable(boolean b) {
	}
	@Override
	public void setMaximizable(boolean b) {
	}
	@Override
	public void setIconifiable(boolean b) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void addToDesktopIfNecessary(StaticContext context) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void safelySetVisible(boolean b) {
		DesktopUtils.safelySetVisible(b,this);
	}
	@Override
	public void safelyDispose() {
		DesktopUtils.safelyDispose(this);
	}
	@Override
	public void safelyMaximize() {
		DesktopUtils.safelyMaximize(this);
	}
	@Override
	public void safelyMinimize() {
		DesktopUtils.safelyMinimize(this);
	}
	@Override
	public void safelyRestore() {
		DesktopUtils.safelyRestore(this);
	}
	@Override
	public boolean safelyIsVisible() {
		return DesktopUtils.safelyIsVisible(this);
	}
	@Override
	public boolean safelyIsHidden() {
		return DesktopUtils.safelyIsHidden(this);
	}
	@Override
	public boolean safelyIsMaximized() {
		return DesktopUtils.safelyIsMaximized(this);
	}
	@Override
	public boolean safelyIsMinimized() {
		return DesktopUtils.safelyIsMinimized(this);
	}
	@Override
	public boolean safelyIsRestored() {
		return DesktopUtils.safelyIsRestored(this);
	}
	@Override
	public void safelySetAlwaysOnTop(boolean b) {
		DesktopUtils.safelySetAlwaysOnTop(b,this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
	public void repaintParent() {
		Container container= getParent();
		if (container != null) {
			container.repaint();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void doSuperLayout() {
		super.doLayout();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Dimension getRealMinimumSize() {
		Insets insets= getInsets();
		return new Dimension(insets.left+insets.right,insets.top+insets.bottom);
	}
	@Override
	public Dimension getRealPreferredSize() {
		return getLayout().preferredLayoutSize(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void doLayout() {
		dialog.doLayout();
	}
	//
	@Override
	public void windowActivated(WindowEvent e) {
		// Invoked when the Window is set to be
		// the active Window.
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// Invoked when a window has been closed as the
		// result of calling dispose on the window.
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// Invoked when the user attempts to close the
		// window from the window's system menu.
		dialog.sendTheWindowClosingOrWindowClosedMessage();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// Invoked when a Window is no longer the
		// active Window.
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// Invoked when a window is changed from a
		// minimized to a normal state.
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// Invoked when a window is changed from a normal
		// to a minimized state.
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// Invoked the first time a window is made visible.
	}
	//
	@Override
	public void mouseClicked(MouseEvent event) {
	}
	@Override
	public void mouseEntered(MouseEvent event) {
	}
	@Override
	public void mouseExited(MouseEvent event) {
	}
	@Override
	public void mousePressed(MouseEvent event) {
		mousePressedOrReleased(event);
	}
	@Override
	public void mouseReleased(MouseEvent event) {
	}
	//
	private void mousePressedOrReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			if (event.getClickCount() > 1) {
				if (safelyIsMaximized()) {
					safelyRestore();
				} else {
					safelyMaximize();
				}
			}
		}
	}
}
