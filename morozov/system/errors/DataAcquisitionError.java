// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.errors;

public class DataAcquisitionError extends RuntimeException {
	//
	protected Throwable exception;
	//
	public DataAcquisitionError(Throwable e) {
		super(e);
		exception= e;
	}
	//
	public Throwable getException() {
		return exception;
	}
	//
	public String toString() {
		return exception.toString();
	}
}
