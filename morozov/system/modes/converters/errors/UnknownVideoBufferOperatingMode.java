// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.modes.converters.errors;

import morozov.system.modes.*;

public class UnknownVideoBufferOperatingMode extends RuntimeException {
	protected VideoBufferOperatingMode mode;
	public UnknownVideoBufferOperatingMode(VideoBufferOperatingMode m) {
		mode= m;
	}
	public String toString() {
		return this.getClass().toString() + "(" + mode.toString() + ")";
	}
}
