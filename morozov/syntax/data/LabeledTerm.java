// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.terms.*;

public class LabeledTerm {
	//
	protected long integerKey;
	protected boolean hasSymbolicName= false;
	protected Term value;
	protected int position;
	//
	public LabeledTerm(long code, boolean mode, Term v, int p) {
		integerKey= code;
		hasSymbolicName= mode;
		value= v;
		position= p;
	}
	//
	public long getKeyCode() {
		if (hasSymbolicName) {
			return -integerKey;
		} else {
			return integerKey;
		}
	}
	public Term getKeyTerm() {
		if (hasSymbolicName) {
			return new PrologSymbol(integerKey);
		} else {
			return new PrologInteger(integerKey);
		}
	}
	public Term getValue() {
		return value;
	}
	public int getPosition() {
		return position;
	}
	//
	@Override
	public String toString() {
		return String.format("%d:",integerKey) + value.toString();
	}
}
