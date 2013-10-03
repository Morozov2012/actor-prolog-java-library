// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.reports.*;
import morozov.system.gui.reports.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.EventQueue;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.AttributeSet;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.MutableAttributeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;

public abstract class Report
		extends Text
		// implements PropertyChangeListener {
		implements ComponentListener {
	//
	protected InternalTextFrame graphicWindow= null;
	protected ExtendedReportSpace space= null;
	//
	public AtomicReference<Term> textColor= new AtomicReference<Term>();
	public AtomicReference<Term> spaceColor= new AtomicReference<Term>();
	//
	protected AtomicReference<MutableAttributeSet> rootTextStyle= new AtomicReference<MutableAttributeSet>(new SimpleAttributeSet());
	protected Map<Color,MutableAttributeSet> transparentTextStyles= Collections.synchronizedMap(new HashMap<Color,MutableAttributeSet>());
	//
	protected ReportSpaceAttributes spaceAttributes= new ReportSpaceAttributes();
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
	abstract public long entry_s_Initialize_0();
	abstract public long entry_s_Start_0();
	abstract public long entry_s_Stop_0();
	//
	public Report() {
	}
	//
	public void closeFiles() {
		synchronized(this) {
			if (graphicWindow != null) {
				// graphicWindow.dispose();
				DesktopUtils.safelyDispose(graphicWindow);
			}
		};
		super.closeFiles();
	}
	//
	public void clear0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			Document doc= spaceAttributes.getStyledDocument();
			if (doc != null) {
				safelySetText("",doc);
			}
		}
	}
	//
	public void setString1s(ChoisePoint iX, Term text) {
		createGraphicWindowIfNecessary(iX,false);
		String buffer= text.toString(iX);
		synchronized(this) {
			Document doc= spaceAttributes.getStyledDocument();
			if (doc != null) {
				safelySetText(buffer,doc);
			}
		}
	}
	//
	public void getString0ff(ChoisePoint iX, PrologVariable text) {
		// createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			String content;
			Document doc= spaceAttributes.getStyledDocument();
			if (doc != null) {
				content= safelyGetText(doc);
			} else {
				content= "";
			};
			text.value= new PrologString(content);
		}
	}
	public void getString0fs(ChoisePoint iX) {
	}
	//
	public void show2s(ChoisePoint iX, Term a1, Term a2) {
		createGraphicWindowIfNecessary(iX,true);
	}
	public void show0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
	}
	//
	public void redraw0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			if (graphicWindow != null) {
				redrawInternalFrame(graphicWindow,iX);
				graphicWindow.safelyRestoreSize(staticContext);
				DesktopUtils.safelyRepaint(graphicWindow);
			} else if (space != null) {
				redrawSpace(iX);
				DesktopUtils.safelyRepaint(space);
			}
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		if (desktopDoesNotExist()) {
			return;
		} else if (spaceDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized(this) {
				if (graphicWindow != null) {
					DesktopUtils.safelySetVisible(false,graphicWindow);
				}
			}
		}
	}
	//
	public void maximize0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized(this) {
			if (graphicWindow != null) {
				DesktopUtils.safelyMaximize(graphicWindow);
			}
		}
	}
	//
	public void minimize0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized(this) {
			if (graphicWindow != null) {
				DesktopUtils.safelyMinimize(graphicWindow);
			}
		}
	}
	//
	public void restore0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized(this) {
			if (graphicWindow != null) {
				DesktopUtils.safelyRestore(graphicWindow);
			}
		}
	}
	//
	public void isMaximized0s(ChoisePoint iX) throws Backtracking {
		if (desktopDoesNotExist()) {
			throw Backtracking.instance;
		} else if (spaceDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if (!DesktopUtils.safelyIsMaximized(graphicWindow)) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isMinimized0s(ChoisePoint iX) throws Backtracking {
		if (desktopDoesNotExist()) {
			throw Backtracking.instance;
		} else if (spaceDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if(!DesktopUtils.safelyIsMinimized(graphicWindow)) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isRestored0s(ChoisePoint iX) throws Backtracking {
		if (desktopDoesNotExist()) {
			throw Backtracking.instance;
		} else if (spaceDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if(!DesktopUtils.safelyIsRestored(graphicWindow)) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
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
	public void setTextColor1s(ChoisePoint iX, Term color) {
		createGraphicWindowIfNecessary(iX,false);
		textColor.set(color.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
		spaceColor.set(getBuiltInSlot_E_space_color().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
	}
	public void setTextColor2s(ChoisePoint iX, Term t, Term s) {
		createGraphicWindowIfNecessary(iX,false);
		textColor.set(t.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
		spaceColor.set(s.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
	}
	//
	public void changeFont2s(ChoisePoint iX, Term fontName, Term fontSize) {
		createGraphicWindowIfNecessary(iX,false);
		Term fontStyle= getBuiltInSlot_E_font_style();
		change_font(iX,fontName,fontSize,fontStyle);
	}
	public void changeFont3s(ChoisePoint iX, Term fontName, Term fontSize, Term fontStyle) {
		createGraphicWindowIfNecessary(iX,false);
		change_font(iX,fontName,fontSize,fontStyle);
	}
	//
	public void changeFontSize1s(ChoisePoint iX, Term fontSize) {
		createGraphicWindowIfNecessary(iX,false);
		int size= computeFontSize(fontSize,iX);
		synchronized(this) {
			if (space != null) {
				space.setPanelFontSize(size,true);
			}
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
				synchronized(this) {
					if (graphicWindow != null) {
						try {
							fontSize= DefaultOptions.textFontSize;
							size= GUI_Utils.termToFontSize(fontSize,iX);
						} catch (TermIsSymbolDefault e3) {
							size= defaultFontSize;
						}
					} else {
						size= ReportSpaceAttributes.automaticFontSizeAdjustment;
					}
				}
			}
		};
		return size;
	}
	//
	public void changeBackgroundColor1s(ChoisePoint iX, Term backgroundColor) {
		changeBackgroundColor(iX,backgroundColor);
	}
	//
	public void initialize0s(ChoisePoint iX) {
	}
	public void start0s(ChoisePoint iX) {
	}
	public void stop0s(ChoisePoint iX) {
	}
	//
	public void registerReport(ExtendedReportSpace s, ChoisePoint iX) {
		synchronized(this) {
			if (space==null) {
				space= s;
				spaceAttributes.implementValues(space);
				spaceAttributes.setStaticContext(staticContext);
				space.enableMouseListener();
				// redrawSpace(iX);
				// space.enableMouseMotionListener();
				// space.setCommands(actualCommands);
				// space.setEnableAntialiasing(sceneAntialiasingIsEnabled);
				DesktopUtils.safelyRepaint(space);
				// DesktopUtils.safelyRepaint(space.panel);
				space.panel.revalidate();
			}
		}
	}
	//
	public void release(TextPaneNoWrap panel, boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		synchronized(this) {
			if (space != null && graphicWindow==null) {
				spaceAttributes.collectValues(space);
				// space.skipDelayedRepainting();
				// space.disableMouseListener();
				// space.disableMouseMotionListener();
				space= null;
			}
		};
		long domainSignature= entry_s_Stop_0();
		callInternalProcedure(domainSignature,dialogIsModal,modalChoisePoint);
	}
	//
	public void draw(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		if (spaceAttributes.initializeControlIfNecessary()) {
			redrawSpace(modalChoisePoint);
			long domainSignature1= entry_s_Initialize_0();
			callInternalProcedure(domainSignature1,dialogIsModal,modalChoisePoint);
		};
		long domainSignature2= entry_s_Start_0();
		callInternalProcedure(domainSignature2,dialogIsModal,modalChoisePoint);
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
	public boolean spaceDoesNotExist() {
		// Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		// return !innerWindows.containsKey(this);
		synchronized(this) {
			return (space==null);
		}
	}
	protected void createGraphicWindowIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		synchronized(this) {
			if (space==null && spaceAttributes.controlIsNotInitialized()) {
				DesktopUtils.createPaneIfNecessary(staticContext);
				createInternalFrameIfNecessary(iX,enableMovingWindowToFront);
			} else if (graphicWindow != null) {
				if (enableMovingWindowToFront) {
					DesktopUtils.safelyMoveToFront(graphicWindow);
				};
				DesktopUtils.safelySetVisible(true,graphicWindow);
			}
		}
	}
	//
	protected void createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		// InternalTextFrame innerWindow= innerWindows.get(this);
		boolean restoreWindow= false;
		boolean moveWindowToFront= false;
		if (graphicWindow==null) {
			if (ReportUtils.isConsoleTextWindow(getBuiltInSlot_E_area_type(),iX)) {
				InternalTextFrame consoleWindow= StaticReportAttributes.retrieveConsoleWindow(staticContext);
				if (consoleWindow==null) {
					ReentrantLock lock= StaticDesktopAttributes.retrieveDesktopGuard(staticContext);
					lock.lock();
					try {
						consoleWindow= StaticReportAttributes.retrieveConsoleWindow(staticContext);
						if (consoleWindow==null) {
							graphicWindow= createInternalFrame(iX);
							StaticReportAttributes.setConsoleWindow(graphicWindow,staticContext);
							restoreWindow= true;
						} else {
							graphicWindow= consoleWindow;
							space= graphicWindow.scrollPane;
							spaceAttributes.setStyledDocument(space.attributes.getStyledDocument());
							redrawInternalFrame(graphicWindow,iX);
						}
					} finally {
						lock.unlock();
					}
				} else {
					graphicWindow= consoleWindow;
					space= graphicWindow.scrollPane;
					spaceAttributes.setStyledDocument(space.attributes.getStyledDocument());
					redrawInternalFrame(graphicWindow,iX);
				}
			} else {
				graphicWindow= innerWindows.get(this);
				if (graphicWindow==null) {
					graphicWindow= createInternalFrame(iX);
					restoreWindow= true;
				}
			}
		} else {
			moveWindowToFront= true;
		};
		if (restoreWindow) {
			graphicWindow.safelyRestoreSize(staticContext);
		};
		if (moveWindowToFront && enableMovingWindowToFront) {
			DesktopUtils.safelyMoveToFront(graphicWindow);
		};
		// graphicWindow= innerWindow;
		// space= graphicWindow.scrollPane;
		// redrawInternalFrame(graphicWindow,null,iX);
		DesktopUtils.safelySetVisible(true,graphicWindow);
		// return graphicWindow;
	}
	//
	protected InternalTextFrame createInternalFrame(ChoisePoint iX) {
		//
		// String title= getBuiltInSlot_E_title().toString(iX);
		String title= null;
		try {
			title= GUI_Utils.termToFrameTitleSafe(getBuiltInSlot_E_title(),iX);
		} catch (TermIsSymbolDefault e) {
			title= "";
		};
		//
		spaceAttributes.setStaticContext(staticContext);
		// InternalTextFrame innerWindow
		graphicWindow= new InternalTextFrame(title,spaceAttributes);
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		innerWindows.put(this,graphicWindow);
		//
		graphicWindow.panel.setStyledDocument(spaceAttributes.getStyledDocument());
		graphicWindow.addComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		desktop.add(graphicWindow);
		//
		// redrawInternalFrame(innerWindow,null,iX);
		// graphicWindow= innerWindow;
		space= graphicWindow.scrollPane;
		redrawInternalFrame(graphicWindow,iX);
		//
		return graphicWindow;
	}
	protected void redrawInternalFrame(InternalTextFrame graphicWindow, ChoisePoint iX) {
		// String title= getBuiltInSlot_E_title().toString(iX);
		String title= null;
		try {
			title= GUI_Utils.termToFrameTitleSafe(getBuiltInSlot_E_title(),iX);
		} catch (TermIsSymbolDefault e) {
			title= "";
		};
		redrawInternalFrame(graphicWindow,title,iX);
	}
	protected void redrawInternalFrame(InternalTextFrame graphicWindow, String title, ChoisePoint iX) {
		//
		if (title != null) {
			DesktopUtils.safelySetTitle(title,graphicWindow);
		};
		//
		Term x= getBuiltInSlot_E_x();
		Term y= getBuiltInSlot_E_y();
		Term width= getBuiltInSlot_E_width().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		Term height= getBuiltInSlot_E_height().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		//
		graphicWindow.logicalWidth.set(GUI_Utils.termToSize(width,iX));
		graphicWindow.logicalHeight.set(GUI_Utils.termToSize(height,iX));
		graphicWindow.logicalX.set(GUI_Utils.termToCoordinate(x,iX));
		graphicWindow.logicalY.set(GUI_Utils.termToCoordinate(y,iX));
		//
		// graphicWindow.safelyRestoreSize(staticContext);
		//
		redrawSpace(iX);
	}
	protected void redrawSpace(ChoisePoint iX) {
		changeBackgroundColor(iX,getBuiltInSlot_E_background_color());
		change_font(
			iX,
			getBuiltInSlot_E_font_name(),
			getBuiltInSlot_E_font_size(),
			getBuiltInSlot_E_font_style());
		textColor.set(getBuiltInSlot_E_text_color().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
		spaceColor.set(getBuiltInSlot_E_space_color().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES));
	}
	//
	protected void write_text_buffer(ChoisePoint iX, boolean appendNewLine, StringBuilder textBuffer) {
		if (appendNewLine) {
			textBuffer.append("\n");
		};
		createGraphicWindowIfNecessary(iX,false);
		// Document doc= space.panel.getDocument();
		Color colorOfText= defaultTextColor;
		try {
			colorOfText= GUI_Utils.termToColor(textColor.get(),iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				colorOfText= GUI_Utils.termToColor(getBuiltInSlot_E_text_color(),iX);
			} catch (TermIsSymbolDefault e2) {
			}
		};
		Color colorOfSpace= defaultBackgroundColor;
		boolean useSpaceColor= true;
		try {
			colorOfSpace= GUI_Utils.termToColor(spaceColor.get(),iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				colorOfSpace= GUI_Utils.termToColor(getBuiltInSlot_E_space_color(),iX);
			} catch (TermIsSymbolDefault e2) {
				useSpaceColor= false;
			}
		};
		synchronized(this) {
			Document doc= spaceAttributes.getStyledDocument();
			if (doc != null) {
				if (useSpaceColor) {
					MutableAttributeSet opaqueTextAttributes= getOpaqueTextStyle(colorOfText,colorOfSpace);
					StyleConstants.setForeground(opaqueTextAttributes,colorOfText);
					StyleConstants.setBackground(opaqueTextAttributes,colorOfSpace);
					safelyInsertString(doc,textBuffer.toString(),opaqueTextAttributes);
				} else {
					MutableAttributeSet transparentTextAttributes= getTransparentTextStyle(colorOfText);
					StyleConstants.setForeground(transparentTextAttributes,colorOfText);
					safelyInsertString(doc,textBuffer.toString(),transparentTextAttributes);
				};
				try {
					long maxLineNumber= ReportUtils.termToMaxLineNumber(getBuiltInSlot_E_max_line_number(),iX);
					String plainText= safelyGetText(doc);
					long numberOfLines= calculate_number_of_lines(plainText);
					if (numberOfLines > maxLineNumber) {
						long endPosition= calculate_position_of_line_end(plainText,maxLineNumber);
						safelyRemoveText(doc,endPosition);
						// space.panel.setDocument(doc);
					}
				} catch (TermIsSymbolWindowHeight e) {
					//
					if (space != null) {
						synchronized(space) {
							double pageHeight= space.getViewport().getExtentSize().getHeight();
							Insets scrollPaneInsets= space.getViewport().getInsets();
							double correctedPageHeight= pageHeight - scrollPaneInsets.top - scrollPaneInsets.bottom;
							//
							// double textHeight= space.getMinimumSize().getHeight();
							double textHeight= space.panel.getMinimumSize().getHeight();
							if (textHeight >= correctedPageHeight) {
								// String plainText= doc.getText(0,doc.getLength());
								String plainText= safelyGetText(doc);
								Font font= space.getFont();
								FontMetrics metrics= space.getFontMetrics(font);
								double lineHeight= metrics.getHeight();
								long pageCapacity= (long)Math.floor(correctedPageHeight / lineHeight);
								pageCapacity= StrictMath.max(pageCapacity,1);
								long endPosition= calculate_position_of_line_end(plainText,pageCapacity);
								safelyRemoveText(doc,endPosition);
								// space.panel.setDocument(doc);
							}
						}
					}
				} catch (TermIsSymbolNoLimit e) {
				};
				if (space != null) {
					safelySetCaretPosition(space.panel,doc.getLength());
				}
			}
		}
	}
	//
	protected static void safelySetText(final String buffer, final Document doc) {
		if (EventQueue.isDispatchThread()) {
			try {
				// doc.setText(text);
				doc.remove(0,doc.getLength());
				doc.insertString(0,buffer,null);
			} catch (BadLocationException e) {
			}
		} else {
			try {
				EventQueue.invokeAndWait(
					new Runnable() {
						public void run() {
							try {
								// doc.setText(text);
								doc.remove(0,doc.getLength());
								doc.insertString(0,buffer,null);
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
	protected static String safelyGetText(final Document doc) {
		if (EventQueue.isDispatchThread()) {
			try {
				return doc.getText(0,doc.getLength());
			} catch (BadLocationException e) {
				return "";
			}
		} else {
			try {
				waitForIdle();
				final AtomicReference<StringBuilder> buffer= new AtomicReference<StringBuilder>(new StringBuilder());
				EventQueue.invokeAndWait(
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
		if (EventQueue.isDispatchThread()) {
			try {
				doc.insertString(doc.getLength(),buffer,textStyle);
			} catch (BadLocationException e) {
			}
		} else {
			try {
				waitForIdle();
				EventQueue.invokeAndWait(
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
		if (EventQueue.isDispatchThread()) {
			try {
				doc.remove(0,(int)endPosition+1);
			} catch (BadLocationException e) {
			}
		} else {
			try {
				waitForIdle();
				EventQueue.invokeAndWait(
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
	protected static void safelySetCaretPosition(final JTextPane panel, final int textLength) {
		if (EventQueue.isDispatchThread()) {
			// Document doc= panel.getDocument();
			panel.setCaretPosition(textLength); // doc.getLength());
		} else {
			try {
				waitForIdle();
				EventQueue.invokeAndWait(
					new Runnable() {
						public void run() {
							// Document doc= panel.getDocument();
							panel.setCaretPosition(textLength); // doc.getLength());
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
		updateRootTextStyleUnderline(isUnderlined);
		synchronized(this) {
			// if (graphicWindow != null) {
			//	int integerSize= (int)size;
			//	Font font= new Font(name,style,integerSize);
			//	if (size != (double)integerSize) {
			//		font= font.deriveFont((float)size);
			//	};
			//	ReportUtils.safelySetFont(font,textWindow);
			// } else
			if (space != null) {
				space.setPanelFont(name,style,size);
			}
		}
	}
	//
	public void changeBackgroundColor(ChoisePoint iX, Term backgroundColor) {
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
		updateRootTextStyleBackground(color);
		synchronized(this) {
			if (space != null) {
				ReportUtils.safelySetBackground(color,space.panel);
			}
		}
	}
	//
	public void updateRootTextStyleUnderline(boolean isUnderlined) {
		synchronized(this) {
			MutableAttributeSet style= rootTextStyle.get();
			StyleConstants.setUnderline(style,isUnderlined);
			rootTextStyle.set(style);
		}
	}
	public void updateRootTextStyleBackground(Color color) {
		synchronized(this) {
			MutableAttributeSet style= rootTextStyle.get();
			StyleConstants.setBackground(style,color);
			rootTextStyle.set(style);
		}
	}
	//
	public MutableAttributeSet getOpaqueTextStyle(Color t, Color s) {
		synchronized(this) {
			MutableAttributeSet textStyle= new SimpleAttributeSet();
			textStyle.setResolveParent(rootTextStyle.get());
			StyleConstants.setForeground(textStyle,t);
			StyleConstants.setBackground(textStyle,s);
			return textStyle;
		}
	}
	//
	public MutableAttributeSet getTransparentTextStyle(Color t) {
		synchronized(this) {
			MutableAttributeSet textStyle= transparentTextStyles.get(t);
			if (textStyle==null) {
				textStyle= new SimpleAttributeSet();
				textStyle.setResolveParent(rootTextStyle.get());
				StyleConstants.setForeground(textStyle,t);
				transparentTextStyles.put(t,textStyle);
			};
			return textStyle;
		}
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
	//
	public static synchronized void waitForIdle() {
		try {
			EventQueue.invokeAndWait(
				new Runnable() {
					public void run() {
					}
				} );
		} catch(InterruptedException e) {
		} catch(InvocationTargetException e) {
		}
	}
}
