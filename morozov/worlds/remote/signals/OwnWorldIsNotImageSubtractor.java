// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.worlds.remote.signals;

import morozov.run.*;

public final class OwnWorldIsNotImageSubtractor extends LightweightException {
	public static final OwnWorldIsNotImageSubtractor instance= new OwnWorldIsNotImageSubtractor();
	//
	private OwnWorldIsNotImageSubtractor() {
	}
}
