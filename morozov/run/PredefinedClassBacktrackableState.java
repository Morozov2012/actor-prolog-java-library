// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.errors.*;

import java.util.ArrayList;

public class PredefinedClassBacktrackableState extends Term {
	//
	protected ArrayList<PredefinedClassRecord> table;
	protected int currentSize;
	//
	private static final long serialVersionUID= 0x7E032CB9D9844490L; // 9080150450438947984L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","PredefinedClassBacktrackableState");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PredefinedClassBacktrackableState(ArrayList<PredefinedClassRecord> stack) {
		table= stack;
		currentSize= stack.size();
	}
	@Override
	public void clear() {
		for(int i=table.size(); i > currentSize; i--) {
			table.remove(i-1);
		}
	}
	// General "Unify With" function:
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	@Override
	public String toString() {
		return "PredefinedClassBacktrackableState=" + currentSize;
	}
}
