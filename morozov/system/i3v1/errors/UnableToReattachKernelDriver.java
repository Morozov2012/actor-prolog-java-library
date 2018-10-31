// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1.errors;

public class UnableToReattachKernelDriver extends I3EZUSBDriverError {
	public UnableToReattachKernelDriver(int errorCode) {
		super(errorCode);
	}
}
