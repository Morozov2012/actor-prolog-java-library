// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import morozov.terms.*;

public abstract class ActorPrologDomainAlternative {
	//
	protected int position;
	//
	public ActorPrologDomainAlternative(int p) {
		position= p;
	}
	//
	public int getPosition() {
		return position;
	}
	//
	public boolean isDomainName(String name) {
		return false;
	}
	//
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		return this.getClass()==givenItem.getClass();
	}
	//
	abstract public Term toActorPrologTerm();
}
