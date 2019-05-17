// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.run.*;
import morozov.system.gui.*;

import javax.swing.JInternalFrame;
import java.awt.Window;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Container;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;

public class DialogJInternalFrame
		extends JInternalFrame
		implements
			DialogOperations,
			InternalFrameListener,
			MouseListener {
	//
	protected AbstractDialog dialog;
	protected StaticContext staticContext;
	protected AtomicBoolean insideDoLayout= new AtomicBoolean(false);
	//
	public DialogJInternalFrame(AbstractDialog d) {
		dialog= d;
		putClientProperty("JInternalFrame.frameType","optionDialog");
		addInternalFrameListener(this);
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
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void addToDesktopIfNecessary(StaticContext context) {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(staticContext);
		desktop.safelyAdd(this,DesktopUtils.DIALOG_LAYER);
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
		DesktopUtils.safelyMaximize(this);
	}
	public void safelyMinimize() {
		DesktopUtils.safelyMinimize(this);
	}
	public void safelyRestore() {
		DesktopUtils.safelyRestore(this);
	}
	public boolean safelyIsVisible() {
		return DesktopUtils.safelyIsVisible(this);
	}
	public boolean safelyIsHidden() {
		return DesktopUtils.safelyIsHidden(this);
	}
	public boolean safelyIsMaximized() {
		return DesktopUtils.safelyIsMaximized(this);
	}
	public boolean safelyIsMinimized() {
		return DesktopUtils.safelyIsMinimized(this);
	}
	public boolean safelyIsRestored() {
		return DesktopUtils.safelyIsRestored(this);
	}
	public void safelySetAlwaysOnTop(boolean b) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Rectangle computeParentLayoutSize() {
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(dialog.staticContext);
		//
		Dimension parentDimension= new Dimension();
		desktop.getSize(parentDimension);
		//
		int parentWidth= parentDimension.width;
		int parentHeight= parentDimension.height;
		//
		Dimension minimumParentSize= desktop.getUI().getMinimumSize(this);
		//
		int parentLayoutWidth= parentWidth - minimumParentSize.width;
		int parentLayoutHeight= parentHeight - minimumParentSize.height;
		//
		return new Rectangle(0,0,parentLayoutWidth,parentLayoutHeight);
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
	public void setLocationByPlatform(boolean locationByPlatform) {
	}
	public Dimension getRealMinimumSize() {
		return getUI().getMinimumSize(this);
	}
	public Dimension getRealPreferredSize() {
		return getUI().getPreferredSize(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyPositionMainPanel() {	// To be called from DesktopUtils
		dialog.safelyPositionMainPanel();
	}
	//
	public void doLayout() {
		if (insideDoLayout.compareAndSet(false,true)) {
			try {
				dialog.doLayout();
			} finally {
				insideDoLayout.set(false);
			}
		}
	}
	//
	public void internalFrameActivated(InternalFrameEvent e) {
		// Invoked when an internal frame is activated.
	}
	public void internalFrameClosed(InternalFrameEvent e) {
		// Invoked when an internal frame has been closed.
	}
	public void internalFrameClosing(InternalFrameEvent e) {
		// Invoked when an internal frame is in the process
		// of being closed.
		dialog.sendTheWindowClosingOrWindowClosedMessage();
	}
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// Invoked when an internal frame is de-activated.
	}
	public void internalFrameDeiconified(InternalFrameEvent e) {
		// Invoked when an internal frame is de-iconified.
	}
	public void internalFrameIconified(InternalFrameEvent e) {
		// Invoked when an internal frame is iconified.
	}
	public void internalFrameOpened(InternalFrameEvent e) {
		// Invoked when a internal frame has been opened.
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
				try {
					if (isMaximum()) {
						setMaximum(false);
						// safelyRestore();
					} else {
						setMaximum(true);
						// safelyMaximize();
					}
				} catch (PropertyVetoException e) {
				}
			}
		}
	}
}
