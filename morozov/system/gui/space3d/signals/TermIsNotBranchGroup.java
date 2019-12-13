// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.signals;

import morozov.run.*;

public final class TermIsNotBranchGroup extends LightweightException {
	//
	public static final TermIsNotBranchGroup instance= new TermIsNotBranchGroup();
	//
	private TermIsNotBranchGroup() {
	}
}
