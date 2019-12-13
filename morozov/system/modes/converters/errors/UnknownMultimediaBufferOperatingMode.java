// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.modes.converters.errors;

import morozov.system.modes.*;

public class UnknownMultimediaBufferOperatingMode extends RuntimeException {
	//
	protected MultimediaBufferOperatingMode mode;
	//
	public UnknownMultimediaBufferOperatingMode(MultimediaBufferOperatingMode m) {
		mode= m;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + mode.toString() + ")";
	}
}
