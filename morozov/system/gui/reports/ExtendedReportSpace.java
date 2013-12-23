// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import target.*;

import morozov.classes.*;
import morozov.system.gui.*;

import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ExtendedReportSpace extends JScrollPane implements MouseListener, ActionListener {
	//
	public TextPaneNoWrap panel;
	public ReportSpaceAttributes attributes;
	//
	protected JPopupMenu popup;
	protected JMenuItem item_copy;
	//
	public ExtendedReportSpace(TextPaneNoWrap p) {
		super(p);
		panel= p;
		panel.setEditable(false);
		panel.setAutoscrolls(true);
	}
	//
	public void setAttributes(ReportSpaceAttributes a) {
		attributes= a;
	}
	//public void setStaticContext(StaticContext context) {
	//	attributes.setStaticContext(context);
	//}
	//
	public void setFont(Font font) {
		// super.setFont(font);
		setPanelFontSize(font.getSize(),false);
	}
	//
	public void setPanelFontSize(int size, boolean keepThisValue) {
		if (attributes != null) {
			if (attributes.getTheKeepFontSizeFlag() && !keepThisValue) {
				size= attributes.getFontSize();
			};
			if (panel != null) {
				Font font= ReportUtils.safelyGetFont(panel);
				if (font==null) {
					return;
				} else {
					if (size==ReportSpaceAttributes.automaticFontSizeAdjustment) {
						attributes.setTheKeepFontSizeFlag(false);
					} else {
						if (keepThisValue) {
							attributes.setTheKeepFontSizeFlag(true);
						};
						attributes.setFontSize(size);
						font= font.deriveFont((float)size);
						ReportUtils.safelySetFont(font,panel);
					}
				}
			}
		}
	}
	public void setPanelFont(String name, int style, int size) {
		if (attributes != null) {
			if (panel != null) {
				if (size==ReportSpaceAttributes.automaticFontSizeAdjustment) {
					attributes.setTheKeepFontSizeFlag(false);
				} else {
					attributes.setTheKeepFontSizeFlag(true);
					attributes.setFontSize(size);
					int integerSize= (int)size;
					int realFontSize= DefaultOptions.fontSystemSimulationMode.simulate(integerSize);
					Font font= new Font(name,style,realFontSize);
					if (size != (double)integerSize) {
						font= font.deriveFont((float)size);
					};
					ReportUtils.safelySetFont(font,panel);
				}
			}
		}
	}
	// public Font getInternalFont() {
	//	if (panel != null) {
	//		return ReportUtils.safelyGetFont(panel);
	//	} else {
	//		return null;
	//	}
	// }
	//
	public JPopupMenu installTextPanePopupMenu(ActionListener listener) {
		JPopupMenu popup= new JPopupMenu();
		//
		JMenuItem item1= new JMenuItem("Cancel");
		item1.setMnemonic('C');
		item1.setDisplayedMnemonicIndex(0);
		item1.setActionCommand("Cancel");
		item1.addActionListener(listener);
		popup.add(item1);
		//
		popup.addSeparator();
		//
		JMenuItem item2= new JMenuItem("Copy");
		item2.setMnemonic('O');
		item2.setDisplayedMnemonicIndex(1);
		item2.setActionCommand("Copy");
		item2.addActionListener(listener);
		popup.add(item2);
		item_copy= item2;
		//
		JMenuItem item3= new JMenuItem("Select all");
		item3.setMnemonic('E');
		item3.setDisplayedMnemonicIndex(1);
		item3.setActionCommand("SelectAll");
		item3.addActionListener(listener);
		popup.add(item3);
		//
		popup.addSeparator();
		//
		JMenuItem item4= new JMenuItem("Clear");
		item4.setMnemonic('L');
		item4.setDisplayedMnemonicIndex(2);
		item4.setActionCommand("Clear");
		item4.addActionListener(listener);
		popup.add(item4);
		//
		popup.addSeparator();
		//
		popup= DesktopUtils.installStandardPopupMenu(listener,popup);
		return popup;
	}
	//
	public void enableMouseListener() {
		// if (owner != null) {
		//	owner.addMouseListener(this);
		// } else {
		panel.addMouseListener(this);
		// };
		popup= installTextPanePopupMenu(this);
	}
	//public void disableMouseListener() {
	//	// if (owner != null) {
 	//	//	owner.removeMouseListener(this);
	//	// } else {
	//	panel.removeMouseListener(this);
	//	// }
	//}
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
		String text= panel.getSelectedText();
		if (text != null && text.length() > 0) {
			if (item_copy != null) {
				item_copy.setEnabled(true);
			}
		} else {
			if (item_copy != null) {
				item_copy.setEnabled(false);
			}
		};
		if (e.isPopupTrigger()) {
			if (popup != null) {
				popup.show(e.getComponent(),e.getX(),e.getY());
			}
		}
	}
	//
	public void actionPerformed(ActionEvent event) {
		String name= event.getActionCommand();
		if (name.equals("Cancel")) {
		// } else if (name.equals("Cut")) {
		//	if (panel != null) {
		//		panel.cut();
		//	}
		} else if (name.equals("Copy")) {
			if (panel != null) {
				panel.copy();
			}
		// } else if (name.equals("Paste")) {
		//	if (panel != null) {
		//		panel.paste();
		//	}
		// } else if (name.equals("Delete")) {
		//	if (panel != null) {
		//		panel.replaceSelection("");
		//	}
		} else if (name.equals("SelectAll")) {
			if (panel != null) {
				panel.selectAll();
			}
		} else if (name.equals("Clear")) {
			if (panel != null) {
				panel.setText("");
			}
		} else {
			if (attributes != null) {
				StaticContext context= attributes.staticContext;
				if (context != null) {
					DesktopUtils.actionPerformed(event,context);
				}
			}
		}
	}
}
