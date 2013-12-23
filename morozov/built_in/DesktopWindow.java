// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.gui.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Window;
import java.awt.Frame;

public abstract class DesktopWindow extends Alpha {
	//
	public void getEnvironmentVariable1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		try {
			String name= a1.getStringValue(iX);
			String value= System.getenv(name);
			if (value != null) {
				result.value= new PrologString(value);
			} else {
				throw Backtracking.instance;
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	public void getEnvironmentVariable1fs(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			String name= a1.getStringValue(iX);
			String value= System.getenv(name);
			if (value==null) {
				throw Backtracking.instance;
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	//
	public void getSystemProperty1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		try {
			String name= a1.getStringValue(iX);
			String value= System.getProperty(name);
			if (value != null) {
				result.value= new PrologString(value);
			} else {
				throw Backtracking.instance;
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	public void getSystemProperty1fs(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			String name= a1.getStringValue(iX);
			String value= System.getProperty(name);
			if (value==null) {
				throw Backtracking.instance;
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	//
	public void setSystemProperty2s(ChoisePoint iX, Term a1, Term a2) {
		try {
			String name= a1.getStringValue(iX);
			String value= a2.getStringValue(iX);
			System.setProperty(name,value);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	//
	public void removeSystemProperty1s(ChoisePoint iX, Term a1) {
		try {
			String name= a1.getStringValue(iX);
			System.clearProperty(name);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	//
	public void show0s(ChoisePoint iX) {
		MainDesktopPane desktop= DesktopUtils.createPaneIfNecessary(staticContext);
		if (desktop != null) {
			DesktopUtils.safelySetVisible(true,desktop);
		};
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				DesktopUtils.safelySetVisible(true,frame);
			}
		}
	}
	//
	public void redraw0s(ChoisePoint iX) {
		MainDesktopPane desktop= DesktopUtils.createPaneIfNecessary(staticContext);
		if (desktop != null) {
			DesktopUtils.safelyRepaint(desktop);
		};
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			DesktopUtils.safelyRepaint(mainWindow);
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		// MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		// if (desktop != null) {
		//	DesktopUtils.safelySetVisible(false,desktop);
		// };
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			DesktopUtils.safelySetVisible(false,mainWindow);
		}
	}
	//
	public void maximize0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				DesktopUtils.safelyMaximize(frame);
			}
		}
	}
	//
	public void minimize0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				DesktopUtils.safelyMinimize(frame);
			}
		}
	}
	//
	public void restore0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				DesktopUtils.safelyRestore(frame);
			}
		}
	}
	//
	public void isVisible0s(ChoisePoint iX) throws Backtracking {
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (!DesktopUtils.safelyIsVisible(mainWindow)) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (!DesktopUtils.safelyIsHidden(mainWindow)) {
				throw Backtracking.instance;
			}
		// } else {
		//	throw Backtracking.instance;
		}
	}
	//
	public void isMaximized0s(ChoisePoint iX) throws Backtracking {
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				if (!DesktopUtils.safelyIsMaximized(frame)) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isMinimized0s(ChoisePoint iX) throws Backtracking {
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				if (!DesktopUtils.safelyIsMinimized(frame)) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isRestored0s(ChoisePoint iX) throws Backtracking {
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				if (!DesktopUtils.safelyIsRestored(frame)) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
}
