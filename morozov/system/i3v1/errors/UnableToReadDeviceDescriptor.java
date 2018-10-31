// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1.errors;

public class UnableToReadDeviceDescriptor extends I3EZUSBDriverError {
	public UnableToReadDeviceDescriptor(int errorCode) {
		super(errorCode);
	}
}
