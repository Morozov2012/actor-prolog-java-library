// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters.errors;

import morozov.system.kinect.modes.*;

public class UnknownColorMapName extends RuntimeException {
	protected ColorMapName name;
	public UnknownColorMapName(ColorMapName n) {
		name= n;
	}
	public String toString() {
		return this.getClass().toString() + "(" + name.toString() + ")";
	}
}
