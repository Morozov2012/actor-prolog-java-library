// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.modes.converters.errors;

import morozov.system.modes.*;

public class UnknownDataAcquisitionBufferOperatingMode extends RuntimeException {
	//
	protected DataAcquisitionBufferOperatingMode mode;
	//
	public UnknownDataAcquisitionBufferOperatingMode(DataAcquisitionBufferOperatingMode m) {
		mode= m;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + mode.toString() + ")";
	}
}
