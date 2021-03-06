// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.domains.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.util.HashMap;
import java.util.HashSet;

public class HashSetTable extends DatabaseTable {
	//
	protected HashSet<Term> databaseHash= new HashSet<>();
	//
	protected static Term termHashSet= new PrologSymbol(SymbolCodes.symbolCode_E_HashSet);
	//
	private static final long serialVersionUID= 0x1951AEB51AB9DBE9L; // 1824431416922463209L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.datum","HashSetTable");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public HashSetTable(PrologDomain domain, boolean reuseKN) {
		super(domain,reuseKN);
	}
	public HashSetTable(PrologDomain domain, boolean reuseKN, HashMap<String,PrologDomain> currentLocalDomainTable) {
		super(domain,reuseKN,currentLocalDomainTable);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term getType() {
		return termHashSet;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public DatabaseRecord insertRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		if (!databaseHash.contains(copy)) {
			databaseHash.add(copy);
			try {
				DatabaseRecord newRecord= super.insertRecord(copy,currentProcess,false,iX,isInnerOperation);
				return newRecord;
			} catch (RuntimeException e) {
				databaseHash.remove(copy);
				throw e;
			}
		} else {
			return null;
		}
	}
	//
	@Override
	public DatabaseRecord appendRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		if (!databaseHash.contains(copy)) {
			databaseHash.add(copy);
			try {
				DatabaseRecord newRecord= super.appendRecord(copy,currentProcess,false,iX,isInnerOperation);
				return newRecord;
			} catch (RuntimeException e) {
				databaseHash.remove(copy);
				throw e;
			}
		} else {
			return null;
		}
	}
	//
	@Override
	public void findRecord(PrologVariable outputResult, Term inputResult, boolean hasOutputArgument, Continuation c0, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		if (!hasOutputArgument) {
			try {
				Term copy= inputResult.copyGroundValue(iX);
				if (databaseHash.contains(copy)) {
					c0.execute(iX);
				} else {
					throw Backtracking.instance;
				}
			} catch (TermIsUnboundVariable e) {
				super.findRecord(outputResult,inputResult,hasOutputArgument,c0,currentProcess,false,iX);
			}
		} else {
			super.findRecord(outputResult,inputResult,hasOutputArgument,c0,currentProcess,false,iX);
		}
	}
	//
	@Override
	public void retractAll(ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		databaseHash.clear();
		super.retractAll(currentProcess,false,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void retractCurrentRecord(DatabaseRecord currentRecord, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		databaseHash.remove(currentRecord.content);
		super.retractCurrentRecord(currentRecord,currentProcess,false,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Object clone() {
		HashSetTable o= (HashSetTable)super.clone();
		o.databaseHash= new HashSet<>();
		return o;
	}
}
