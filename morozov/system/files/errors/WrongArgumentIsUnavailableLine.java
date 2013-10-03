// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

import morozov.terms.*;

public class WrongArgumentIsUnavailableLine extends FileInputOutputError {
	public WrongArgumentIsUnavailableLine(String name, Throwable e) {
		super(name,e);
	}
}
