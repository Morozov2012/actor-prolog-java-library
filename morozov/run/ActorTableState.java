// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.errors.*;

import java.util.HashSet;

public class ActorTableState extends Term {
	//
	protected HashSet<ActorNumber> table;
	protected ActorNumber insertedActor;
	//
	private static final long serialVersionUID= 0xDE5B381CE039F7D0L; // -2424282277736745008L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","ActorTableState");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorTableState(HashSet<ActorNumber> actorTable, ActorNumber actor) {
		table= actorTable;
		insertedActor= actor;
	}
	//
	@Override
	public void clear() {
		table.remove(insertedActor);
	}
	// General "Unify With" function:
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	@Override
	public String toString() {
		return "ActorTableState:" + insertedActor;
	}
}
