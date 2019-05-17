// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.errors.*;

import java.util.ArrayList;

public class SuspendedCallTableState extends Term {
	//
	private ArrayList<SuspendedCall> table;
	private int currentSize;
	//
	private static final long serialVersionUID= 0xA10A10748C93A20BL; // -6842638591075311093L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","SuspendedCallTableState");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SuspendedCallTableState(ArrayList<SuspendedCall> callTable) {
		table= callTable;
		currentSize= callTable.size();
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
		return "SuspendedCallTableState=" + currentSize;
	}
}
