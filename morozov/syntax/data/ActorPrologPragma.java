// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public class ActorPrologPragma {
	//
	protected String name;
	protected Term attribute;
	protected int position;
	//
	public ActorPrologPragma(String n, Term a, int p) {
		name= n;
		attribute= a;
		position= p;
	}
	//
	public String getName() {
		return name;
	}
	public Term getAtribute() {
		return attribute;
	}
	public int getPosition() {
		return position;
	}
	//
	public static Term arrayToList(ActorPrologPragma[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[3];
		internalArray[0]= new PrologString(name);
		internalArray[1]= attribute;
		internalArray[2]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_pragma_statement,internalArray);
	}
}
