// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.terms.*;

public abstract class Trap extends Lambda {
	//
	static Continuation success= new SuccessTermination();
	//
	public void catchException(ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term... args) throws Backtracking {
		catch_exception(success,iX,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,(Term[])args);
	}
	//
	public class CatchException extends Continuation {
		// private Continuation c0;
		private long predicateSignatureNumber;
		private boolean subgoalIsCallOfFunction;
		private boolean clauseIsFunction;
		private Term[] argumentList;
		//
		public CatchException(Continuation aC, long aSignature, boolean aSICOF, boolean aCIF, Term... args) {
			c0= aC;
			predicateSignatureNumber= aSignature;
			subgoalIsCallOfFunction= aSICOF;
			clauseIsFunction= aCIF;
			argumentList= (Term[])args;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			catch_exception(c0,iX,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,argumentList);
		}
	}
	//
	private void catch_exception(Continuation c0, ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term[] args) throws Backtracking {
		long worldDomainSignatureNumber;
		if (clauseIsFunction) {
			worldDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_3(predicateSignatureNumber);
		} else {
			worldDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_1(predicateSignatureNumber);
		};
		Term targetWorld= getBuiltInSlot_E_world();
		Term[] targetArguments;
		if (!subgoalIsCallOfFunction && clauseIsFunction) {
			targetArguments= new Term[args.length+1];
			targetArguments[0]= new PrologVariable();
			for(int i= 0; i < args.length; i++) {
				targetArguments[i+1]= args[i];
			}
		} else {
			targetArguments= (Term[])args;
		};
		Continuation c1= new DomainSwitch(c0,worldDomainSignatureNumber,targetWorld,Trap.this,targetArguments);
		ChoisePoint newIndex= new ChoisePoint(iX);
		try {
			c1.execute(newIndex);
		} catch (Backtracking b) {
			throw b;
		} catch (ProcessedErrorExit e1) {
			if (c0.containsNode(e1.continuation)) {
				throw e1;
			} else {
				processException(e1.processedException,c0,newIndex,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,args);
			}
		} catch (ErrorExit e1) {
			processException(e1,c0,newIndex,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,args);
		} catch (Throwable e1) {
			processException(new LowLevelErrorExit(newIndex,e1),c0,newIndex,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,args);
		}
	}
	protected void processException(ErrorExit ee, Continuation c0, ChoisePoint newIndex, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term[] args) throws Backtracking {
		Term targetWorld= getBuiltInSlot_E_world();
		newIndex.freeTrail();
		try {
			long handlerDomainSignatureNumber;
			long predicateNameCode;
			if (clauseIsFunction) {
				handlerDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_4(predicateSignatureNumber);
				predicateNameCode= headingNameOfClause_4(predicateSignatureNumber);
			} else {
				handlerDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_2(predicateSignatureNumber);
				predicateNameCode= headingNameOfClause_2(predicateSignatureNumber);
			};
			Term handlerWorld= getBuiltInSlot_E_handler();
			Term[] alarmArguments= new Term[2+args.length];
			alarmArguments[0]= ee.createTerm();
			// int predicateCode= SymbolTable.retrieveSymbolCode(predicateName);
			alarmArguments[1]= new PrologSymbol(predicateNameCode);
			if (subgoalIsCallOfFunction) {
				for(int i= 1; i < args.length; i++) {
					 alarmArguments[i+1]= args[i];
				}
			} else {
				for(int i= 0; i < args.length; i++) {
					 alarmArguments[i+2]= args[i];
				}
			};
			Continuation c2= new DomainSwitch(success,handlerDomainSignatureNumber,targetWorld,Trap.this,alarmArguments);
			c2.execute(newIndex);
			// newIndex.freeTrail();
			// throw new Backtracking();
		} catch (Backtracking b) {
			// newIndex.freeTrail();
			throw new ProcessedErrorExit(ee,c0);
			// throw ee;
			// throw new Backtracking();
		} catch (ErrorExit e2) {
			throw new ProcessedErrorExit(e2,c0);
		} catch (Throwable e2) {
			throw new ProcessedErrorExit(new LowLevelErrorExit(newIndex,e2),c0);
		};
		throw new Backtracking();
	}
	//
	public long domainSignatureOfSubgoal_1_InClause_1(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long domainSignatureOfSubgoal_1_InClause_2(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long domainSignatureOfSubgoal_1_InClause_3(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long domainSignatureOfSubgoal_1_InClause_4(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long headingNameOfClause_1(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long headingNameOfClause_2(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long headingNameOfClause_3(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long headingNameOfClause_4(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	//
	abstract public Term getBuiltInSlot_E_world();
	abstract public Term getBuiltInSlot_E_handler();
}
