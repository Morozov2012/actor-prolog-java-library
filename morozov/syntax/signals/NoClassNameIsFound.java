// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.signals;

import morozov.run.*;

public final class NoClassNameIsFound extends LightweightException {
	//
	public static final NoClassNameIsFound instance= new NoClassNameIsFound();
	//
	private NoClassNameIsFound() {
	}
}
