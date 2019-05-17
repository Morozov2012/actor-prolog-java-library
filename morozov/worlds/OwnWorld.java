// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;

public abstract class OwnWorld extends AbstractWorld {
	//
	public StaticContext staticContext;
	//
	private static final long serialVersionUID= 0xF479391FAB6F87EL; // 1101010889101211774L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds","OwnWorld");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public OwnWorld() {
	}
	public OwnWorld(GlobalWorldIdentifier id) {
		super(id);
	}
}
