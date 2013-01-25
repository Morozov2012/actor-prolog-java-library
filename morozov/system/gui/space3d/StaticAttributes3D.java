// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.classes.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class StaticAttributes3D extends StaticAttributes {
	//
	private Map<AbstractWorld,InternalFrame3D> innerWindows= Collections.synchronizedMap(new HashMap<AbstractWorld,InternalFrame3D>());
	private static final String staticIdentifier= "Canvas3D";
	//
	private static StaticAttributes3D retrieveStaticAttributes3D(StaticContext context) {
		StaticAttributes attributes= context.retrieveAttributes(staticIdentifier);
		if (attributes==null) {
			synchronized(context) {
				attributes= context.retrieveAttributes(staticIdentifier);
				if (attributes==null) {
					attributes= new StaticAttributes3D();
					context.saveAttributes(staticIdentifier,attributes);
				}
			}
		};
		return (StaticAttributes3D)attributes;
	}
	public static Map<AbstractWorld,InternalFrame3D> retrieveInnerWindows(StaticContext context) {
		StaticAttributes3D attributes= retrieveStaticAttributes3D(context);
		Map<AbstractWorld,InternalFrame3D> innerWindows;
		synchronized(attributes) {
			innerWindows= attributes.innerWindows;
		};
		return innerWindows;
	}
}
