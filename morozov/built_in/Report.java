// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.reports.*;
import morozov.terms.*;

import java.awt.*;
import javax.swing.text.*;
import javax.swing.SwingUtilities;
import javax.swing.JTextPane;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

public abstract class Report
		extends Text
		// implements PropertyChangeListener {
		implements ComponentListener {
	//
	protected static final String defaultFontName= Font.MONOSPACED;
	protected static final int defaultFontSize= 18;
	protected static final int defaultFontStyle= Font.PLAIN;
	protected static final boolean defaultFontStyleIsUnderlined= false;
	protected static final Color defaultBackgroundColor= Color.WHITE;
	protected static final Color defaultTextColor= Color.BLACK;
	//
	abstract protected Term getBuiltInSlot_E_title();
	abstract protected Term getBuiltInSlot_E_text_color();
	abstract protected Term getBuiltInSlot_E_space_color();
	abstract protected Term getBuiltInSlot_E_font_name();
	abstract protected Term getBuiltInSlot_E_font_size();
	abstract protected Term getBuiltInSlot_E_font_style();
	abstract protected Term getBuiltInSlot_E_x();
	abstract protected Term getBuiltInSlot_E_y();
	abstract protected Term getBuiltInSlot_E_width();
	abstract protected Term getBuiltInSlot_E_height();
	abstract protected Term getBuiltInSlot_E_background_color();
	abstract protected Term getBuiltInSlot_E_max_line_number();
	abstract protected Term getBuiltInSlot_E_area_type();
	//
	public void clear0s(ChoisePoint iX) {
		if (desktopDoesNotExist()) {
			return;
		} else if (reportDoesNotExist()) {
			return;
		} else {
			DesktopUtils.createPaneIfNecessary(staticContext);
			InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
			ReportUtils.safelySetText("",textWindow);
		}
	}
	//
	public void setString1s(ChoisePoint iX, Term text) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
		ReportUtils.safelySetText(text.toString(iX),textWindow);
	}
	//
	public void getString0ff(ChoisePoint iX, PrologVariable text) {
		if (desktopDoesNotExist()) {
			text.value= new PrologString("");
		} else if (reportDoesNotExist()) {
			text.value= new PrologString("");
		} else {
			String value;
			DesktopUtils.createPaneIfNecessary(staticContext);
			InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
			String content= ReportUtils.safelyGetText(textWindow);
			text.value= new PrologString(content);
		}
	}
	public void getString0fs(ChoisePoint iX) {
	}
	//
	public void show2s(ChoisePoint iX, Term a1, Term a2) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		createInternalFrameIfNecessary(iX,true);
	}
	public void show0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		createInternalFrameIfNecessary(iX,true);
	}
	//
	public void redraw0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		InternalTextFrame textWindow= innerWindows.get(this);
		if (textWindow==null) {
			createInternalFrameIfNecessary(iX);
		} else {
			redrawInternalFrame(textWindow,iX);
			textWindow.safelyRestoreSize(staticContext);
			DesktopUtils.safelyRepaint(textWindow);
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		if (desktopDoesNotExist()) {
			return;
		} else if (reportDoesNotExist()) {
			return;
		} else {
			DesktopUtils.createPaneIfNecessary(staticContext);
			InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
			DesktopUtils.safelySetVisible(false,textWindow);
		}
	}
	//
	public void write1ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		write_text_buffer(iX,false,textBuffer);
	}
	//
	public void writeLn1ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		write_text_buffer(iX,true,textBuffer);
	}
	//
	public void writeF2ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToFormattedString(iX,(Term[])args);
		write_text_buffer(iX,false,textBuffer);
	}
	//
	public void newLine0s(ChoisePoint iX) {
		write_text_buffer(iX,true,new StringBuilder());
	}
	//
	public void setTextColor1s(ChoisePoint iX, Term textColor) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
		textWindow.textColor.set(textColor.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
		textWindow.spaceColor.set(getBuiltInSlot_E_space_color().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
	}
	public void setTextColor2s(ChoisePoint iX, Term textColor, Term spaceColor) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
		textWindow.textColor.set(textColor.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
		textWindow.spaceColor.set(spaceColor.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
	}
	//
	public void changeFont2s(ChoisePoint iX, Term fontName, Term fontSize) {
		Term fontStyle= getBuiltInSlot_E_font_style();
		change_font(iX,fontName,fontSize,fontStyle);
	}
	public void changeFont3s(ChoisePoint iX, Term fontName, Term fontSize, Term fontStyle) {
		change_font(iX,fontName,fontSize,fontStyle);
	}
	//
	public void changeFontSize1s(ChoisePoint iX, Term fontSize) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
		float size= computeFontSize(fontSize,iX);
		Font font= ReportUtils.safelyGetFont(textWindow);
		if (font==null) {
			return;
		} else {
			font= font.deriveFont(size);
			ReportUtils.safelySetFont(font,textWindow);
		}
	}
	protected int computeFontSize(Term fontSize, ChoisePoint iX) {
		int size;
		try {
			size= GUI_Utils.termToFontSize(fontSize,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				fontSize= getBuiltInSlot_E_font_size();
				size= GUI_Utils.termToFontSize(fontSize,iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					fontSize= DefaultOptions.textFontSize;
					size= GUI_Utils.termToFontSize(fontSize,iX);
				} catch (TermIsSymbolDefault e3) {
					size= defaultFontSize;
				}
			}
		};
		return size;
	}
	//
	public void changeBackgroundColor1s(ChoisePoint iX, Term backgroundColor) {
		changeBackgroundColor(iX,backgroundColor);
	}
	// Auxiliary operations
	protected boolean desktopDoesNotExist() {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		if (desktop==null) {
			return true;
		} else {
			return false;
		}
	}
	public boolean reportDoesNotExist() {
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		return !innerWindows.containsKey(this);
	}
	//
	protected InternalTextFrame createInternalFrameIfNecessary(ChoisePoint iX) {
		return createInternalFrameIfNecessary(iX,false);
	}
	protected InternalTextFrame createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		InternalTextFrame textWindow= innerWindows.get(this);
		boolean restoreWindow= false;
		boolean moveWindowToFront= false;
		if (textWindow==null) {
			if (ReportUtils.isConsoleTextWindow(getBuiltInSlot_E_area_type(),iX)) {
				InternalTextFrame consoleWindow= StaticReportAttributes.retrieveConsoleWindow(staticContext);
				if (consoleWindow==null) {
					ReentrantLock lock= StaticDesktopAttributes.retrieveDesktopGuard(staticContext);
					lock.lock();
					try {
						consoleWindow= StaticReportAttributes.retrieveConsoleWindow(staticContext);
						if (consoleWindow==null) {
							textWindow= createInternalFrame(iX);
							StaticReportAttributes.setConsoleWindow(textWindow,staticContext);
							restoreWindow= true;
						} else {
							textWindow= consoleWindow;
						}
					} finally {
						lock.unlock();
					}
				} else {
					textWindow= consoleWindow;
				}
			} else {
				synchronized(this) {
					textWindow= innerWindows.get(this);
					if (textWindow==null) {
						textWindow= createInternalFrame(iX);
						restoreWindow= true;
					}
				}
			}
		} else {
			moveWindowToFront= true;
		};
		if (restoreWindow) {
			textWindow.safelyRestoreSize(staticContext);
		};
		if (moveWindowToFront && enableMovingWindowToFront) {
			DesktopUtils.safelyMoveToFront(textWindow);
		};
		DesktopUtils.safelySetVisible(true,textWindow);
		return textWindow;
	}
	//
	protected InternalTextFrame createInternalFrame(ChoisePoint iX) {
		//
		String title= getBuiltInSlot_E_title().toString(iX);
		//
		InternalTextFrame textWindow= new InternalTextFrame(title,staticContext);
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		innerWindows.put(this,textWindow);
		//
		textWindow.addComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		desktop.add(textWindow);
		//
		redrawInternalFrame(textWindow,null,iX);
		//
		return textWindow;
	}
	protected void redrawInternalFrame(InternalTextFrame textWindow, ChoisePoint iX) {
		String title= getBuiltInSlot_E_title().toString(iX);
		redrawInternalFrame(textWindow,title,iX);
	}
	protected void redrawInternalFrame(InternalTextFrame textWindow, String title, ChoisePoint iX) {
		//
		if (title != null) {
			DesktopUtils.safelySetTitle(title,textWindow);
		};
		//
		Term x= getBuiltInSlot_E_x();
		Term y= getBuiltInSlot_E_y();
		Term width= getBuiltInSlot_E_width().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		Term height= getBuiltInSlot_E_height().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		//
		textWindow.logicalWidth.set(GUI_Utils.termToSize(width,iX));
		textWindow.logicalHeight.set(GUI_Utils.termToSize(height,iX));
		textWindow.logicalX.set(GUI_Utils.termToCoordinate(x,iX));
		textWindow.logicalY.set(GUI_Utils.termToCoordinate(y,iX));
		//
		// textWindow.safelyRestoreSize(staticContext);
		//
		changeBackgroundColor(iX,getBuiltInSlot_E_background_color());
		change_font(
			iX,
			getBuiltInSlot_E_font_name(),
			getBuiltInSlot_E_font_size(),
			getBuiltInSlot_E_font_style());
		textWindow.textColor.set(getBuiltInSlot_E_text_color().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
		textWindow.spaceColor.set(getBuiltInSlot_E_space_color().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
	}
	//
	protected void write_text_buffer(ChoisePoint iX, boolean appendNewLine, StringBuilder textBuffer) {
		if (appendNewLine) {
			textBuffer.append("\n");
		};
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
		synchronized(textWindow) {
			Document doc= textWindow.panel.getDocument();
			Color textColor= defaultTextColor;
			try {
				textColor= GUI_Utils.termToColor(textWindow.textColor.get(),iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					textColor= GUI_Utils.termToColor(getBuiltInSlot_E_text_color(),iX);
				} catch (TermIsSymbolDefault e2) {
				}
			};
			Color spaceColor= defaultBackgroundColor;
			boolean useSpaceColor= true;
			try {
				spaceColor= GUI_Utils.termToColor(textWindow.spaceColor.get(),iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					spaceColor= GUI_Utils.termToColor(getBuiltInSlot_E_space_color(),iX);
				} catch (TermIsSymbolDefault e2) {
					useSpaceColor= false;
				}
			};
			if (useSpaceColor) {
				MutableAttributeSet opaqueTextAttributes= textWindow.getOpaqueTextStyle(textColor,spaceColor);
				StyleConstants.setForeground(opaqueTextAttributes,textColor);
				StyleConstants.setBackground(opaqueTextAttributes,spaceColor);
				// try {
					// doc.insertString(doc.getLength(),textBuffer.toString(),opaqueTextAttributes);
					safelyInsertString(doc,textBuffer.toString(),opaqueTextAttributes);
				// } catch (BadLocationException e) {
				// }
			} else {
				MutableAttributeSet transparentTextAttributes= textWindow.getTransparentTextStyle(textColor);
				StyleConstants.setForeground(transparentTextAttributes,textColor);
				// try {
					// doc.insertString(doc.getLength(),textBuffer.toString(),transparentTextAttributes);
					safelyInsertString(doc,textBuffer.toString(),transparentTextAttributes);
				// } catch (BadLocationException e) {
				// }
			};
			// try {
				try {
					long maxLineNumber= ReportUtils.termToMaxLineNumber(getBuiltInSlot_E_max_line_number(),iX);
					// String plainText= doc.getText(0,doc.getLength());
					String plainText= safelyGetText(doc);
					long numberOfLines= calculate_number_of_lines(plainText);
					if (numberOfLines > maxLineNumber) {
						long endPosition= calculate_position_of_line_end(plainText,maxLineNumber);
						safelyRemoveText(doc,endPosition);
						textWindow.panel.setDocument(doc);
					}
				} catch (TermIsSymbolWindowHeight e) {
					//
					double pageHeight= textWindow.scrollPane.getViewport().getExtentSize().getHeight();
					Insets scrollPaneInsets= textWindow.scrollPane.getViewport().getInsets();
					double correctedPageHeight= pageHeight - scrollPaneInsets.top - scrollPaneInsets.bottom;
					//
					double textHeight= textWindow.panel.getMinimumSize().getHeight();
					//
					if (textHeight >= correctedPageHeight) {
						// String plainText= doc.getText(0,doc.getLength());
						String plainText= safelyGetText(doc);
						Font font= textWindow.panel.getFont();
						FontMetrics metrics= textWindow.panel.getFontMetrics(font);
						double lineHeight= metrics.getHeight();
						long pageCapacity= (long)Math.floor(correctedPageHeight / lineHeight);
						pageCapacity= StrictMath.max(pageCapacity,1);
						long endPosition= calculate_position_of_line_end(plainText,pageCapacity);
						safelyRemoveText(doc,endPosition);
						textWindow.panel.setDocument(doc);
					}
				} catch (TermIsSymbolNoLimit e) {
				};
				// textWindow.panel.setCaretPosition(doc.getLength());
				safelySetCaretPosition(textWindow.panel);
			// } catch (BadLocationException e) {
			// }
		}
	}
	//
	protected static String safelyGetText(final Document doc) {
		if (SwingUtilities.isEventDispatchThread()) {
			try {
				return doc.getText(0,doc.getLength());
			} catch (BadLocationException e) {
				return "";
			}
		} else {
			try {
				final AtomicReference<StringBuilder> buffer= new AtomicReference<StringBuilder>(new StringBuilder());
				SwingUtilities.invokeAndWait(
					new Runnable() {
						public void run() {
							try {
								buffer.get().append(doc.getText(0,doc.getLength()));
							} catch (BadLocationException e) {
							}
						}
					});
				return buffer.get().toString();
			} catch (InterruptedException e) {
				return "";
			} catch (InvocationTargetException e) {
				return "";
			}
		}
	}
	//
	protected static void safelyInsertString(final Document doc, final String buffer, final AttributeSet textStyle) {
		if (SwingUtilities.isEventDispatchThread()) {
			try {
				doc.insertString(doc.getLength(),buffer,textStyle);
			} catch (BadLocationException e) {
			}
		} else {
			try {
				SwingUtilities.invokeAndWait(
					new Runnable() {
						public void run() {
							try {
								doc.insertString(doc.getLength(),buffer,textStyle);
							} catch (BadLocationException e) {
							}
						}
					});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}

	}
	//
	protected static void safelyRemoveText(final Document doc, final long endPosition) {
		if (SwingUtilities.isEventDispatchThread()) {
			try {
				doc.remove(0,(int)endPosition+1);
			} catch (BadLocationException e) {
			}
		} else {
			try {
				SwingUtilities.invokeAndWait(
					new Runnable() {
						public void run() {
							try {
								doc.remove(0,(int)endPosition+1);
							} catch (BadLocationException e) {
							}
						}
					});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	protected static void safelySetCaretPosition(final JTextPane panel) {
		if (SwingUtilities.isEventDispatchThread()) {
			// try {
				Document doc= panel.getDocument();
				panel.setCaretPosition(doc.getLength());
			// } catch (BadLocationException e) {
			// }
		} else {
			try {
				SwingUtilities.invokeAndWait(
					new Runnable() {
						public void run() {
							// try {
								Document doc= panel.getDocument();
								panel.setCaretPosition(doc.getLength());
							// } catch (BadLocationException e) {
							// }
						}
					});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	protected long calculate_number_of_lines(String text) {
		long counter= 0;
		int c= 0;
		for(int i= 0; i < text.length(); i++) {
			c= text.codePointAt(i);
			if (c == '\n') {
				counter= counter + 1;
			}
		};
		if (c != '\n') {
			counter= counter + 1;
		};
		return counter;
	}
	//
	protected long calculate_position_of_line_end(String text, long n) {
		int bound= text.length()-1; 
		long counter= n;
		for(int i=bound; i >= 0; i--) {
			int c= text.codePointAt(i);
			if (c == '\n') {
				counter= counter - 1;
			};
			if (counter <= 0) {
				return i;
			}
		};
		return text.length();
	}
	//
	public void change_font(ChoisePoint iX, Term fontName, Term fontSize, Term fontStyle) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
		String name;
		try {
			name= GUI_Utils.termToFontName(fontName,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				fontName= getBuiltInSlot_E_font_name();
				name= GUI_Utils.termToFontName(fontName,iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					fontName= DefaultOptions.textFontName;
					name= GUI_Utils.termToFontName(fontName,iX);
				} catch (TermIsSymbolDefault e3) {
					name= defaultFontName;
				}
			}
		};
		int size= computeFontSize(fontSize,iX);
		int style;
		boolean isUnderlined;
		try {
			style= GUI_Utils.termToFontStyleSafe(fontStyle,iX);
			isUnderlined= GUI_Utils.fontIsUnderlined(fontStyle,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				fontStyle= getBuiltInSlot_E_font_style();
				style= GUI_Utils.termToFontStyleSafe(fontStyle,iX);
				isUnderlined= GUI_Utils.fontIsUnderlined(fontStyle,iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					fontStyle= DefaultOptions.textFontStyle;
					style= GUI_Utils.termToFontStyleSafe(fontStyle,iX);
					isUnderlined= GUI_Utils.fontIsUnderlined(fontStyle,iX);
				} catch (TermIsSymbolDefault e3) {
					style= defaultFontStyle;
					isUnderlined= defaultFontStyleIsUnderlined;
				}
			}
		};
		textWindow.updateRootTextStyleUnderline(isUnderlined);
		ReportUtils.safelySetFont(new Font(name,style,size),textWindow);
	}
	//
	public void changeBackgroundColor(ChoisePoint iX, Term backgroundColor) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalTextFrame textWindow= createInternalFrameIfNecessary(iX);
		Color color;
		try {
			color= GUI_Utils.termToColor(backgroundColor,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				backgroundColor= getBuiltInSlot_E_background_color();
				color= GUI_Utils.termToColor(backgroundColor,iX);
			} catch (TermIsSymbolDefault e2) {
				color= defaultBackgroundColor;
			}
		};
		textWindow.updateRootTextStyleBackground(color);
		ReportUtils.safelySetBackground(color,textWindow);
	}
	//
	public void componentHidden(ComponentEvent e) {
		DesktopUtils.selectNextInternalFrame(staticContext);
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentResized(ComponentEvent e) {
	}
	public void componentShown(ComponentEvent e) {
	}
}
