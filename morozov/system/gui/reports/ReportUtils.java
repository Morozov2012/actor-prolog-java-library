// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import target.*;

import morozov.run.*;
import morozov.system.gui.reports.errors.*;
import morozov.system.gui.reports.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Font;
import java.awt.Color;

import javax.swing.SwingUtilities;

import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class ReportUtils {
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
	public static void safelySetFont(final Font font, final TextPaneNoWrap panel) {
		if (SwingUtilities.isEventDispatchThread()) {
			panel.setFont(font);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
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
	//
	public static long termToMaxLineNumber(Term value, ChoisePoint iX) throws TermIsSymbolNoLimit, TermIsSymbolWindowHeight {
		try {
			return value.getSmallIntegerValue(iX);
		} catch (TermIsNotAnInteger e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_no_limit) {
					throw TermIsSymbolNoLimit.instance;
				} else if (code==SymbolCodes.symbolCode_E_window_height) {
					throw TermIsSymbolWindowHeight.instance;
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
