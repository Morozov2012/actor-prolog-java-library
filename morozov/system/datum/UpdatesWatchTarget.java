// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.built_in.*;

public class UpdatesWatchTarget {
	//
	public DataAbstraction world;
	public LoadableContainer container;
	//
	public UpdatesWatchTarget(DataAbstraction w, LoadableContainer c) {
		world= w;
		container= c;
	}
}
