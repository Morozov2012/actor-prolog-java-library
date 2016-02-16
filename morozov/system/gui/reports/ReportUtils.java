// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import target.*;

import morozov.run.*;
import morozov.system.gui.reports.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.SwingUtilities;

import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class ReportUtils {
	//
	protected static Term commonTerm= new PrologSymbol(SymbolCodes.symbolCode_E_common);
	protected static Term separateTerm= new PrologSymbol(SymbolCodes.symbolCode_E_separate);
	//
	protected static final Font font01= new Font(Font.MONOSPACED,0,1);
	protected static final Font font02= new Font(Font.MONOSPACED,0,2);
	//
	public static long calculate_number_of_lines(String text) {
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
	public static long calculate_position_of_line_end(String text, long n) {
		int bound= text.length()-1;
		long counter= n;
		for(int i=bound; i >= 0; i--) {
			int c= text.codePointAt(i);
			if (c == '\n') {
				if (i != bound) {
					counter= counter - 1;
				}
			};
			if (counter <= 0) {
				return i;
			}
		};
		return text.length();
	}
	//
	public static void safelySetText(final String text, final TextPaneNoWrap panel) {
		if (SwingUtilities.isEventDispatchThread()) {
			panel.setText(text);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						panel.setText(text);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public static String safelyGetText(final TextPaneNoWrap panel) {
		if (SwingUtilities.isEventDispatchThread()) {
			return panel.getText();
		} else {
			final StringBuilder text= new StringBuilder();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						text.append(panel.getText());
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return text.toString();
		}
	}
	//
	public static void safelySetFont(final Font font, final TextPaneNoWrap panel, final boolean invokeUpdate) {
		if (SwingUtilities.isEventDispatchThread()) {
			if (invokeUpdate) {
				panel.setFont(font01);
				panel.setFont(font02);
			};
			panel.setFont(font);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						if (invokeUpdate) {
							panel.setFont(font01);
							panel.setFont(font02);
						};
						panel.setFont(font);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public static Font safelyGetFont(final TextPaneNoWrap panel) {
		if (SwingUtilities.isEventDispatchThread()) {
			return panel.getFont();
		} else {
			final AtomicReference<Font> font= new AtomicReference<Font>();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						font.set(panel.getFont());
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return font.get();
		}
	}
	//
	public static FontMetrics safelyGetFontMetrics(final TextPaneNoWrap panel) {
		if (SwingUtilities.isEventDispatchThread()) {
			return panel.getFontMetrics(panel.getFont());
		} else {
			final AtomicReference<FontMetrics> value= new AtomicReference<FontMetrics>();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(panel.getFontMetrics(panel.getFont()));
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return value.get();
		}
	}
	//
/*
	public static void safelySetBackground(final Color color, final TextPaneNoWrap panel) {
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
*/
	public static boolean termToAreaTypeSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return false;
		} else {
			try {
				return termToAreaType(value,iX);
			} catch (RuntimeException e) {
				return false;
			}
		}
	}
	public static boolean termToAreaType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_separate) {
				return false;
			} else if (code==SymbolCodes.symbolCode_E_common) {
				return true;
			} else {
				throw new WrongArgumentIsNotAreaType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotAreaType(value);
		}
	}
	public static Term areaTypeToTerm(boolean value) {
		if (value) {
			return commonTerm;
		} else {
			return separateTerm;
		}
	}
}
