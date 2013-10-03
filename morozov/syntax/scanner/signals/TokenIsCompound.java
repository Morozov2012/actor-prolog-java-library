// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.signals;

import morozov.run.*;

public final class TokenIsCompound extends LightweightException {
	public static final TokenIsCompound instance= new TokenIsCompound();
	//
	private TokenIsCompound() {
	}
}
