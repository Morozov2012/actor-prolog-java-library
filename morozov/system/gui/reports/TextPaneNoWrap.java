// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import morozov.classes.*;
import morozov.system.gui.*;

import javax.swing.JTextPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TextPaneNoWrap extends JTextPane implements MouseListener, ActionListener {
	protected Component parent;
	protected StaticContext staticContext;
	protected JPopupMenu popup;
	protected JMenuItem item_copy;
	public TextPaneNoWrap(StaticContext context) {
		setEditorKit(new NoWrapEditorKit());
		staticContext= context;
		popup= installTextPanePopupMenu(this);
		addMouseListener(this);
	}
	public void initiate(Component scrollPane) {
		parent= scrollPane;
	}
	//
	public JPopupMenu installTextPanePopupMenu(ActionListener panel) {
		JPopupMenu popup= new JPopupMenu();
		//
		JMenuItem item1= new JMenuItem("Cancel");
		item1.setMnemonic('C');
		item1.setDisplayedMnemonicIndex(0);
		item1.setActionCommand("Cancel");
		item1.addActionListener(panel);
		popup.add(item1);
		//
		popup.addSeparator();
		//
		JMenuItem item2= new JMenuItem("Copy");
		item2.setMnemonic('O');
		item2.setDisplayedMnemonicIndex(1);
		item2.setActionCommand("Copy");
		item2.addActionListener(panel);
		popup.add(item2);
		item_copy= item2;
		//
		JMenuItem item3= new JMenuItem("Select all");
		item3.setMnemonic('E');
		item3.setDisplayedMnemonicIndex(1);
		item3.setActionCommand("SelectAll");
		item3.addActionListener(panel);
		popup.add(item3);
		//
		popup.addSeparator();
		//
		JMenuItem item4= new JMenuItem("Clear");
		item4.setMnemonic('L');
		item4.setDisplayedMnemonicIndex(2);
		item4.setActionCommand("Clear");
		item4.addActionListener(panel);
		popup.add(item4);
		//
		popup.addSeparator();
		//
		popup= DesktopUtils.installStandardPopupMenu(panel,popup);
		return popup;
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
	protected void mousePressedOrReleased(MouseEvent event) {
		maybeShowPopup(event);
	}
	protected void maybeShowPopup(MouseEvent e) {
		String text= getSelectedText();
		if (text != null && text.length() > 0) {
			item_copy.setEnabled(true);
		} else {
			item_copy.setEnabled(false);
		};
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(),e.getX(),e.getY());
		}
	}
	//
	public void actionPerformed(ActionEvent event) {
		String name= event.getActionCommand();
		if (name.equals("Cancel")) {
		// } else if (name.equals("Cut")) {
		//	cut();
		} else if (name.equals("Copy")) {
			copy();
		// } else if (name.equals("Paste")) {
		//	paste();
		// } else if (name.equals("Delete")) {
		//	replaceSelection("");
		} else if (name.equals("SelectAll")) {
			selectAll();
		} else if (name.equals("Clear")) {
			setText("");
		} else {
			// StaticDesktopAttributes staticAttributes= StaticDesktopAttributes.retrieveStaticDesktopAttributes(staticContext);
			DesktopUtils.actionPerformed(event,staticContext);
		}
	}
}
