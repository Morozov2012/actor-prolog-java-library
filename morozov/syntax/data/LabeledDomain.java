// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.syntax.data.domains.*;
import morozov.terms.*;

public class LabeledDomain {
	//
	protected long integerKey;
	protected boolean hasSymbolicName= false;
	protected ActorPrologArgumentDomain domain;
	protected int position;
	//
	public LabeledDomain(long code, boolean mode, ActorPrologArgumentDomain d, int p) {
		integerKey= code;
		hasSymbolicName= mode;
		domain= d;
		position= p;
	}
	//
	public long getIntegerKey() {
		return integerKey;
	}
	public boolean hasSymbolicName() {
		return hasSymbolicName;
	}
	public ActorPrologArgumentDomain getDomain() {
		return domain;
	}
	public int getPosition() {
		return position;
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
	//
	@Override
	public String toString() {
		return String.format("%d:",integerKey) + domain.toString();
	}
}
