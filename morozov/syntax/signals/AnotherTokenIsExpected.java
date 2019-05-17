// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.signals;

import morozov.run.*;

public final class AnotherTokenIsExpected extends LightweightException {
	public static final AnotherTokenIsExpected instance= new AnotherTokenIsExpected();
	//
	private AnotherTokenIsExpected() {
	}
}
