// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.worlds.errors.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class SlotVariableValueState extends Term {
	protected HashSet<ActorNumber> table;
	protected ActorNumber insertedActor;
	//
	public SlotVariableValueState(HashSet<ActorNumber> actorTable, ActorNumber actor) {
		table= actorTable;
		insertedActor= actor;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear() {
		table.remove(insertedActor);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return "SlotVariableValueState:" + insertedActor;
	}
}
