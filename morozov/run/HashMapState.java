// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.classes.errors.*;
import morozov.system.indices.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class HashMapState extends Term {
	//
	protected HashMap<ArrayIndices,SlotVariable> volume;
	protected ArrayIndices indices;
	protected SlotVariable slotVariable;
	//
	public HashMapState(HashMap<ArrayIndices,SlotVariable> v, ArrayIndices list, SlotVariable slot) {
		volume= v;
		indices= list;
		slotVariable= slot;
	}
	public void clear() {
		// System.out.printf("HashMapState:: remove(%s)\n\n",indices);
		volume.remove(indices);
	}
	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {
		slotVariables.add(slotVariable);
	}
	// General "Unify With" function
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return "HashMapState:" + indices.toString();
	}
}
