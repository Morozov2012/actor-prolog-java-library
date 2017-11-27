// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.worlds.remote.signals;

import morozov.run.*;

public final class OwnWorldIsNotVideoProcessingMachine extends LightweightException {
	public static final OwnWorldIsNotVideoProcessingMachine instance= new OwnWorldIsNotVideoProcessingMachine();
	//
	private OwnWorldIsNotVideoProcessingMachine() {
	}
}
