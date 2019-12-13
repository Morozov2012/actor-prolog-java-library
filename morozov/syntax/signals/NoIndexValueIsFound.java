// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.signals;

import morozov.run.*;

public final class NoIndexValueIsFound extends LightweightException {
	//
	public static final NoIndexValueIsFound instance= new NoIndexValueIsFound();
	//
	private NoIndexValueIsFound() {
	}
}
