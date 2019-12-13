// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import morozov.terms.*;

public abstract class ActorPrologArgumentDomain extends ActorPrologDomainAlternative {
	//
	public ActorPrologArgumentDomain(int p) {
		super(p);
	}
	//
	public static Term arrayToList(ActorPrologArgumentDomain[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
}
