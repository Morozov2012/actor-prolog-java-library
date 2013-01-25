// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax;

import morozov.terms.*;

class NamedTerm {
	public long nameCode;
	public Term value;
	public int position;
	public NamedTerm(long code, Term v, int p) {
		nameCode= code;
		value= v;
		position= p;
	}
	public int getPosition() {
		return position;
	}
	public String toString() {
		return String.format("%d:",nameCode) + value.toString();
	}
}
