// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.classes.*;
import morozov.run.*;
import morozov.terms.*;

public abstract class Lambda extends AbstractWorld {
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
		throw new Backtracking();
	}
	//
	public class Alarm1s extends Continuation {
		public Alarm1s(Continuation aC, Term exceptionName) {
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			throw new Backtracking();
		}
	}
}
