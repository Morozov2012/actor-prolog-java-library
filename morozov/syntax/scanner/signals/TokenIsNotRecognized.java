// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.signals;

import morozov.run.*;

public final class TokenIsNotRecognized extends LightweightException {
	//
	public static final TokenIsNotRecognized instance= new TokenIsNotRecognized();
	//
	private TokenIsNotRecognized() {
	}
}
