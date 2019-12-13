// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.syntax.data.domains.*;
import morozov.terms.*;

public class ActorPrologDomainDefinition {
	//
	protected String name;
	protected ActorPrologDomainDeclarator declarator;
	protected ActorPrologDomainAlternative[] alternatives;
	protected int position;
	//
	public ActorPrologDomainDefinition(String n, ActorPrologDomainDeclarator d, ActorPrologDomainAlternative[] a, int p) {
		name= n;
		declarator= d;
		alternatives= a;
		position= p;
	}
	//
	public String getName() {
		return name;
	}
	public ActorPrologDomainDeclarator getDeclarator() {
		return declarator;
	}
	public ActorPrologDomainAlternative[] getAlternatives() {
		return alternatives;
	}
	public int getPosition() {
		return position;
	}
	//
	public static Term arrayToList(ActorPrologDomainDefinition[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public Term toActorPrologTerm() {
		Term termAlternatives= PrologEmptyList.instance;
		for (int k=alternatives.length-1; k >= 0; k--) {
			termAlternatives= new PrologList(alternatives[k].toActorPrologTerm(),termAlternatives);
		};
		Term[] internalArray= new Term[4];
		internalArray[0]= new PrologString(name);
		internalArray[1]= declarator.toTerm();
		internalArray[2]= termAlternatives;
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_domain,internalArray);
	}
}
