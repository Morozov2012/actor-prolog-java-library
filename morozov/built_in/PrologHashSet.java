// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.system.datum.*;
import morozov.worlds.*;

public abstract class PrologHashSet extends Database {
	//
	public PrologHashSet() {
	}
	public PrologHashSet(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	@Override
	public DatabaseType getDatabaseType() {
		return DatabaseType.HASH_SET;
	}
}
