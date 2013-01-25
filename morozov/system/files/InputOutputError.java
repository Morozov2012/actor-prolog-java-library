// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.files;

public class InputOutputError extends RuntimeException {
	public InputOutputError(Throwable e) {
		super(e);
	}
}
