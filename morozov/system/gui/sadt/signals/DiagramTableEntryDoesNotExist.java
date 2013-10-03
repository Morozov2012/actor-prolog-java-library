// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt.signals;

import morozov.run.*;

public final class DiagramTableEntryDoesNotExist extends LightweightException {
	public String identifier;
	public DiagramTableEntryDoesNotExist(String label) {
		identifier= label;
	}
}
