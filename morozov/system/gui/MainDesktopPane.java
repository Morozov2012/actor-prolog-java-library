// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.classes.*;

import javax.swing.JDesktopPane;
import javax.swing.JPopupMenu;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainDesktopPane extends JDesktopPane implements MouseListener, ActionListener {
	private StaticContext staticContext;
	private Dimension desktopSize;
	private JPopupMenu popup;
	//
	public MainDesktopPane(StaticContext context) {
		staticContext= context;
		popup= DesktopUtils.installStandardPopupMenu(this);
		addMouseListener(this);
	}
	//
	public void doLayout() {
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
		};
		super.doLayout();
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
		mousePressedOrReleased(event);
	}
	//
	private void mousePressedOrReleased(MouseEvent event) {
		maybeShowPopup(event);
	}
	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(),e.getX(),e.getY());
		}
	}
	//
	public void actionPerformed(ActionEvent event) {
		DesktopUtils.actionPerformed(event,staticContext);
	}
}
