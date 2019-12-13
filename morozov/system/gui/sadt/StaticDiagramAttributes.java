// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import morozov.run.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StaticDiagramAttributes extends StaticAttributes {
	//
	protected Map<String,InternalDiagramFrame> innerWindows= Collections.synchronizedMap(new HashMap<String,InternalDiagramFrame>());
	protected static final String staticIdentifier= "_SADT";
	//
	private static StaticDiagramAttributes retrieveStaticDiagramAttributes(StaticContext context) {
		StaticAttributes attributes= context.retrieveAttributes(staticIdentifier);
		if (attributes==null) {
			synchronized (context) {
				attributes= context.retrieveAttributes(staticIdentifier);
				if (attributes==null) {
					attributes= new StaticDiagramAttributes();
					context.saveAttributes(staticIdentifier,attributes);
				}
			}
		};
		return (StaticDiagramAttributes)attributes;
	}
	public static Map<String,InternalDiagramFrame> retrieveInnerWindows(StaticContext context) {
		StaticDiagramAttributes attributes= retrieveStaticDiagramAttributes(context);
		Map<String,InternalDiagramFrame> innerWindows;
		synchronized (attributes) {
			innerWindows= attributes.innerWindows;
		};
		return innerWindows;
	}
}
