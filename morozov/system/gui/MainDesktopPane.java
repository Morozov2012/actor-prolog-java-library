// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.run.*;

import javax.swing.JDesktopPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.lang.reflect.InvocationTargetException;

public class MainDesktopPane extends JDesktopPane implements MouseListener, ActionListener {
	//
	protected StaticContext staticContext;
	protected Dimension desktopSize;
	protected JPopupMenu popup;
	//
	public MainDesktopPane(StaticContext context) {
		staticContext= context;
		popup= DesktopUtils.installStandardPopupMenu(this);
		addMouseListener(this);
	}
	//
	public void safelyAdd(final Component comp) {
		if (SwingUtilities.isEventDispatchThread()) {
			add(comp);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						add(comp);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyAdd(final Component comp, final Object constraints) {
		if (SwingUtilities.isEventDispatchThread()) {
			add(comp,constraints);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						add(comp,constraints);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyAdd(final Component comp, final int index) {
		if (SwingUtilities.isEventDispatchThread()) {
			add(comp,index);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						add(comp,index);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	@Override
	public void doLayout() {
		super.doLayout();
		boolean enableResizingOfComponents= false;
		if (desktopSize==null) {
			desktopSize= getSize(null);
			enableResizingOfComponents= true;
		} else {
			double oldWidth= desktopSize.getWidth();
			double oldHeight= desktopSize.getHeight();
			desktopSize= getSize(desktopSize);
			if (	oldWidth != desktopSize.getWidth() ||
				oldHeight != desktopSize.getHeight()) {
				enableResizingOfComponents= true;
			}
		};
		if (enableResizingOfComponents) {
			DesktopUtils.restoreFrames(staticContext);
		}
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
		mousePressedOrReleased(event);
	}
	//
	protected void mousePressedOrReleased(MouseEvent event) {
		maybeShowPopup(event);
	}
	protected void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(),e.getX(),e.getY());
		}
	}
	//
	@Override
	public void actionPerformed(ActionEvent event) {
		DesktopUtils.actionPerformed(event,staticContext);
	}
}
