// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.domains.*;
import morozov.run.*;
import morozov.system.datum.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.math.BigInteger;

public class LinkedHashMapTable extends HashMapTable {
	//
	protected HashSet<BigInteger> freeKeyNumbers= new HashSet<BigInteger>();
	protected BigInteger recentKeyNumber= BigInteger.ZERO;
	//
	protected static Term linkedHashMapTerm= new PrologSymbol(SymbolCodes.symbolCode_E_LinkedHashMap);
	//
	public LinkedHashMapTable(PrologDomain domain, boolean reuseKN) {
		super(domain,reuseKN);
	}
	public LinkedHashMapTable(PrologDomain domain, boolean reuseKN, HashMap<String,PrologDomain> currentLocalDomainTable) {
		super(domain,reuseKN,currentLocalDomainTable);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getType() {
		return linkedHashMapTerm;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retractAll(ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		freeKeyNumbers.clear();
		recentKeyNumber= BigInteger.ZERO;
		super.retractAll(currentProcess,false,iX);
	}
	//
	public void copyAndInsertTerm(Term argumentValue, PrologVariable argumentKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term valueTerm= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		BigInteger keyNumber= getNewKeyNumber(iX);
		Term keyTerm= new PrologInteger(keyNumber);
		Term mapTerm= new PrologStructure(SymbolCodes.symbolCode_E_map,new Term[]{keyTerm,valueTerm});
		insertRecord(mapTerm,currentProcess,false,iX,true);
		argumentKey.setBacktrackableValue(keyTerm,iX);
	}
	//
	public DatabaseRecord insertRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		if (isInnerOperation) {
			return super.insertRecord(copy,currentProcess,false,iX,isInnerOperation);
		} else {
			Term keyTerm= DatabaseUtils.extractMapKey(copy,iX);
			try {
				BigInteger keyNumber= keyTerm.getIntegerValue(iX);
				boolean f= freeKeyNumbers.remove(keyNumber);
				return super.insertRecord(copy,currentProcess,false,iX,isInnerOperation);
			} catch (TermIsNotAnInteger e) {
				throw new DatabaseKeyTermIsNotAnInteger();
			}
		}
	}
	//
	public void copyAndAppendTerm(Term argumentValue, PrologVariable argumentKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term valueTerm= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		BigInteger keyNumber= getNewKeyNumber(iX);
		Term keyTerm= new PrologInteger(keyNumber);
		Term mapTerm= new PrologStructure(SymbolCodes.symbolCode_E_map,new Term[]{keyTerm,valueTerm});
		appendRecord(mapTerm,currentProcess,false,iX,true);
		argumentKey.setBacktrackableValue(keyTerm,iX);
	}
	//
	public DatabaseRecord appendRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term keyTerm= DatabaseUtils.extractMapKey(copy,iX);
		try {
			BigInteger keyNumber= keyTerm.getIntegerValue(iX);
			freeKeyNumbers.remove(keyNumber);
			return super.appendRecord(copy,currentProcess,false,iX,isInnerOperation);
		} catch (TermIsNotAnInteger e) {
			throw new DatabaseKeyTermIsNotAnInteger();
		}
	}
	//
	public void insertBefore(Term argumentValue, Term argumentCurrentKey, PrologVariable argumentNewKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term currentKeyTerm= argumentCurrentKey.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord currentRecord= databaseHash.get(currentKeyTerm);
		if (currentRecord != null) {
			Term valueTerm= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
			BigInteger keyNumber= getNewKeyNumber(iX);
			Term keyTerm= new PrologInteger(keyNumber);
			Term mapTerm= new PrologStructure(SymbolCodes.symbolCode_E_map,new Term[]{keyTerm,valueTerm});
			DatabaseRecord previousRecord= currentRecord.previousRecord;
			if (previousRecord==null) {
				insertRecord(mapTerm,currentProcess,false,iX,true);
			} else {
				DatabaseRecord newRecord= new DatabaseRecord(mapTerm);
				newRecord.previousRecord= previousRecord;
				newRecord.nextRecord= currentRecord;
				previousRecord.nextRecord= newRecord;
				currentRecord.previousRecord= newRecord;
				databaseHash.put(keyTerm,newRecord);
			};
			argumentNewKey.setBacktrackableValue(keyTerm,iX);
		} else {
			throw new GivenKeyDoesNotExistInTheDatabase();
		}
	}
	//
	public void insertAfter(Term argumentValue, Term argumentCurrentKey, PrologVariable argumentNewKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		Term currentKeyTerm= argumentCurrentKey.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord currentRecord= databaseHash.get(currentKeyTerm);
		if (currentRecord != null) {
			Term valueTerm= argumentValue.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
			BigInteger keyNumber= getNewKeyNumber(iX);
			Term keyTerm= new PrologInteger(keyNumber);
			Term mapTerm= new PrologStructure(SymbolCodes.symbolCode_E_map,new Term[]{keyTerm,valueTerm});
			DatabaseRecord nextRecord= currentRecord.nextRecord;
			if (nextRecord==null) {
				appendRecord(mapTerm,currentProcess,false,iX,true);
			} else {
				DatabaseRecord newRecord= new DatabaseRecord(mapTerm);
				newRecord.previousRecord= currentRecord;
				newRecord.nextRecord= nextRecord;
				currentRecord.nextRecord= newRecord;
				nextRecord.previousRecord= newRecord;
				databaseHash.put(keyTerm,newRecord);
			};
			argumentNewKey.setBacktrackableValue(keyTerm,iX);
		} else {
			throw new GivenKeyDoesNotExistInTheDatabase();
		}
	}
	//
	public void getFirstKey(PrologVariable argumentResult, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		if (tableContent != null) {
			Term keyTerm= DatabaseUtils.extractMapKey(tableContent.content,iX);
			argumentResult.setNonBacktrackableValue(keyTerm);
		} else {
			throw Backtracking.instance;
		}
	}
	public void getFirstKey(ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		if (tableContent==null) {
			throw Backtracking.instance;
		}
	}
	//
	public void getLastKey(PrologVariable argumentResult, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		if (ultimateRecord != null) {
			Term keyTerm= DatabaseUtils.extractMapKey(ultimateRecord.content,iX);
			argumentResult.setNonBacktrackableValue(keyTerm);
		} else {
			throw Backtracking.instance;
		}
	}
	public void getLastKey(ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		if (ultimateRecord==null) {
			throw Backtracking.instance;
		}
	}
	//
	public void getNextKey(PrologVariable argumentResult, Term argumentCurrentKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		Term currentKeyTerm= argumentCurrentKey.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord currentRecord= databaseHash.get(currentKeyTerm);
		if (currentRecord != null) {
			DatabaseRecord nextRecord= currentRecord.nextRecord;
			if (nextRecord==null) {
				throw Backtracking.instance;
			} else {
				Term keyTerm= DatabaseUtils.extractMapKey(nextRecord.content,iX);
				argumentResult.setNonBacktrackableValue(keyTerm);
			}
		} else {
			throw new GivenKeyDoesNotExistInTheDatabase();
		}
	}
	public void getNextKey(Term argumentCurrentKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		Term currentKeyTerm= argumentCurrentKey.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord currentRecord= databaseHash.get(currentKeyTerm);
		if (currentRecord != null) {
			DatabaseRecord nextRecord= currentRecord.nextRecord;
			if (nextRecord==null) {
				throw Backtracking.instance;
			}
		} else {
			throw new GivenKeyDoesNotExistInTheDatabase();
		}
	}
	//
	public void getPreviousKey(PrologVariable argumentResult, Term argumentCurrentKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		Term currentKeyTerm= argumentCurrentKey.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord currentRecord= databaseHash.get(currentKeyTerm);
		if (currentRecord != null) {
			DatabaseRecord previousRecord= currentRecord.previousRecord;
			if (previousRecord==null) {
				throw Backtracking.instance;
			} else {
				Term keyTerm= DatabaseUtils.extractMapKey(previousRecord.content,iX);
				argumentResult.setNonBacktrackableValue(keyTerm);
			}
		} else {
			throw new GivenKeyDoesNotExistInTheDatabase();
		}
	}
	public void getPreviousKey(Term argumentCurrentKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		Term currentKeyTerm= argumentCurrentKey.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord currentRecord= databaseHash.get(currentKeyTerm);
		if (currentRecord != null) {
			DatabaseRecord previousRecord= currentRecord.previousRecord;
			if (previousRecord==null) {
				throw Backtracking.instance;
			}
		} else {
			throw new GivenKeyDoesNotExistInTheDatabase();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void retractCurrentRecord(DatabaseRecord currentRecord, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		if (reuseKeyNumbers) {
			Term keyTerm= DatabaseUtils.extractMapKey(currentRecord.content,iX);
			try {
				BigInteger keyNumber= keyTerm.getIntegerValue(iX);
				freeKeyNumbers.add(keyNumber);
				super.retractCurrentRecord(currentRecord,currentProcess,false,iX);
			} catch (TermIsNotAnInteger e) {
				throw new DatabaseKeyTermIsNotAnInteger();
			}
		} else {
			super.retractCurrentRecord(currentRecord,currentProcess,false,iX);
		}
	}
	//
	protected BigInteger getNewKeyNumber(ChoisePoint iX) {
		BigInteger keyNumber;
		if (freeKeyNumbers.isEmpty()) {
			keyNumber= computeNewKeyNumber();
		} else {
			if (reuseKeyNumbers) {
				Iterator<BigInteger> iterator= freeKeyNumbers.iterator();
				keyNumber= iterator.next();
				iterator.remove();
			} else {
				keyNumber= computeNewKeyNumber();
				freeKeyNumbers.remove(keyNumber);
			}
		};
		return keyNumber;
	}
	protected BigInteger computeNewKeyNumber() {
		BigInteger keyNumber;
		while (true) {
			recentKeyNumber= recentKeyNumber.add(BigInteger.ONE);
			if (!databaseHash.containsKey(new PrologInteger(recentKeyNumber))) {
				keyNumber= recentKeyNumber;
				break;
			}
		}
		return keyNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Object clone() {
		LinkedHashMapTable o= (LinkedHashMapTable)super.clone();
		o.freeKeyNumbers= new HashSet<BigInteger>();
		o.recentKeyNumber= BigInteger.ZERO;
		return o;
	}
}
