// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.errors.*;
import morozov.run.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class SlotVariableValueState extends Term {
	protected HashSet<ActorNumber> table;
	protected ActorNumber insertedActor;
	public SlotVariableValueState(HashSet<ActorNumber> actorTable, ActorNumber actor) {
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
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return "SlotVariableValueState:" + insertedActor;
	}
}
