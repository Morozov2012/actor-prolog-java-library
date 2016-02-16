// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;

public abstract class OwnWorld extends AbstractWorld {
	//
	public StaticContext staticContext;
	//
	public OwnWorld() {
	}
	public OwnWorld(GlobalWorldIdentifier id) {
		super(id);
	}
}
