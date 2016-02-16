// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import java.lang.Exception;

public class DataDeserializingError extends RuntimeException {
	public Exception exception;
	public DataDeserializingError(Exception e) {
		super(e);
		exception= e;
	}
	public String toString() {
		return exception.toString();
	}
}
