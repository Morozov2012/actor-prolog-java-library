// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.system.files.errors.*;

public class CannotAccessSharedDataStore extends FileAccessError {
	public CannotAccessSharedDataStore(String name) {
		super(name);
	}
}
