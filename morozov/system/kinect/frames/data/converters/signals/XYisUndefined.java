// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters.signals;

import morozov.run.*;

public final class XYisUndefined extends LightweightException {
	public static final XYisUndefined instance= new XYisUndefined();
	//
	private XYisUndefined() {
	}
}
