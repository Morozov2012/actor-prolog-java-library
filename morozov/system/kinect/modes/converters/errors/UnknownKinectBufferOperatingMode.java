// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters.errors;

import morozov.system.kinect.modes.*;

public class UnknownKinectBufferOperatingMode extends RuntimeException {
	protected KinectBufferOperatingMode mode;
	public UnknownKinectBufferOperatingMode(KinectBufferOperatingMode m) {
		mode= m;
	}
	public String toString() {
		return this.getClass().toString() + "(" + mode.toString() + ")";
	}
}
