// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotASet extends LightweightException {
	public static final TermIsNotASet instance= new TermIsNotASet();
	//
	private TermIsNotASet() {
	}
}
