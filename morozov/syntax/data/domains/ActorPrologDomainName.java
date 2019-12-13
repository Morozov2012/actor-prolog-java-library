// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import morozov.terms.*;

public class ActorPrologDomainName extends ActorPrologArgumentDomain {
	//
	protected String name;
	//
	public ActorPrologDomainName(String n, int p) {
		super(p);
		name= n;
	}
	//
	public String getName() {
		return name;
	}
	//
	@Override
	public boolean isDomainName(String givenName) {
		return name.equals(givenName);
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainName) {
			ActorPrologDomainName givenInstance= (ActorPrologDomainName)givenItem;
			return getName().equals(givenInstance.getName());
		} else {
			return false;
		}
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return new PrologString(name);
	}
}
