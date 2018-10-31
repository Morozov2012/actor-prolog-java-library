// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.datum.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class PrologLinkedHashMap extends PrologHashMap {
	//
	public PrologLinkedHashMap() {
	}
	public PrologLinkedHashMap(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	// abstract public Term getBuiltInSlot_E_reuse_key_numbers();
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseType getDatabaseType() {
		return DatabaseType.LINKED_HASH_MAP;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void insert2s(ChoisePoint iX, Term a1, PrologVariable a2) {
		((LinkedHashMapTable)getDatabaseTable(iX)).copyAndInsertTerm(a1,a2,currentProcess,true,iX);
	}
	//
	public void append2s(ChoisePoint iX, Term a1, PrologVariable a2) {
		((LinkedHashMapTable)getDatabaseTable(iX)).copyAndAppendTerm(a1,a2,currentProcess,true,iX);
	}
	//
	public void insertBefore3s(ChoisePoint iX, Term a1, Term a2, PrologVariable a3) {
		((LinkedHashMapTable)getDatabaseTable(iX)).insertBefore(a1,a2,a3,currentProcess,true,iX);
	}
	//
	public void insertAfter3s(ChoisePoint iX, Term a1, Term a2, PrologVariable a3) {
		((LinkedHashMapTable)getDatabaseTable(iX)).insertAfter(a1,a2,a3,currentProcess,true,iX);
	}
	//
	public void getFirstKey0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getFirstKey(result,currentProcess,true,iX);
	}
	public void getFirstKey0fs(ChoisePoint iX) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getFirstKey(currentProcess,true,iX);
	}
	//
	public void getLastKey0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getLastKey(result,currentProcess,true,iX);
	}
	public void getLastKey0fs(ChoisePoint iX) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getLastKey(currentProcess,true,iX);
	}
	//
	public void getNextKey1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getNextKey(result,a1,currentProcess,true,iX);
	}
	public void getNextKey1fs(ChoisePoint iX, Term a1) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getNextKey(a1,currentProcess,true,iX);
	}
	//
	public void getPreviousKey1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getPreviousKey(result,a1,currentProcess,true,iX);
	}
	public void getPreviousKey1fs(ChoisePoint iX, Term a1) throws Backtracking {
		((LinkedHashMapTable)getDatabaseTable(iX)).getPreviousKey(a1,currentProcess,true,iX);
	}
}
