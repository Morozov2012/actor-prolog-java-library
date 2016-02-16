// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import java.io.IOException;

public class DataSerializingError extends RuntimeException {
	public IOException exception;
	public DataSerializingError(IOException e) {
		super(e);
		exception= e;
	}
	public String toString() {
		return exception.toString();
	}
}
