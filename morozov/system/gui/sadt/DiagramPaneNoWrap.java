// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.sadt.signals.*;
import morozov.worlds.*;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.beans.PropertyVetoException;
import java.util.Map;

public class DiagramPaneNoWrap extends JPanel implements MouseListener, ActionListener {
	//
	protected JInternalFrame owner;
	protected String identifier;
	protected DiagramContent graph;
	protected DiagramColors diagramColors;
	protected StaticContext staticContext;
	protected Map<String,AbstractProcess> diagramComponents;
	protected Map<String,ComponentState> componentSuccess;
	protected JPopupMenu popup;
	//
	public DiagramPaneNoWrap(JInternalFrame frame, String diagramIdentifier, DiagramContent diagramGraph, DiagramColors colors, StaticContext context, Map<String,AbstractProcess> components, Map<String,ComponentState> componentState) {
		owner= frame;
		identifier= diagramIdentifier;
		graph= diagramGraph;
		diagramColors= colors;
		staticContext= context;
		diagramComponents= components;
		componentSuccess= componentState;
		popup= DesktopUtils.installStandardPopupMenu(this);
		addMouseListener(this);
	}
	//
	@Override
	public void paint(Graphics g0) {
		Dimension size= getSize();
		super.paint(g0);
		Graphics2D g2= (Graphics2D)g0;
		DesktopUtils.setRenderingHints(g2);
		graph.draw(g2,size,diagramColors,componentSuccess);
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
	private void mousePressedOrReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			try {
				long innerBlockNumber= graph.getInnerBlock(event.getPoint(),getSize());
				String fullIdentifier= DiagramUtils.computeFullIdentifier(identifier,innerBlockNumber);
				DiagramUtils.showDiagramPage(fullIdentifier,staticContext,diagramColors,diagramComponents,componentSuccess);
			} catch (NoBlockIsPointed exception) {
				if (event.getClickCount() >= 2) {
					if (!identifier.isEmpty()) {
						owner.setVisible(false);
					} else {
						try {
							owner.setIcon(true);
						} catch (PropertyVetoException e) {
						}
					}
				} else {
					maybeShowPopup(event);
				}
			}
		} else if (	event.getButton() == MouseEvent.BUTTON2 ||
				event.getButton() == MouseEvent.BUTTON3 ) {
			try {
				long innerBlockNumber= graph.getInnerBlock(event.getPoint(),getSize());
				String fullIdentifier= DiagramUtils.computeFullIdentifier(identifier,innerBlockNumber);
				DiagramUtils.showNote(this,fullIdentifier,staticContext);
			} catch (NoBlockIsPointed exception) {
				maybeShowPopup(event);
			}
		}
	}
	//
	private void maybeShowPopup(MouseEvent e) {
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
