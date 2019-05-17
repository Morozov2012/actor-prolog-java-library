// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.worlds.errors.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class SlotVariableValueState extends Term {
	//
	protected HashSet<ActorNumber> table;
	protected ActorNumber insertedActor;
	//
	private static final long serialVersionUID= 0x55FE3E67D76B0E8BL; // 6196458753025052299L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","SlotVariableValueState");
	// }
	//
	///////////////////////////////////////////////////////////////
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
