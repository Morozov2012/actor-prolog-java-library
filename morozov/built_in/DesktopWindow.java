// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.Window;
import java.awt.Frame;

public abstract class DesktopWindow extends Alpha {
	//
	public DesktopWindow() {
	}
	public DesktopWindow(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getEnvironmentVariable1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		String name= GeneralConverters.argumentToString(a1,iX);
		String value= System.getenv(name);
		if (value != null) {
			result.setNonBacktrackableValue(new PrologString(value));
		} else {
			throw Backtracking.instance;
		}
	}
	public void getEnvironmentVariable1fs(ChoisePoint iX, Term a1) throws Backtracking {
		String name= GeneralConverters.argumentToString(a1,iX);
		String value= System.getenv(name);
		if (value==null) {
			throw Backtracking.instance;
		}
	}
	//
	public void getSystemProperty1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		String name= GeneralConverters.argumentToString(a1,iX);
		String value= System.getProperty(name);
		if (value != null) {
			result.setNonBacktrackableValue(new PrologString(value));
		} else {
			throw Backtracking.instance;
		}
	}
	public void getSystemProperty1fs(ChoisePoint iX, Term a1) throws Backtracking {
		String name= GeneralConverters.argumentToString(a1,iX);
		String value= System.getProperty(name);
		if (value==null) {
			throw Backtracking.instance;
		}
	}
	//
	public void setSystemProperty2s(ChoisePoint iX, Term a1, Term a2) {
		String name= GeneralConverters.argumentToString(a1,iX);
		String value= GeneralConverters.argumentToString(a2,iX);
		System.setProperty(name,value);
	}
	//
	public void removeSystemProperty1s(ChoisePoint iX, Term a1) {
		String name= GeneralConverters.argumentToString(a1,iX);
		System.clearProperty(name);
	}
	//
	public void setExitOnClose1s(ChoisePoint iX, Term a1) {
		boolean mode= YesNoConverters.termYesNo2Boolean(a1,iX);
		StaticDesktopAttributes.setExitOnClose(mode,staticContext);
	}
	//
	public void getExitOnClose0ff(ChoisePoint iX, PrologVariable result) {
		boolean mode= StaticDesktopAttributes.retrieveExitOnClose(staticContext);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(mode));
	}
	public void getExitOnClose0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void show0s(ChoisePoint iX) {
		showDesktopWindow(iX);
	}
	public void show1ms(ChoisePoint iX, Term... args) {
		showDesktopWindow(iX);
	}
	//
	protected void showDesktopWindow(ChoisePoint iX) {
		MainDesktopPane desktop= DesktopUtils.createPaneIfNecessary(staticContext);
		if (desktop != null) {
			DesktopUtils.safelySetVisible(true,desktop);
		};
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			DesktopUtils.safelySetVisible(true,mainWindow);
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
			DesktopUtils.safelySetVisible(true,mainWindow);
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			DesktopUtils.safelySetVisible(false,mainWindow);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void maximize0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
		if (mainWindow != null) {
			if (mainWindow instanceof Frame) {
				Frame frame= (Frame)mainWindow;
				DesktopUtils.safelyMaximize(frame);
			};
			DesktopUtils.safelySetVisible(true,mainWindow);
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
			};
			DesktopUtils.safelySetVisible(true,mainWindow);
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
			};
			DesktopUtils.safelySetVisible(true,mainWindow);
		}
	}
	//
	///////////////////////////////////////////////////////////////
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
	//
	public void collectGarbage0s(ChoisePoint iX) {
		System.runFinalization();
		System.gc();
	}
}
