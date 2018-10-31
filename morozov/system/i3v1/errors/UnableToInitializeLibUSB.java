// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1.errors;

public class UnableToInitializeLibUSB extends I3EZUSBDriverError {
	public UnableToInitializeLibUSB(int errorCode) {
		super(errorCode);
	}
}
