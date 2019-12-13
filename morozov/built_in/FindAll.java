// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.AbstractCollection;

public abstract class FindAll extends Lambda {
	//
	abstract public Term getBuiltInSlot_E_world();
	abstract public Term getBuiltInSlot_E_mode();
	//
	///////////////////////////////////////////////////////////////
	//
	public long domainSignatureOfSubgoal_1_InClause_1(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long domainSignatureOfSubgoal_1_InClause_3(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public FindAll() {
	}
	public FindAll(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void collect(ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term... args) {
		try {
			collect_items(new SuccessTermination(),iX,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,(Term[])args);
		} catch (Backtracking b) {
			throw new ImperativeProcedureFailed();
		}
	}
	//
	public class Collect extends Continuation {
		//
		private long predicateSignatureNumber;
		private boolean subgoalIsCallOfFunction;
		private boolean clauseIsFunction;
		private Term[] argumentList;
		//
		public Collect(Continuation aC, long aSignature, boolean aSICOF, boolean aCIF, Term... args) {
			c0= aC;
			predicateSignatureNumber= aSignature;
			subgoalIsCallOfFunction= aSICOF;
			clauseIsFunction= aCIF;
			argumentList= (Term[])args;
		}
		//
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			collect_items(c0,iX,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,argumentList);
		}
	}
	//
	protected void collect_items(Continuation c0, ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term[] args) throws Backtracking {
		CollectingMode reduceResultList= CollectingModeConverters.argumentToCollectingMode(getBuiltInSlot_E_mode(),iX);
		long worldDomainSignatureNumber;
		if (clauseIsFunction) {
			worldDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_3(predicateSignatureNumber);
		} else {
			worldDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_1(predicateSignatureNumber);
		};
		Term targetWorld= getBuiltInSlot_E_world();
		Term[] targetArguments;
		final PrologVariable result= new PrologVariable();
		if (clauseIsFunction) {
			if (subgoalIsCallOfFunction) {
				targetArguments= new Term[args.length];
				targetArguments[0]= result;
				System.arraycopy(args,1,targetArguments,1,args.length-1);
			} else {
				targetArguments= new Term[args.length+1];
				targetArguments[0]= result;
				System.arraycopy(args,0,targetArguments,1,args.length);
			}
		} else {
			targetArguments= args;
		};
		final AbstractCollection<Term> resultSet;
		if (reduceResultList==CollectingMode.SET) {
			resultSet= new TreeSet<>(new TermComparator(true));
		} else {
			resultSet= new ArrayList<>();
		};
		Continuation completion= new Continuation() {
			@Override
			public void execute(ChoisePoint iX) throws Backtracking {
				Term newResult= result.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
				resultSet.add(newResult);
				throw Backtracking.instance;
			}
			@Override
			public boolean isPhaseTermination() {
				return false;
			}
			@Override
			public String toString() {
				return "RememberResult&Backtrack;";
			}
		};
		Continuation c1= new DomainSwitch(completion,worldDomainSignatureNumber,targetWorld,FindAll.this,targetArguments);
		ChoisePoint newIndex= new ChoisePoint(iX);
		try {
			c1.execute(newIndex);
			throw new FailureProcedureSucceed(); // Never happens
		} catch (Backtracking b) {
			newIndex.freeTrail();
			if (subgoalIsCallOfFunction && clauseIsFunction) {
				Term resultList= PrologEmptyList.instance;
				Term[] resultArray= resultSet.toArray(new Term[resultSet.size()]);
				for (int n=resultArray.length-1; n >= 0; n--) {
					Term currentResult= resultArray[n];
					resultList= new PrologList(currentResult,resultList);
				};
				args[0].unifyWith(resultList,newIndex);
			};
			c0.execute(newIndex);
		}
	}
}
