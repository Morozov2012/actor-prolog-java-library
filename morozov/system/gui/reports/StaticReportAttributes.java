// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import morozov.classes.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class StaticReportAttributes extends StaticAttributes {
	//
	private Map<AbstractWorld,InternalTextFrame> innerWindows= Collections.synchronizedMap(new HashMap<AbstractWorld,InternalTextFrame>());
	private InternalTextFrame consoleWindow;
	private static final String staticIdentifier= "Report";
	//
	private static StaticReportAttributes retrieveStaticReportAttributes(StaticContext context) {
		StaticAttributes attributes= context.retrieveAttributes(staticIdentifier);
		if (attributes==null) {
			synchronized(context) {
				attributes= context.retrieveAttributes(staticIdentifier);
				if (attributes==null) {
					attributes= new StaticReportAttributes();
					context.saveAttributes(staticIdentifier,attributes);
				}
			}
		};
		return (StaticReportAttributes)attributes;
	}
	public static Map<AbstractWorld,InternalTextFrame> retrieveInnerWindows(StaticContext context) {
		StaticReportAttributes attributes= retrieveStaticReportAttributes(context);
		Map<AbstractWorld,InternalTextFrame> innerWindows;
		synchronized(attributes) {
			innerWindows= attributes.innerWindows;
		};
		return innerWindows;
	}
	public static void setConsoleWindow(InternalTextFrame textWindow, StaticContext context) {
		StaticReportAttributes attributes= retrieveStaticReportAttributes(context);
		synchronized(attributes) {
			attributes.consoleWindow= textWindow;
		}
	}
	public static InternalTextFrame retrieveConsoleWindow(StaticContext context) {
		StaticReportAttributes attributes= retrieveStaticReportAttributes(context);
		InternalTextFrame consoleWindow;
		synchronized(attributes) {
			consoleWindow= attributes.consoleWindow;
		};
		return consoleWindow;
	}
	// public static void setFileChooser(ExtendedFileChooser chooser, StaticContext context) {
	//	StaticReportAttributes attributes= retrieveStaticReportAttributes(context);
	//	synchronized(attributes) {
	//		attributes.fileChooser= chooser;
	//	}
	// }
	// public static ExtendedFileChooser retrieveFileChooser(StaticContext context) {
	//	StaticReportAttributes attributes= retrieveStaticReportAttributes(context);
	//	ExtendedFileChooser chooser;
	//	synchronized(attributes) {
	//		chooser= attributes.fileChooser;
	//	};
	//	return chooser;
	// }
}
