// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.system.indices.*;
import morozov.terms.*;
import morozov.worlds.errors.*;

import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.HashSet;

public class HashMapState extends Term {
	//
	protected Map<ArrayIndices,SlotVariable> volume;
	protected ArrayIndices indices;
	protected SlotVariable slotVariable;
	//
	private static final long serialVersionUID= 0xFE1C2DE8C6A1D5A3L; // -136183410939800157L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","HashMapState");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public HashMapState(Map<ArrayIndices,SlotVariable> v, ArrayIndices list, SlotVariable slot) {
		volume= v;
		indices= list;
		slotVariable= slot;
	}
	//
	@Override
	public void clear() {
		volume.remove(indices);
	}
	@Override
	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {
		slotVariables.add(slotVariable);
	}
	// General "Unify With" function:
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	// Converting Term to String:
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return "HashMapState:" + indices.toString();
	}
}
