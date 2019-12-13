// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.signals;

import morozov.run.*;

public final class InternalNumericalLiteralIsNotRecognized extends LightweightException {
	//
	public static final InternalNumericalLiteralIsNotRecognized instance= new InternalNumericalLiteralIsNotRecognized();
	//
	private InternalNumericalLiteralIsNotRecognized() {
	}
}
