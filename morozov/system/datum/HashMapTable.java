// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.domains.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.HashMap;

public class HashMapTable extends DatabaseTable {
	//
	protected HashMap<Term,DatabaseRecord> databaseHash= new HashMap<Term,DatabaseRecord>();
	//
	protected static Term termHashMap= new PrologSymbol(SymbolCodes.symbolCode_E_HashMap);
	//
	private static final long serialVersionUID= 0x3D890F2BB70D64A3L; // 4434091988552082595L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.datum","HashMapTable");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public HashMapTable(PrologDomain domain, boolean reuseKN) {
		super(domain,reuseKN);
	}
	public HashMapTable(PrologDomain domain, boolean reuseKN, HashMap<String,PrologDomain> currentLocalDomainTable) {
		super(domain,reuseKN,currentLocalDomainTable);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getType() {
		return termHashMap;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseRecord insertRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term key= DatabaseUtils.extractMapKey(copy,iX);
		DatabaseRecord oldRecord= databaseHash.get(key);
		if (oldRecord==null) {
			try {
				DatabaseRecord newRecord= super.insertRecord(copy,currentProcess,false,iX,isInnerOperation);
				databaseHash.put(key,newRecord);
				return newRecord;
			} catch (RuntimeException e) {
				databaseHash.remove(key);
				throw e;
			}
		} else {
			oldRecord.content= copy;
			return oldRecord;
		}
	}
	//
	public DatabaseRecord appendRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term key= DatabaseUtils.extractMapKey(copy,iX);
		DatabaseRecord oldRecord= databaseHash.get(key);
		if (oldRecord==null) {
			try {
				DatabaseRecord newRecord= super.appendRecord(copy,currentProcess,false,iX,isInnerOperation);
				databaseHash.put(key,newRecord);
				return newRecord;
			} catch (RuntimeException e) {
				databaseHash.remove(key);
				throw e;
			}
		} else {
			oldRecord.content= copy;
			return oldRecord;
		}
	}
	//
	public void put(Term argumentKey, Term argumentValue, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term key= argumentKey.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		Term value= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord oldRecord= databaseHash.get(key);
		Term map= new PrologStructure(SymbolCodes.symbolCode_E_map,new Term[]{key,value});
		if (oldRecord==null) {
			try {
				DatabaseRecord record= appendRecord(map,currentProcess,false,iX,false);
				databaseHash.put(key,record);
			} catch (RuntimeException e) {
				databaseHash.remove(key);
				throw e;
			}
		} else {
			oldRecord.content= map;
		}
	}
	//
	public void get(PrologVariable argumentResult, Term argumentValue, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		Term copy2= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord result= databaseHash.get(copy2);
		if (result != null) {
			argumentResult.setNonBacktrackableValue(DatabaseUtils.extractSecondArgument(result.content,null));
		} else {
			throw Backtracking.instance;
		}
	}
	public void get(Term argumentValue, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		containsKey(argumentValue,currentProcess,false,iX);
	}
	//
	public void containsKey(Term argumentValue, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		Term copy1= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		if (!databaseHash.containsKey(copy1)) {
			throw Backtracking.instance;
		}
	}
	//
	public void retractKey(Term argumentValue, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term copy1= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord record= databaseHash.get(copy1);
		if (record != null) {
			retractCurrentRecord(record,currentProcess,false,iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void retractAll(ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		databaseHash.clear();
		super.retractAll(currentProcess,false,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void retractCurrentRecord(DatabaseRecord currentRecord, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term key= DatabaseUtils.extractMapKey(currentRecord.content,iX);
		databaseHash.remove(key);
		super.retractCurrentRecord(currentRecord,currentProcess,false,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Object clone() {
		HashMapTable o= (HashMapTable)super.clone();
		o.databaseHash= new HashMap<Term,DatabaseRecord>();
		return o;
	}
}
