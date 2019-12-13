// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import java.lang.Exception;

public class DataDeserializingError extends RuntimeException {
	//
	protected Exception exception;
	//
	public DataDeserializingError(Exception e) {
		super(e);
		exception= e;
	}
	//
	@Override
	public String toString() {
		return exception.toString();
	}
}
