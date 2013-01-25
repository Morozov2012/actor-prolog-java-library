// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import target.*;

import morozov.terms.*;

import java.awt.Font;
import java.awt.Color;

import javax.swing.SwingUtilities;

import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class ReportUtils {
	public static void safelySetText(final String text, final InternalTextFrame textWindow) {
		if (SwingUtilities.isEventDispatchThread()) {
			textWindow.panel.setText(text);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						textWindow.panel.setText(text);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public static String safelyGetText(final InternalTextFrame textWindow) {
		if (SwingUtilities.isEventDispatchThread()) {
			return textWindow.panel.getText();
		} else {
			final StringBuilder text= new StringBuilder();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						text.append(textWindow.panel.getText());
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return text.toString();
		}
	}
	//
	public static void safelySetFont(final Font font, final InternalTextFrame textWindow) {
		if (SwingUtilities.isEventDispatchThread()) {
			textWindow.panel.setFont(font);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						textWindow.panel.setFont(font);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public static Font safelyGetFont(final InternalTextFrame textWindow) {
		if (SwingUtilities.isEventDispatchThread()) {
			return textWindow.panel.getFont();
		} else {
			final AtomicReference<Font> font= new AtomicReference<Font>();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						font.set(textWindow.panel.getFont());
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return font.get();
		}
	}
	//
	public static void safelySetBackground(final Color color, final InternalTextFrame textWindow) {
		if (SwingUtilities.isEventDispatchThread()) {
			textWindow.panel.setBackground(color);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						textWindow.panel.setBackground(color);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public static long termToMaxLineNumber(Term value, ChoisePoint iX) throws TermIsSymbolNoLimit, TermIsSymbolWindowHeight {
		try {
			return value.getSmallIntegerValue(iX);
		} catch (TermIsNotAnInteger e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_no_limit) {
					throw new TermIsSymbolNoLimit();
				} else if (code==SymbolCodes.symbolCode_E_window_height) {
					throw new TermIsSymbolWindowHeight();
				} else {
					throw new WrongTermIsNotMaxLineNumber(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongTermIsNotMaxLineNumber(value);
			}
		}
	}
	//
	public static boolean isConsoleTextWindow(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_separate) {
				return false;
			} else if (code==SymbolCodes.symbolCode_E_common) {
				return true;
			} else {
				throw new WrongTermIsNotAreaType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotAreaType(value);
		}
	}
}
