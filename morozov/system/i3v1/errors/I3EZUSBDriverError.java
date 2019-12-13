// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1.errors;

public class I3EZUSBDriverError extends RuntimeException {
	//
	protected int errorCode;
	//
	public I3EZUSBDriverError(int code) {
		errorCode= code;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(errorCode) + ")";
	}
}
