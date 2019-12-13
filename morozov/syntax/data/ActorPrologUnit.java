// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.terms.*;

public abstract class ActorPrologUnit {
	//
	protected long classNameCode;
	protected int position;
	//
	public ActorPrologUnit(
			long cNC,
			int p) {
		classNameCode= cNC;
		position= p;
	}
	//
	public long getClassNameCode() {
		return classNameCode;
	}
	public int getPosition() {
		return position;
	}
	//
	public static Term arrayToList(ActorPrologUnit[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public static Term getTermAncestorCode(long nameCode) {
		Term termAncestor;
		if (nameCode < 0) {
			termAncestor= PrologUnknownValue.instance;
		} else {
			termAncestor= new PrologSymbol(nameCode);
		};
		return termAncestor;
	}
	//
	abstract public Term toActorPrologTerm();
}
