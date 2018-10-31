// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.domains.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.system.datum.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigInteger;

public abstract class Database extends DataAbstraction {
	//
	public DatabasePlace place= null;
	public Boolean reuseKeyNumbers= null;
	//
	protected DatabaseTableContainer databaseTableContainer= null;
	//
	///////////////////////////////////////////////////////////////
	//
	public Database() {
	}
	public Database(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// abstract public long entry_s_Update_0();
	//
	// abstract public Term getBuiltInSlot_E_name();
	// abstract public Term getBuiltInSlot_E_extension();
	// abstract public Term getBuiltInSlot_E_maximal_waiting_time();
	// abstract public Term getBuiltInSlot_E_character_set();
	// abstract public Term getBuiltInSlot_E_backslash_always_is_separator();
	// protected Term getBuiltInSlot_E_transaction_waiting_period()
	// abstract public Term getBuiltInSlot_E_transaction_sleep_period();
	// abstract public Term getBuiltInSlot_E_transaction_maximal_retry_number();
	abstract public Term getBuiltInSlot_E_place();
	abstract protected PrologDomain getBuiltInSlotDomain_E_target_data();
	//
	protected Term getBuiltInSlot_E_reuse_key_numbers() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_yes);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set place
	//
	public void setPlace1s(ChoisePoint iX, Term a1) {
		PrologDomain domain= getBuiltInSlotDomain_E_target_data();
		DatabaseType type= getDatabaseType();
		boolean reuseKN= getReuseKeyNumbers(iX);
		setPlace(DatabasePlace.argumentToDatabasePlace(a1,this,domain,type,reuseKN,iX));
	}
	public void setPlace(DatabasePlace value) {
		place= value;
	}
	public void getPlace0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getPlace(iX).toTerm());
	}
	public void getPlace0fs(ChoisePoint iX) {
	}
	public DatabasePlace getPlace(ChoisePoint iX) {
		if (place != null) {
			return place;
		} else {
			Term value= getBuiltInSlot_E_place();
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			DatabaseType type= getDatabaseType();
			boolean reuseKN= getReuseKeyNumbers(iX);
			return DatabasePlace.argumentToDatabasePlace(value,this,domain,type,reuseKN,iX);
		}
	}
	//
	// get/set reuse_key_numbers
	//
	public void setReuseKeyNumbers1s(ChoisePoint iX, Term a1) {
		boolean reuseKN= YesNo.termYesNo2Boolean(a1,iX);
		setReuseKeyNumbers(reuseKN);
		getDatabaseTable(iX).setReuseKeyNumbers(reuseKN,currentProcess,true);
	}
	public void setReuseKeyNumbers(boolean value) {
		reuseKeyNumbers= value;
	}
	public void getReuseKeyNumbers0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getReuseKeyNumbers(iX)));
	}
	public void getReuseKeyNumbers0fs(ChoisePoint iX) {
	}
	public boolean getReuseKeyNumbers(ChoisePoint iX) {
		if (reuseKeyNumbers != null) {
			return reuseKeyNumbers;
		} else {
			Term value= getBuiltInSlot_E_reuse_key_numbers();
			return YesNo.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setWatchUpdates1s(ChoisePoint iX, Term a1) {
		super.setWatchUpdates1s(iX,a1);
		if (!getWatchUpdates(iX)) {
			unregisterWatcher(currentProcess,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseType getDatabaseType() {
		return DatabaseType.PLAIN;
	}
	//
	public DatabaseTable retrieveStandaloneDatabaseTable(ChoisePoint iX) {
		if (databaseTableContainer == null) {
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			boolean reuseKN= getReuseKeyNumbers(iX);
			databaseTableContainer= new DatabaseTableContainer(getDatabaseType().createTable(domain,reuseKN),false);
		};
		return databaseTableContainer.getTable();
	}
	//
	public DatabaseTableContainer retrieveStandaloneDatabaseTableContainer(ChoisePoint iX) {
		if (databaseTableContainer == null) {
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			boolean reuseKN= getReuseKeyNumbers(iX);
			databaseTableContainer= new DatabaseTableContainer(getDatabaseType().createTable(domain,reuseKN),false);
		};
		return databaseTableContainer;
	}
	//
	public DatabaseTable getDatabaseTable(ChoisePoint iX) {
		if (place != null) {
			return place.databaseTableContainer.getTable();
		} else {
			Term value= getBuiltInSlot_E_place();
			return DatabaseTableContainer.argumentToDatabaseTable(value,this,iX);
		}
	}
	//
	public DatabaseTableContainer getDatabaseTableContainer(ChoisePoint iX) {
		if (place != null) {
			return place.databaseTableContainer;
		} else {
			Term value= getBuiltInSlot_E_place();
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			DatabaseType type= getDatabaseType();
			boolean reuseKN= getReuseKeyNumbers(iX);
			return DatabaseTableContainer.argumentToDatabaseTableContainer(value,this,domain,type,reuseKN,iX);
		}
	}
	//
	public void beginDatabaseTableTransaction(DatabaseAccessMode accessMode, TimeInterval waitingPeriod, TimeInterval sleepPeriod, BigInteger maximalRetryNumber, ActiveWorld currentProcess, ChoisePoint iX) throws Backtracking {
		if (place != null) {
			place.beginTransaction(accessMode,waitingPeriod,sleepPeriod,maximalRetryNumber,this,currentProcess,iX);
		} else {
			Term value= getBuiltInSlot_E_place();
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			boolean reuseKN= getReuseKeyNumbers(iX);
			DatabaseTableContainer.convertTermToDatabaseTableAndBeginTransaction(value,domain,getDatabaseType(),reuseKN,this,accessMode,waitingPeriod,sleepPeriod,maximalRetryNumber,currentProcess,iX);
		}
	}
	//
	public void endDatabaseTableTransaction(ActiveWorld currentProcess, boolean watchTable, ChoisePoint iX) {
		if (place != null) {
			place.endTransaction(this,currentProcess,watchTable);
		} else {
			Term value= getBuiltInSlot_E_place();
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			DatabaseTableContainer.convertTermToDatabaseTableAndEndTransaction(value,domain,getDatabaseType(),this,currentProcess,watchTable,iX);
		}
	}
	//
	public void activateWatcher(ActiveWorld currentProcess, ChoisePoint iX) {
		setWatchUpdates(true);
		if (place != null) {
			place.activateWatcher(this,currentProcess);
		} else {
			Term value= getBuiltInSlot_E_place();
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			boolean reuseKN= getReuseKeyNumbers(iX);
			DatabaseTableContainer.convertTermToDatabaseTableAndActivateWatcher(value,domain,getDatabaseType(),reuseKN,this,currentProcess,iX);
		}
	}
	//
	public void unregisterWatcher(ActiveWorld currentProcess, ChoisePoint iX) {
		if (place != null) {
			place.unregisterWatcher(this,currentProcess);
		} else {
			Term value= getBuiltInSlot_E_place();
			// PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			// boolean reuseKN= getReuseKeyNumbers(iX);
			DatabaseTableContainer.convertTermToDatabaseTableAndUnregisterWatcher(value,this,currentProcess,iX);
		}
	}
	//
	public void rollbackDatabaseTableCurrentTransaction(ActiveWorld currentProcess, ChoisePoint iX) {
		boolean watchTable= getWatchUpdates(iX);
		if (place != null) {
			place.rollbackCurrentTransaction(this,currentProcess,watchTable);
		} else {
			Term value= getBuiltInSlot_E_place();
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			DatabaseTableContainer.convertTermToDatabaseTableAndRollbackCurrentTransaction(value,domain,getDatabaseType(),this,currentProcess,watchTable,iX);
		}
	}
	//
	public void rollbackDatabaseTableTransactionTree(ActiveWorld currentProcess, ChoisePoint iX) {
		boolean watchTable= getWatchUpdates(iX);
		if (place != null) {
			place.rollbackTransactionTree(this,currentProcess,watchTable);
		} else {
			Term value= getBuiltInSlot_E_place();
			PrologDomain domain= getBuiltInSlotDomain_E_target_data();
			DatabaseTableContainer.convertTermToDatabaseTableAndRollbackTransactionTree(value,domain,getDatabaseType(),this,currentProcess,watchTable,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void insert1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		getDatabaseTable(iX).insertRecord(copy,currentProcess,true,iX,false);
	}
	//
	public void append1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		getDatabaseTable(iX).appendRecord(copy,currentProcess,true,iX,false);
	}
	public class Find1s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult;
		protected Term inputResult;
		protected boolean hasOutputArgument;
		//
		public Find1s(Continuation aC, PrologVariable a1) {
			c0= aC;
			outputResult= a1;
			hasOutputArgument= true;
		}
		public Find1s(Continuation aC, Term a1) {
			c0= aC;
			inputResult= a1;
			hasOutputArgument= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			getDatabaseTable(iX).findRecord(outputResult,inputResult,hasOutputArgument,c0,currentProcess,true,iX);
		}
	}
	//
	public class Match1ff extends Match {
		public Match1ff(Continuation aC, PrologVariable result, Term a1) {
			super(aC,a1);
			argumentResult= result;
			isFunctionCall= true;
		}
	}
	public class Match1fs extends Match {
		public Match1fs(Continuation aC, Term a1) {
			super(aC,a1);
			isFunctionCall= false;
		}
	}
	public class Match extends Continuation {
		// private Continuation c0;
		protected PrologVariable argumentResult;
		protected Term pattern;
		protected boolean isFunctionCall;
		//
		public Match(Continuation aC, Term aP) {
			c0= aC;
			pattern= aP;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			getDatabaseTable(iX).matchRecord(argumentResult,pattern,isFunctionCall,c0,currentProcess,true,iX);
		}
	}
	//
	public class Retract1s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult;
		protected Term inputResult;
		protected boolean hasOutputArgument;
		//
		public Retract1s(Continuation aC, PrologVariable a1) {
			c0= aC;
			outputResult= a1;
			hasOutputArgument= true;
		}
		public Retract1s(Continuation aC, Term a1) {
			c0= aC;
			inputResult= a1;
			hasOutputArgument= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			getDatabaseTable(iX).retractRecord(outputResult,inputResult,hasOutputArgument,c0,currentProcess,true,iX);
		}
	}
	//
	public void retractAll0s(ChoisePoint iX) {
		getDatabaseTable(iX).retractAll(currentProcess,true,iX);
	}
	public void retractAll1s(ChoisePoint iX, Term a1) {
		getDatabaseTable(iX).retractAll(a1,currentProcess,true,iX);
	}
	//
	public void sortBy1s(ChoisePoint iX, Term targetKey) {
		getDatabaseTable(iX).sortBy(targetKey,currentProcess,true,iX);
	}
	//
	public void save0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		getDatabaseTable(iX).saveContent(fileName,requestedCharacterSet,currentProcess,true,iX);
	}
	public void save1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		getDatabaseTable(iX).saveContent(fileName,requestedCharacterSet,currentProcess,true,iX);
	}
	//
	public void load0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		getDatabaseTableContainer(iX).loadContent(fileName,timeout,requestedCharacterSet,staticContext,currentProcess,true,iX);
	}
	public void load1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		getDatabaseTableContainer(iX).loadContent(fileName,timeout,requestedCharacterSet,staticContext,currentProcess,true,iX);
	}
	//
	public void recentLoadingError4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) throws Backtracking {
		getDatabaseTableContainer(iX).recentLoadingError(a1,a2,a3,a4,currentProcess,true,iX);
	}
	public void recentLoadingError3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) throws Backtracking {
		getDatabaseTableContainer(iX).recentLoadingError(a1,a2,a3,currentProcess,true,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void beginTransaction1s(ChoisePoint iX, Term a1) throws Backtracking {
		DatabaseAccessMode accessMode= DatabaseAccessMode.argumentToDatabaseAccessMode(a1,iX);
		TimeInterval waitingPeriod= getTransactionWaitingPeriod(iX);
		TimeInterval sleepPeriod= getTransactionSleepPeriod(iX);
		BigInteger maximalRetryNumber= getTransactionMaximalRetryNumber(iX);
		beginDatabaseTableTransaction(accessMode,waitingPeriod,sleepPeriod,maximalRetryNumber,currentProcess,iX);
	}
	//
	public void endTransaction0s(ChoisePoint iX) {
		boolean watchTable= getWatchUpdates(iX);
		endDatabaseTableTransaction(currentProcess,watchTable,iX);
	}
	//
	public void rollbackTransaction0s(ChoisePoint iX) {
		rollbackDatabaseTableCurrentTransaction(currentProcess,iX);
	}
	//
	public void rollbackTransactionTree0s(ChoisePoint iX) {
		rollbackDatabaseTableTransactionTree(currentProcess,iX);
	}
	//
	public void activate0s(ChoisePoint iX) {
		activateWatcher(currentProcess,iX);
	}
	//
	public void isUpdated0s(ChoisePoint iX) throws Backtracking {
		if (!getDatabaseTableContainer(iX).isUpdated(this)) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void update0s(ChoisePoint iX) {
	}
	//
	public class Update0s extends Continuation {
		// private Continuation c0;
		//
		public Update0s(Continuation aC) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
}
