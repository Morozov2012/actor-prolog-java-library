// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.errors.*;

import java.util.ArrayList;

public class AsyncCallTableState extends Term {
	//
	private ArrayList<AsyncCall> table;
	private int currentSize;
	//
	private static final long serialVersionUID= 0x3B7DE7ECF790C279L; // 4286837425278337657L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","AsyncCallTableState");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public AsyncCallTableState(ArrayList<AsyncCall> callTable) {
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
		return "AsyncCallTableState=" + currentSize;
	}
}
