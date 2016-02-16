// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.worlds.*;

public abstract class DemoConsole extends Console {
	public DemoConsole() {
	}
	public DemoConsole(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void goal0s(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	public class Goal0s extends Continuation {
		//
		public Goal0s(Continuation aC) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			throw Backtracking.instance;
		}
	}
}
