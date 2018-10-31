// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1.errors;

public class UnableToReadConfigDescriptor extends I3EZUSBDriverError {
	public UnableToReadConfigDescriptor(int errorCode) {
		super(errorCode);
	}
}
