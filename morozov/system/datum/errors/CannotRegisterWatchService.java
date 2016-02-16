// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.system.files.errors.*;

public class CannotRegisterWatchService extends FileAccessError {
	public CannotRegisterWatchService(String name) {
		super(name);
	}
}
