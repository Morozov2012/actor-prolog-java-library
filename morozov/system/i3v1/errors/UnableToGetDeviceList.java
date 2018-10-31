// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1.errors;

public class UnableToGetDeviceList extends I3EZUSBDriverError {
	public UnableToGetDeviceList(int errorCode) {
		super(errorCode);
	}
}
