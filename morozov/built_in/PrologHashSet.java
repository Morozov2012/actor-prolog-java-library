// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.records.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.HashSet;

public abstract class PrologHashSet extends Database {
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
	protected HashSet<Term> databaseHash= new HashSet<Term>();
	//
	public void insert1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		if (!databaseHash.contains(copy)) {
			insertRecord(copy);
		}
	}
	public void append1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		if (!databaseHash.contains(copy)) {
			appendRecord(copy);
		}
	}
	//
	protected void updateDatabaseHash(Term copy, DatabaseRecord record) {
		databaseHash.add(copy);
	}
	//
	// public class Find1s extends Continuation {
	public class Find1s extends Database.Find1s {
		public Find1s(Continuation aC, PrologVariable a1) {
			super(aC,a1);
		}
		public Find1s(Continuation aC, Term a1) {
			super(aC,a1);
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			if (!hasOutputArgument) {
				try {
					Term copy= inputResult.copyGroundValue(iX);
					if (databaseHash.contains(copy)) {
						c0.execute(iX);
					} else {
						// super.execute(iX);
						throw Backtracking.instance;
					}
				} catch (TermIsUnboundVariable e) {
					super.execute(iX);
				}
			} else {
				super.execute(iX);
			}
		}
	}
	//
	public void retractAll0s(ChoisePoint iX) {
		databaseHash.clear();
		super.retractAll0s(iX);
	}
	//
	protected void retractCurrentRecord(DatabaseRecord currentRecord) {
		databaseHash.remove(currentRecord.value);
		super.retractCurrentRecord(currentRecord);
	}
	//
	public void insertNewItem(Term newItem, DatabaseRecord newRecord) {
		if (!databaseHash.contains(newItem)) {
			databaseHash.add(newItem);
			try {
				super.insertNewItem(newItem,newRecord);
			} catch (RuntimeException e) {
				databaseHash.remove(newItem);
				throw e;
			}
		}
	}
}
