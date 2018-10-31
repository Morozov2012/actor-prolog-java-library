// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.scalable.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.EventQueue;
import javax.swing.JViewport;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.text.Document;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import java.lang.reflect.InvocationTargetException;

public class ExtendedReportSpace extends CanvasSpace implements MouseListener, ActionListener {
	//
	public TextPaneNoWrap panel;
	public ReportSpaceAttributes attributes;
	//
	protected JPopupMenu popup;
	protected JMenuItem item_copy;
	//
	protected boolean scrollBarIsLocked= false;
	protected boolean scrollBarWasReleased= true;
	protected int scrollBarLockedPosition= 0;
	protected int scrollBarPreviousMaximum= 0;
	//
	public ExtendedReportSpace(CustomControlComponent customControl, Report world, TextPaneNoWrap p) {
		super(customControl);
		targetWorld= world;
		control= new JScrollPane(p);
		panel= p;
		panel.setEditable(false);
		panel.setAutoscrolls(true);
	}
	//
	public void setAttributes(ReportSpaceAttributes a) {
		attributes= a;
	}
	//
	public void safelySetBackground(final Color color) {
		if (SwingUtilities.isEventDispatchThread()) {
			panel.setBackground(color);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						panel.setBackground(color);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setFont(Font font) {
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
						ReportUtils.safelySetFont(font,panel,false);
					}
				}
			}
		}
	}
	public void setPanelFont(String name, int style, int size, boolean invokeUpdate) {
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
					ReportUtils.safelySetFont(font,panel,invokeUpdate);
				}
			}
		}
	}
	public Font getInternalFont() {
		if (panel != null) {
			return ReportUtils.safelyGetFont(panel);
		} else {
			return null;
		}
	}
	public FontMetrics getFontMetrics() {
		if (panel != null) {
			return ReportUtils.safelyGetFontMetrics(panel);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
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
		JMenuItem item3= new JMenuItem("Select All");
		item3.setMnemonic('E');
		item3.setDisplayedMnemonicIndex(1);
		item3.setActionCommand("SelectAll");
		item3.addActionListener(listener);
		popup.add(item3);
		//
		JMenuItem item4= new JMenuItem("Select All & Copy");
		item4.setMnemonic('A');
		item4.setDisplayedMnemonicIndex(7);
		item4.setActionCommand("SelectAllAndCopy");
		item4.addActionListener(listener);
		popup.add(item4);
		//
		popup.addSeparator();
		//
		JMenuItem item5= new JMenuItem("Clear");
		item5.setMnemonic('L');
		item5.setDisplayedMnemonicIndex(1);
		item5.setActionCommand("Clear");
		item5.addActionListener(listener);
		popup.add(item5);
		//
		popup.addSeparator();
		//
		popup= DesktopUtils.installStandardPopupMenu(listener,popup);
		return popup;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelySetCaretPosition(final int textLength) {
		if (EventQueue.isDispatchThread()) {
			quicklySetCaretPosition(textLength);
		} else {
			try {
				EventQueue.invokeAndWait(
					new Runnable() {
						public void run() {
							quicklySetCaretPosition(textLength);
						}
					});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public void quicklySetCaretPosition(int textLength) {
		JScrollBar scrollBar= ((JScrollPane)control).getVerticalScrollBar();
		int value= scrollBar.getValue();
		int extent= scrollBar.getVisibleAmount();
		int maximum= scrollBar.getMaximum();
		boolean valueIsAdjusting= scrollBar.getValueIsAdjusting();
		if (valueIsAdjusting) {
			if (scrollBarWasReleased) {
				if (scrollBarIsLocked && value + extent >= scrollBarPreviousMaximum) {
					scrollBarIsLocked= false;
					scrollBarWasReleased= false;
				} else {
					if (!scrollBarIsLocked) {
						scrollBarIsLocked= true;
						scrollBarLockedPosition= value;
						scrollBarWasReleased= false;
					}
				}
			}
		} else {
			scrollBarWasReleased= true;
		};
		if (!scrollBarIsLocked) {
			panel.setCaretPosition(textLength);
		} else {
			if (scrollBarLockedPosition > maximum) {
				scrollBarLockedPosition= maximum;
			};
			panel.setCaretPosition(scrollBarLockedPosition);
		};
		scrollBarPreviousMaximum= maximum;
	}
	//
	public void safelyResetCaretPosition() {
		if (EventQueue.isDispatchThread()) {
			resetCaretPosition();
		} else {
			try {
				EventQueue.invokeAndWait(
					new Runnable() {
						public void run() {
							resetCaretPosition();
						}
					});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void resetCaretPosition() {
		panel.setCaretPosition(0);
		JScrollBar scrollBar= ((JScrollPane)control).getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMinimum());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyRemoveSuperfluousLines(final Document doc, final Boolean isSucceeded) {
		if (EventQueue.isDispatchThread()) {
			removeSuperfluousLines(doc,isSucceeded);
		} else {
			try {
				EventQueue.invokeAndWait(
					new Runnable() {
						public void run() {
							removeSuperfluousLines(doc,isSucceeded);
						}
					});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void removeSuperfluousLines(Document doc, Boolean isSucceeded) {
		isSucceeded= false;
		JViewport viewport= ((JScrollPane)control).getViewport();
		double pageHeight= viewport.getExtentSize().getHeight();
		Insets scrollPaneInsets= viewport.getInsets();
		double correctedPageHeight= pageHeight - scrollPaneInsets.top - scrollPaneInsets.bottom;
		double textHeight= panel.getMinimumSize().getHeight();
		if (textHeight >= correctedPageHeight) {
			try {
				String plainText= doc.getText(0,doc.getLength());
				FontMetrics metrics= getFontMetrics();
				if (metrics != null) {
					double lineHeight= metrics.getHeight();
					long pageCapacity= (long)Math.floor(correctedPageHeight / lineHeight);
					pageCapacity= StrictMath.max(pageCapacity,1);
					long endPosition= ReportUtils.calculate_position_of_line_end(plainText,pageCapacity);
					doc.remove(0,(int)endPosition+1);
					isSucceeded= true;
				}
			} catch (BadLocationException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void enableMouseListener() {
		panel.addMouseListener(this);
		popup= installTextPanePopupMenu(this);
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
		} else if (name.equals("Copy")) {
			if (panel != null) {
				panel.copy();
			}
		} else if (name.equals("SelectAll")) {
			if (panel != null) {
				panel.selectAll();
			}
		} else if (name.equals("SelectAllAndCopy")) {
			if (panel != null) {
				panel.selectAll();
				panel.copy();
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
