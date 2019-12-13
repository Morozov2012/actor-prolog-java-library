// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class UnifyTerms extends Continuation {
	//
	private Term value1;
	private Term value2;
	//
	public UnifyTerms(Continuation aC, Term t1, Term t2) {
		c0= aC;
		value1= t1;
		value2= t2;
	}
	//
	@Override
	public void execute(ChoisePoint iX) throws Backtracking {
		value1.unifyWith(value2,iX);
		c0.execute(iX);
	}
}
