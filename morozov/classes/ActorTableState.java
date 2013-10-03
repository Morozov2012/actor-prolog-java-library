// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.classes.errors.*;
import morozov.run.*;
import morozov.terms.*;

import java.util.HashSet;

public class ActorTableState extends Term {
	private HashSet<ActorNumber> table;
	private ActorNumber insertedActor;
	public ActorTableState(HashSet<ActorNumber> actorTable, ActorNumber actor) {
		table= actorTable;
		insertedActor= actor;
	}
	public void clear() {
		table.remove(insertedActor);
	}
	// General "Unify With" function
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	public String toString() {
		return "ActorTableState:" + insertedActor;
	}
}
