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
	@Override
	public void initiate(StaticContext context) {
		staticContext= context;
	}
	//
	@Override
	public Window getWindow() {
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void addToDesktopIfNecessary(StaticContext context) {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(staticContext);
		desktop.safelyAdd(this,DesktopUtils.DIALOG_LAYER);
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
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	public void setLocationByPlatform(boolean locationByPlatform) {
	}
	@Override
	public Dimension getRealMinimumSize() {
		return getUI().getMinimumSize(this);
	}
	@Override
	public Dimension getRealPreferredSize() {
		return getUI().getPreferredSize(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyPositionMainPanel() {
		dialog.safelyPositionMainPanel();
	}
	//
	@Override
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
	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		// Invoked when an internal frame is activated.
	}
	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		// Invoked when an internal frame has been closed.
	}
	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		// Invoked when an internal frame is in the process
		// of being closed.
		dialog.sendTheWindowClosingOrWindowClosedMessage();
	}
	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// Invoked when an internal frame is de-activated.
	}
	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		// Invoked when an internal frame is de-iconified.
	}
	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		// Invoked when an internal frame is iconified.
	}
	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		// Invoked when a internal frame has been opened.
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
				try {
					if (isMaximum()) {
						setMaximum(false);
					} else {
						setMaximum(true);
					}
				} catch (PropertyVetoException e) {
				}
			}
		}
	}
}
