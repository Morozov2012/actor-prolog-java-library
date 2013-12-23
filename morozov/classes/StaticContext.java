// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.system.gui.*;

import java.awt.Window;
import javax.swing.JApplet;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StaticContext {
	//
	private static HashSet<Window> ownerlessWindows= new HashSet<Window>();
	//
	private AbstractProcess rootProcess;
	private JApplet applet;
	private String[] arguments;
	private Window systemWindow;
	//
	private Map<String,StaticAttributes> contextList= Collections.synchronizedMap(new HashMap<String,StaticAttributes>());
	//
	public StaticContext(AbstractProcess self, JApplet program, String[] args, Window[] windows) {
		rootProcess= self;
		applet= program;
		arguments= args;
		LookAndFeelUtils.assignLookAndFeel();
		if (windows.length > 0) {
			for (int i=windows.length-1; i >= 0; i--) {
				if (!ownerlessWindows.contains(windows[i])) {
					systemWindow= windows[i];
					ownerlessWindows.add(systemWindow);
					break;
				}
			};
			if (systemWindow==null) {
				systemWindow= windows[windows.length-1];
			}
		}
	}
	//
	public void saveAttributes(String key, StaticAttributes value) {
		contextList.put(key,value);
	}
	public StaticAttributes retrieveAttributes(String key) {
		return contextList.get(key);
	}
	//
	public static AbstractProcess retrieveRootProcess(StaticContext context) {
		return context.rootProcess;
	}
	public static JApplet retrieveApplet(StaticContext context) {
		return context.applet;
	}
	public static String[] retrieveArguments(StaticContext context) {
		return context.arguments;
	}
	public static Window retrieveSystemWindow(StaticContext context) {
		return context.systemWindow;
	}
}
