// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.system.records.*;
import morozov.terms.*;

import java.util.HashMap;

public abstract class PrologHashMap extends Database {
	// protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	// protected DatabaseRecord content;
	// protected DatabaseRecord ultimateRecord;
	//
	// protected String recentErrorText;
	// protected long recentErrorPosition= -1;
	// protected Throwable recentErrorException;
	//
	// abstract protected Term getBuiltInSlot_E_name();
	// abstract protected Term getBuiltInSlot_E_extension();
	// abstract protected Term getBuiltInSlot_E_character_set();
	// abstract protected PrologDomain getBuiltInSlotDomain_E_target_data();
	//
	protected HashMap<Term,DatabaseRecord> databaseHash= new HashMap<Term,DatabaseRecord>();
	//
	public void insert1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		Term key= DatabaseUtils.extractMapKey(copy,iX);
		if (!databaseHash.containsKey(key)) {
			insertRecord(copy);
		}
	}
	public void append1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		Term key= DatabaseUtils.extractMapKey(copy,iX);
		if (!databaseHash.containsKey(key)) {
			appendRecord(copy);
		}
	}
	//
	public void put2s(ChoisePoint iX, Term a1, Term a2) {
		Term copy1= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		if (!databaseHash.containsKey(copy1)) {
			Term copy2= a2.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
			appendRecord(new PrologStructure(SymbolCodes.symbolCode_E_map,new Term[]{copy1,copy2}));
		}
	}
	//
	protected void updateDatabaseHash(Term copy, DatabaseRecord record) {
		Term key= DatabaseUtils.extractMapKey(copy,null);
		Term value= DatabaseUtils.extractSecondArgument(copy,null);
		// System.out.printf("PrologHashMap: key=%s, value=%s\n",key,value);
		databaseHash.put(key,record);
	}
	//
	public void get1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		Term copy2= a2.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord result= databaseHash.get(copy2);
		if (result != null) {
			a1.value= DatabaseUtils.extractSecondArgument(result.value,null);
		} else {
			throw new Backtracking();
		}
	}
	public void get1fs(ChoisePoint iX, Term a1) throws Backtracking {
		containsKey1s(iX,a1);
	}
	//
	public void containsKey1s(ChoisePoint iX, Term a1) throws Backtracking {
		Term copy1= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		if (!databaseHash.containsKey(copy1)) {
			throw new Backtracking();
		}
	}
	//
	public void retractKey1s(ChoisePoint iX, Term a1) throws Backtracking {
		Term copy1= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		DatabaseRecord record= databaseHash.get(copy1);
		if (record != null) {
			retractCurrentRecord(record);
		} else {
			throw new Backtracking();
		}
	}
	//
	public void retractAll0s(ChoisePoint iX) {
		databaseHash.clear();
		super.retractAll0s(iX);
	}
	//
	protected void retractCurrentRecord(DatabaseRecord currentRecord) {
		Term key= DatabaseUtils.extractMapKey(currentRecord.value,null);
		databaseHash.remove(key);
		super.retractCurrentRecord(currentRecord);
	}
	//
	public void insertNewItem(Term newItem, DatabaseRecord newRecord) {
		Term key= DatabaseUtils.extractMapKey(newItem,null);
		if (!databaseHash.containsKey(key)) {
			Term value= DatabaseUtils.extractSecondArgument(newItem,null);
			databaseHash.put(key,newRecord);
			try {
				super.insertNewItem(newItem,newRecord);
			} catch (RuntimeException e) {
				databaseHash.remove(key);
				throw e;
			}
		}
	}
}
