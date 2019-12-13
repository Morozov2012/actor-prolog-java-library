// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.worlds.remote.signals;

import morozov.run.*;

public final class OwnWorldIsNotBufferedImage extends LightweightException {
	//
	public static final OwnWorldIsNotBufferedImage instance= new OwnWorldIsNotBufferedImage();
	//
	private OwnWorldIsNotBufferedImage() {
	}
}
