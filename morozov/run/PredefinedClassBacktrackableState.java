// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.errors.*;

import java.util.ArrayList;

public class PredefinedClassBacktrackableState extends Term {
	private ArrayList<PredefinedClassRecord> table;
	private int currentSize;
	public PredefinedClassBacktrackableState(ArrayList<PredefinedClassRecord> stack) {
		table= stack;
		currentSize= stack.size();
	}
	public void clear() {
		for(int i=table.size(); i > currentSize; i--) {
			table.remove(i-1);
		}
	}
	// General "Unify With" function
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	public String toString() {
		return "PredefinedClassBacktrackableState=" + currentSize;
	}
}
