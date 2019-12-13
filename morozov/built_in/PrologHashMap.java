// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.datum.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class PrologHashMap extends Database {
	//
	public PrologHashMap() {
	}
	public PrologHashMap(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	@Override
	public DatabaseType getDatabaseType() {
		return DatabaseType.HASH_MAP;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void put2s(ChoisePoint iX, Term a1, Term a2) {
		((HashMapTable)getDatabaseTable(iX)).put(a1,a2,currentProcess,true,iX);
	}
	//
	public void get1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		((HashMapTable)getDatabaseTable(iX)).get(result,a1,currentProcess,true,iX);
	}
	public void get1fs(ChoisePoint iX, Term a1) throws Backtracking {
		((HashMapTable)getDatabaseTable(iX)).containsKey(a1,currentProcess,true,iX);
	}
	//
	public void containsKey1s(ChoisePoint iX, Term a1) throws Backtracking {
		((HashMapTable)getDatabaseTable(iX)).containsKey(a1,currentProcess,true,iX);
	}
	//
	public void retractKey1s(ChoisePoint iX, Term a1) throws Backtracking {
		((HashMapTable)getDatabaseTable(iX)).retractKey(a1,currentProcess,true,iX);
	}
}
