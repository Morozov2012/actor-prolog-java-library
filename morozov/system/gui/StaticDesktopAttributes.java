// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.run.*;

import java.awt.Component;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.util.concurrent.locks.ReentrantLock;

public class StaticDesktopAttributes extends StaticAttributes {
	//
	private MainDesktopPane desktop;
	private ReentrantLock desktopGuard= new ReentrantLock();
	private int previousDefaultPosition= -1;
	private Window mainWindow;
	private Window topLevelWindow;
	private static final String staticIdentifier= "_Desktop";
	private boolean exitOnClose= true;
	//
	private static StaticDesktopAttributes retrieveStaticDesktopAttributes(StaticContext context) {
		StaticAttributes attributes= context.retrieveAttributes(staticIdentifier);
		if (attributes==null) {
			synchronized (context) {
				attributes= context.retrieveAttributes(staticIdentifier);
				if (attributes==null) {
					attributes= new StaticDesktopAttributes();
					context.saveAttributes(staticIdentifier,attributes);
				}
			}
		};
		return (StaticDesktopAttributes)attributes;
	}
	public static void setMainDesktopPane(MainDesktopPane desktop, StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		synchronized (attributes) {
			attributes.desktop= desktop;
		}
	}
	public static MainDesktopPane retrieveMainDesktopPane(StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		MainDesktopPane desktop;
		synchronized (attributes) {
			desktop= attributes.desktop;
		};
		return desktop;
	}
	public static ReentrantLock retrieveDesktopGuard(StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		ReentrantLock guard;
		synchronized (attributes) {
			guard= attributes.desktopGuard;
		};
		return guard;
	}
	public static void resetDefaultPosition(StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		synchronized (attributes) {
			attributes.previousDefaultPosition= -1;
		}
	}
	public static int increaseDefaultPosition(StaticContext context, int step, int realWidth, int realHeight, int desktopWidth, int desktopHeight) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		int position;
		synchronized (attributes) {
			position= attributes.previousDefaultPosition;
			if (position < 0) {
				position= 0;
			} else {
				position= position + 1;
				int realY= step * position;
				int realX= step * position;
				if (	( realY + realHeight > desktopHeight ) ||
					( realX + realWidth > desktopWidth ) ) {
					position= 0;
				}
			};
			attributes.previousDefaultPosition= position;
		};
		return position;
	}
	public static int increaseDefaultPosition(StaticContext context, int step, int realSize, int desktopSize) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		int position;
		synchronized (attributes) {
			position= attributes.previousDefaultPosition;
			if (position < 0) {
				position= 0;
			} else {
				position= position + 1;
				int realPosition= step * position;
				if (realPosition + realSize > desktopSize) {
					position= 0;
				}
			};
			attributes.previousDefaultPosition= position;
		};
		return position;
	}
	public static void setMainWindow(Window window, StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		synchronized (attributes) {
			attributes.mainWindow= window;
		}
	}
	public static Window retrieveMainWindow(StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		Window window;
		synchronized (attributes) {
			window= attributes.mainWindow;
		};
		return window;
	}
	public static void setTopLevelWindow(Window window, StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		synchronized (attributes) {
			attributes.topLevelWindow= window;
		}
	}
	public static Window retrieveTopLevelWindow(StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		Window window;
		synchronized (attributes) {
			window= attributes.topLevelWindow;
		};
		return window;
	}
	public static Component retrieveTopLevelWindowOrDesktopPane(StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		synchronized (attributes) {
			Window window= attributes.topLevelWindow;
			if (window != null) {
				return window;
			} else {
				MainDesktopPane desktop= attributes.desktop;
				return desktop;
			}
		}
	}
	public static void setExitOnClose(boolean mode, StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		synchronized (attributes) {
			attributes.exitOnClose= mode;
			Window mainWindow= attributes.mainWindow;
			if (mainWindow != null) {
				if (mainWindow instanceof JFrame) {
					JFrame frame= (JFrame)mainWindow;
					try {
						if (mode) {
							frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
						} else {
							// frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
						}
					} catch (SecurityException e) {
						// frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
					}
				}
			}
		}
	}
	public static boolean retrieveExitOnClose(StaticContext context) {
		StaticDesktopAttributes attributes= retrieveStaticDesktopAttributes(context);
		boolean mode;
		synchronized (attributes) {
			mode= attributes.exitOnClose;
		};
		return mode;
	}
}
