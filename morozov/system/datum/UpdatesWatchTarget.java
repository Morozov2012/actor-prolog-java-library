// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.built_in.*;

public class UpdatesWatchTarget {
	//
	protected DataAbstraction world;
	protected LoadableContainer container;
	//
	public UpdatesWatchTarget(DataAbstraction w, LoadableContainer c) {
		world= w;
		container= c;
	}
}
