// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class Lambda extends AbstractInternalWorld {
	//
	public Lambda() {
	}
	public Lambda(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	public void goal0s(ChoisePoint iX) {
	}
	//
	public class Goal0s extends Continuation {
		// private Continuation c0;
		//
		public Goal0s(Continuation aC) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void alarm1s(ChoisePoint iX, Term exceptionName) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	public class Alarm1s extends Continuation {
		public Alarm1s(Continuation aC, Term exceptionName) {
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			throw Backtracking.instance;
		}
	}
}
