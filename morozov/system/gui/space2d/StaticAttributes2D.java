// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.run.*;
import morozov.worlds.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class StaticAttributes2D extends StaticAttributes {
	//
	private Map<AbstractWorld,InternalFrame2D> innerWindows= Collections.synchronizedMap(new HashMap<AbstractWorld,InternalFrame2D>());
	private static final String staticIdentifier= "Canvas2D";
	//
	private static StaticAttributes2D retrieveStaticAttributes2D(StaticContext context) {
		StaticAttributes attributes= context.retrieveAttributes(staticIdentifier);
		if (attributes==null) {
			synchronized(context) {
				attributes= context.retrieveAttributes(staticIdentifier);
				if (attributes==null) {
					attributes= new StaticAttributes2D();
					context.saveAttributes(staticIdentifier,attributes);
				}
			}
		};
		return (StaticAttributes2D)attributes;
	}
	public static Map<AbstractWorld,InternalFrame2D> retrieveInnerWindows(StaticContext context) {
		StaticAttributes2D attributes= retrieveStaticAttributes2D(context);
		Map<AbstractWorld,InternalFrame2D> innerWindows;
		synchronized(attributes) {
			innerWindows= attributes.innerWindows;
		};
		return innerWindows;
	}
}
