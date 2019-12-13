// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import java.io.IOException;

public class DataSerializingError extends RuntimeException {
	//
	protected IOException exception;
	//
	public DataSerializingError(IOException e) {
		super(e);
		exception= e;
	}
	//
	@Override
	public String toString() {
		return exception.toString();
	}
}
