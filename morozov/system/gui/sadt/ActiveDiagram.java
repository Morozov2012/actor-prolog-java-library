// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.worlds.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

public class ActiveDiagram implements ProcessStateListener {
	//
	protected Map<String,AbstractProcess> diagramComponents;
	protected StaticContext staticContext;
	protected Map<String,ComponentState> componentSuccess= Collections.synchronizedMap(new HashMap<String,ComponentState>());
	protected DiagramColors diagramColors= new DiagramColors();
	//
	public ActiveDiagram(Map<String,AbstractProcess> components, StaticContext context) {
		diagramComponents= components;
		staticContext= context;
		Set<String> keys= components.keySet();
		Iterator<String> iterator= keys.iterator();
		while (iterator.hasNext()) {
			String identifier= iterator.next();
			AbstractProcess process= components.get(identifier);
			if (process!=null) {
				process.sendStateRequest(this,identifier);
			}
		}
	}
	//
	public void showModel(String identifier) {
		DiagramUtils.showDiagramPage(identifier,staticContext,diagramColors,diagramComponents,componentSuccess);
	}
	//
	public void showDescription(String identifier) {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(staticContext);
		DiagramUtils.showNote(desktop,identifier,staticContext);
	}
	//
	@Override
	public void rememberStateOfProcess(String identifier, boolean isProven, boolean isSuspended) {
		ComponentState state= componentSuccess.get(identifier);
		if (state==null) {
			componentSuccess.put(identifier,new ComponentState(isProven,isSuspended));
		} else if (state.equals(isProven,isSuspended)) {
			return;
		} else {
			state.set(isProven,isSuspended);
		};
		DiagramUtils.safelyRepaintAllDiagrams(staticContext);
	}
}
