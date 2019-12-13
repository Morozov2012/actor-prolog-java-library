// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.terms.*;

public class InternalTerm {
	//
	protected Term value;
	protected int position;
	//
	public InternalTerm(Term v, int p) {
		value= v;
		position= p;
	}
	//
	public Term getValue() {
		return value;
	}
	public int getPosition() {
		return position;
	}
	//
	@Override
	public String toString() {
		return value.toString();
	}
}
