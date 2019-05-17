// (c) 2010-2014 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.reports.*;
import morozov.system.gui.reports.signals.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.MutableAttributeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;

public abstract class Report extends Text {
	//
	// There are values of slots:
	//
	protected AtomicReference<ExtendedMaximalLineNumber> maximalLineNumber= new AtomicReference<>();
	protected AtomicReference<Boolean> areaType= new AtomicReference<>(); // Is Console Text Window
	//
	// There are attributes of the real graphic image:
	//
	protected AtomicReference<MutableAttributeSet> rootTextStyle= new AtomicReference<MutableAttributeSet>(new SimpleAttributeSet());
	protected Map<Color,MutableAttributeSet> transparentTextStyles= Collections.synchronizedMap(new HashMap<Color,MutableAttributeSet>());
	//
	protected ChoisePoint emptyChoisePoint= null;
	//
	// There are auxiliary constants:
	//
	protected static final Color defaultTextColor= Color.BLACK;
	protected static final String defaultFontName= Font.MONOSPACED;
	protected static final int defaultFontSize= 18;
	protected static final int defaultFontStyle= Font.PLAIN;
	protected static final boolean defaultFontStyleIsUnderlined= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public Report() {
		spaceAttributes= new ReportSpaceAttributes();
	}
	public Report(GlobalWorldIdentifier id) {
		super(id);
		spaceAttributes= new ReportSpaceAttributes();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// abstract public Term getBuiltInSlot_E_title();
	// abstract public Term getBuiltInSlot_E_text_color();
	// abstract public Term getBuiltInSlot_E_space_color();
	// abstract public Term getBuiltInSlot_E_font_name();
	// abstract public Term getBuiltInSlot_E_font_size();
	// abstract public Term getBuiltInSlot_E_font_style();
	// abstract public Term getBuiltInSlot_E_x();
	// abstract public Term getBuiltInSlot_E_y();
	// abstract public Term getBuiltInSlot_E_width();
	// abstract public Term getBuiltInSlot_E_height();
	// abstract public Term getBuiltInSlot_E_background_color();
	abstract public Term getBuiltInSlot_E_maximal_line_number();
	abstract public Term getBuiltInSlot_E_area_type();
	//
	abstract public long entry_s_Action_1_i();
	abstract public long entry_s_WindowClosed_0();
	// abstract public long entry_s_Initialize_0();
	// abstract public long entry_s_Start_0();
	// abstract public long entry_s_Stop_0();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set maximalLineNumber
	//
	public void setMaximalLineNumber1s(ChoisePoint iX, Term a1) {
		setMaximalLineNumber(ExtendedMaximalLineNumber.argumentToExtendedMaximalLineNumber(a1,iX));
	}
	public void setMaximalLineNumber(ExtendedMaximalLineNumber value) {
		maximalLineNumber.set(value);
	}
	public void getMaximalLineNumber0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getMaximalLineNumber(iX).toTerm());
	}
	public void getMaximalLineNumber0fs(ChoisePoint iX) {
	}
	public ExtendedMaximalLineNumber getMaximalLineNumber(ChoisePoint iX) {
		ExtendedMaximalLineNumber number= maximalLineNumber.get();
		if (number != null) {
			return number;
		} else {
			Term value= getBuiltInSlot_E_maximal_line_number();
			return ExtendedMaximalLineNumber.argumentToExtendedMaximalLineNumberSafe(value,iX);
		}
	}
	//
	// get/set areaType
	//
	public void setAreaType1s(ChoisePoint iX, Term a1) {
		setAreaType(ReportUtils.argumentToAreaType(a1,iX));
	}
	public void setAreaType(boolean value) {
		areaType.set(value);
	}
	public void getAreaType0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getAreaType(iX);
		result.setNonBacktrackableValue(ReportUtils.areaTypeToTerm(value));
	}
	public void getAreaType0fs(ChoisePoint iX) {
	}
	public boolean getAreaType(ChoisePoint iX) {
		Boolean type= areaType.get();
		if (type != null) {
			return type;
		} else {
			Term value= getBuiltInSlot_E_area_type();
			return ReportUtils.argumentToAreaTypeSafe(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setColor1s(ChoisePoint iX, Term a1) {
		setTextColor(ExtendedColor.argumentToExtendedColor(a1,iX));
	}
	public void setColor2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedColor tColor= ExtendedColor.argumentToExtendedColor(a1,iX);
		ExtendedColor sColor= ExtendedColor.argumentToExtendedColor(a2,iX);
		setTextColor(tColor);
		setSpaceColor(sColor);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setFontName1s(ChoisePoint iX, Term a1) {
		super.setFontName1s(iX,a1);
		changeFont(iX);
	}
	public void setFontSize1s(ChoisePoint iX, Term a1) {
		super.setFontSize1s(iX,a1);
		changeFontSize(iX);
	}
	public void setFontStyle1s(ChoisePoint iX, Term a1) {
		super.setFontStyle1s(iX,a1);
		changeFont(iX);
	}
	//
	public void setFont2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedFontName fName= ExtendedFontName.argumentToExtendedFontName(a1,iX);
		ExtendedFontSize fSize= ExtendedFontSize.argumentToExtendedFontSize(a2,iX);
		setFontName(fName);
		setFontSize(fSize);
		changeFont(iX);
	}
	public void setFont3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ExtendedFontName fName= ExtendedFontName.argumentToExtendedFontName(a1,iX);
		ExtendedFontSize fSize= ExtendedFontSize.argumentToExtendedFontSize(a2,iX);
		ExtendedFontStyle fStyle= ExtendedFontStyle.argumentToExtendedFontStyle(a3,iX);
		setFontName(fName);
		setFontSize(fSize);
		setFontStyle(fStyle);
		changeFont(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void changeBackgroundColor(ExtendedColor eColor, ChoisePoint iX) {
		Color color;
		try {
			color= eColor.getValue();
		} catch (UseDefaultColor e) {
			color= getDefaultBackgroundColor();
		};
		updateRootTextStyleBackground(color);
		synchronized (this) {
			if (canvasSpace != null) {
				// ReportUtils.safelySetBackground(color,canvasSpace.panel);
				canvasSpace.safelySetBackground(color);
			}
		}
	}
	//
	public void changeFont(ChoisePoint iX) {
		// createGraphicWindowIfNecessary(iX,false);
		String name;
		try {
			name= getFontName(iX).getValue();
		} catch (UseDefaultFontName e1) {
			try {
				Term nameOfFont= DefaultOptions.textFontName;
				name= ExtendedFontName.argumentToFontName(nameOfFont,iX);
			} catch (TermIsSymbolDefault e2) {
				name= defaultFontName;
			}
		};
		int size= computeFontSize(iX);
		int style;
		boolean isUnderlined;
		try {
			ExtendedFontStyle eFS= getFontStyle(iX);
			style= eFS.getValue();
			isUnderlined= eFS.isUnderlined();
		} catch (UseDefaultFontStyle e1) {
			try {
				Term styleOfFont= DefaultOptions.textFontStyle;
				style= ExtendedFontStyle.argumentToFontStyleSafe(styleOfFont,iX);
				isUnderlined= ExtendedFontStyle.fontIsUnderlinedSafe(styleOfFont,iX);
			} catch (TermIsSymbolDefault e2) {
				style= defaultFontStyle;
				isUnderlined= defaultFontStyleIsUnderlined;
			}
		};
		updateRootTextStyleUnderline(isUnderlined);
		synchronized (this) {
			if (canvasSpace != null) {
				((ExtendedReportSpace)canvasSpace).setPanelFont(name,style,size,true);
			}
		}
	}
	public void changeFontSize(ChoisePoint iX) {
		// createGraphicWindowIfNecessary(iX,false);
		int size= computeFontSize(iX);
		synchronized (this) {
			if (canvasSpace != null) {
				((ExtendedReportSpace)canvasSpace).setPanelFontSize(size,true);
			}
		}
	}
	protected int computeFontSize(ChoisePoint iX) {
		int size;
		try {
			size= getFontSize(iX).getValue();
		} catch (UseDefaultFontSize e1) {
			synchronized (this) {
				if (graphicWindow != null) {
					try {
						Term sizeOfFont= DefaultOptions.textFontSize;
						size= ExtendedFontSize.argumentToFontSizeSafe(sizeOfFont,iX);
					} catch (TermIsSymbolDefault e2) {
						size= defaultFontSize;
					}
				} else {
					size= ReportSpaceAttributes.automaticFontSizeAdjustment;
				}
			}
		};
		return size;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized (this) {
			Document doc= ((ReportSpaceAttributes)spaceAttributes).getStyledDocument();
			if (doc != null) {
				safelySetText("",doc);
			}
		}
	}
	//
	public void setString1s(ChoisePoint iX, Term text) {
		createGraphicWindowIfNecessary(iX,false);
		String buffer= text.toString(iX);
		synchronized (this) {
			Document doc= ((ReportSpaceAttributes)spaceAttributes).getStyledDocument();
			if (doc != null) {
				safelySetText(buffer,doc);
			}
		}
	}
	//
	public void getString0ff(ChoisePoint iX, PrologVariable result) {
		synchronized (this) {
			String content;
			Document doc= ((ReportSpaceAttributes)spaceAttributes).getStyledDocument();
			if (doc != null) {
				content= safelyGetText(doc);
			} else {
				content= "";
			};
			result.setNonBacktrackableValue(new PrologString(content));
		}
	}
	public void getString0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
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
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected void initiateRegisteredCanvasSpace(CanvasSpace s, ChoisePoint iX) {
		ReportSpaceAttributes reportAttributes= (ReportSpaceAttributes)spaceAttributes;
		reportAttributes.implementValues((ExtendedReportSpace)s);
		reportAttributes.setStaticContext(staticContext);
		((ExtendedReportSpace)s).enableMouseListener();
		s.safelyRepaint();
		s.safelyRevalidate(); // s.panel.revalidate();
	}
	//
	public void saveCanvasSpaceAttributes() {
		((ReportSpaceAttributes)spaceAttributes).collectValues((ExtendedReportSpace)canvasSpace);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected InnerPage createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		boolean restoreWindow= false;
		boolean moveWindowToFront= false;
		if (graphicWindow==null) {
			if (getAreaType(iX)) {
				InternalTextFrame consoleWindow= StaticReportAttributes.retrieveConsoleWindow(staticContext);
				if (consoleWindow==null) {
					ReentrantLock lock= StaticDesktopAttributes.retrieveDesktopGuard(staticContext);
					lock.lock();
					try {
						consoleWindow= StaticReportAttributes.retrieveConsoleWindow(staticContext);
						if (consoleWindow==null) {
							graphicWindow= createInternalFrame(iX);
							StaticReportAttributes.setConsoleWindow((InternalTextFrame)graphicWindow,staticContext);
							restoreWindow= true;
						} else {
							graphicWindow= consoleWindow;
							canvasSpace= graphicWindow.getCanvasSpace(); // scrollPane;
							((ReportSpaceAttributes)spaceAttributes).setStyledDocument(((ExtendedReportSpace)canvasSpace).attributes.getStyledDocument());
							refreshAttributesOfInternalFrame(graphicWindow,iX);
						}
					} finally {
						lock.unlock();
					}
				} else {
					graphicWindow= consoleWindow;
					canvasSpace= graphicWindow.getCanvasSpace(); // scrollPane;
					((ReportSpaceAttributes)spaceAttributes).setStyledDocument(((ExtendedReportSpace)canvasSpace).attributes.getStyledDocument());
					refreshAttributesOfInternalFrame(graphicWindow,iX);
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
			graphicWindow.safelyMoveToFront();
		};
		graphicWindow.safelySetVisible(true);
		return graphicWindow;
	}
	//
	protected InternalTextFrame createInternalFrame(ChoisePoint iX) {
		//
		String title= getTitle(iX).getValueOrDefaultText("");
		//
		((ReportSpaceAttributes)spaceAttributes).setStaticContext(staticContext);
		InternalTextFrame internalTextFrame= new InternalTextFrame(this,title,(ReportSpaceAttributes)spaceAttributes);
		graphicWindow= internalTextFrame;
		Map<AbstractWorld,InternalTextFrame> innerWindows= StaticReportAttributes.retrieveInnerWindows(staticContext);
		innerWindows.put(this,internalTextFrame);
		//
		internalTextFrame.safelySetStyledDocument(((ReportSpaceAttributes)spaceAttributes).getStyledDocument());
		internalTextFrame.safelyAddComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(staticContext);
		desktop.safelyAdd(internalTextFrame.getInternalFrame());
		//
		canvasSpace= internalTextFrame.getCanvasSpace();
		refreshAttributesOfInternalFrame(internalTextFrame,iX);
		//
		return internalTextFrame;
	}
	//
	protected void refreshAttributesOfCanvasSpace(ChoisePoint iX) {
		changeBackgroundColor(iX);
		changeFont(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void sendTheWindowClosedMessage() {
		long domainSignature= entry_s_WindowClosed_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,noArguments,true);
		transmitAsyncCall(call,emptyChoisePoint);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void write_text_buffer(ChoisePoint iX, boolean appendNewLine, StringBuilder textBuffer) {
		if (appendNewLine) {
			textBuffer.append("\n");
		};
		createGraphicWindowIfNecessary(iX,false);
		Color colorOfText;
		try {
			colorOfText= getTextColor(iX).getValue();
		} catch (UseDefaultColor e) {
			colorOfText= defaultTextColor;
		};
		Color colorOfSpace;
		boolean useSpaceColor= true;
		try {
			colorOfSpace= getSpaceColor(iX).getValue();
		} catch (UseDefaultColor e) {
			colorOfSpace= getDefaultBackgroundColor();
			useSpaceColor= false;
		};
		synchronized (this) {
			Document doc= ((ReportSpaceAttributes)spaceAttributes).getStyledDocument();
			if (doc != null) {
				boolean scrollPageToTheEndPosition= true;
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
					long maximalLineNumber= getMaximalLineNumber(iX).getValue();
					safelyRemoveSuperfluousLines(maximalLineNumber,doc);
				} catch (UseWindowHeight e) {
					if (canvasSpace != null) {
						synchronized (canvasSpace) {
							Boolean isSucceded= new Boolean(false);
							((ExtendedReportSpace)canvasSpace).safelyRemoveSuperfluousLines(doc,isSucceded);
							if (isSucceded) {
								scrollPageToTheEndPosition= false;
							}
						}
					}
				} catch (UseNoLimit e) {
				};
				if (canvasSpace != null) {
					if (scrollPageToTheEndPosition) {
						// safelySetCaretPosition(canvasSpace.panel,doc.getLength());
						((ExtendedReportSpace)canvasSpace).safelySetCaretPosition(doc.getLength());
					} else {
						((ExtendedReportSpace)canvasSpace).safelyResetCaretPosition();
					}
				}
			}
		}
	}
	//
	protected static void safelySetText(final String buffer, final Document doc) {
		if (EventQueue.isDispatchThread()) {
			try {
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
				// waitForIdle();
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
				// waitForIdle();
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
				// waitForIdle();
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
	protected void safelyRemoveSuperfluousLines(final long maximalLineNumber, final Document doc) {
		if (EventQueue.isDispatchThread()) {
			removeSuperfluousLines(maximalLineNumber,doc);
		} else {
			try {
				// waitForIdle();
				EventQueue.invokeAndWait(
					new Runnable() {
						public void run() {
							removeSuperfluousLines(maximalLineNumber,doc);
						}
					});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void removeSuperfluousLines(long maximalLineNumber, Document doc) {
		try {
			String plainText= doc.getText(0,doc.getLength());
			long numberOfLines= ReportUtils.calculate_number_of_lines(plainText);
			if (numberOfLines > maximalLineNumber) {
				long endPosition= ReportUtils.calculate_position_of_line_end(plainText,maximalLineNumber);
				doc.remove(0,(int)endPosition+1);
			}
		} catch (BadLocationException e) {
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void updateRootTextStyleUnderline(boolean isUnderlined) {
		synchronized (this) {
			MutableAttributeSet style= rootTextStyle.get();
			StyleConstants.setUnderline(style,isUnderlined);
			rootTextStyle.set(style);
		}
	}
	public void updateRootTextStyleBackground(Color color) {
		synchronized (this) {
			MutableAttributeSet style= rootTextStyle.get();
			StyleConstants.setBackground(style,color);
			rootTextStyle.set(style);
		}
	}
	//
	public MutableAttributeSet getOpaqueTextStyle(Color t, Color s) {
		synchronized (this) {
			MutableAttributeSet textStyle= new SimpleAttributeSet();
			textStyle.setResolveParent(rootTextStyle.get());
			StyleConstants.setForeground(textStyle,t);
			StyleConstants.setBackground(textStyle,s);
			return textStyle;
		}
	}
	//
	public MutableAttributeSet getTransparentTextStyle(Color t) {
		synchronized (this) {
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
	///////////////////////////////////////////////////////////////
	//
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
		isHiddenCustomControl(iX);
	}
}
