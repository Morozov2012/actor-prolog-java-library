// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.terms.*;

import java.util.*;

public class AsyncCallTableState extends Term {
	private ArrayList<AsyncCall> table;
	private int currentSize;
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
