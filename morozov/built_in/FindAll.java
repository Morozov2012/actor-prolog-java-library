// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.AbstractCollection;

public abstract class FindAll extends Lambda {
	//
	abstract protected Term getBuiltInSlot_E_mode();
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
		// private Continuation c0;
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
		public void execute(ChoisePoint iX) throws Backtracking {
			collect_items(c0,iX,predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,argumentList);
		}
	}
	//
	private void collect_items(Continuation c0, ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term[] args) throws Backtracking {
		boolean reduceResultList= Converters.termToCollectingMode(getBuiltInSlot_E_mode(),iX);
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
				for(int i= 1; i < args.length; i++) {
					targetArguments[i]= args[i];
				}
			} else {
				targetArguments= new Term[args.length+1];
				targetArguments[0]= result;
				for(int i= 0; i < args.length; i++) {
					targetArguments[i+1]= args[i];
				}
			}
		} else {
			targetArguments= args;
		};
		final AbstractCollection<Term> resultSet;
		if (reduceResultList) {
			resultSet= new TreeSet<Term>(new TermComparator(true));
		} else {
			// final ArrayList<Term> resultSet= new ArrayList<Term>();
			resultSet= new ArrayList<Term>();
		};
		Continuation completion= new Continuation() {
			public void execute(ChoisePoint iX) throws Backtracking {
				Term newResult= result.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
				resultSet.add(newResult);
				throw Backtracking.instance;
			}
			public boolean isPhaseTermination() {
				return false;
			}
			public String toString() {
				return "RememberResult&Backtrack;";
			}
		};
//for (int i=0; i < targetArguments.length; i++) {
//	System.out.printf("%s) targetArguments: %s\n",i,targetArguments[i]);
//}
		Continuation c1= new DomainSwitch(completion,worldDomainSignatureNumber,targetWorld,FindAll.this,targetArguments);
		ChoisePoint newIndex= new ChoisePoint(iX);
		try {
			c1.execute(newIndex);
			throw new FailureProcedureSucceed(); // Never happens
		} catch (Backtracking b) {
			newIndex.freeTrail();
			if (subgoalIsCallOfFunction && clauseIsFunction) {
				Term resultList= PrologEmptyList.instance;
				// ListIterator<Term> resultSetIterator= resultSet.listIterator(resultSet.size());
				// Iterator<Term> resultSetIterator= resultSet.iterator();
				Term[] resultArray= resultSet.toArray(new Term[0]);
				// while (resultSetIterator.hasPrevious()) {
				//	Term currentResult= resultSetIterator.previous();
				//	resultList= new PrologList(currentResult,resultList);
				// };
				// while (resultSetIterator.hasNext()) {
				//	Term currentResult= resultSetIterator.next();
				//	resultList= new PrologList(currentResult,resultList);
				// };
				for (int n=resultArray.length-1; n >= 0; n--) {
					Term currentResult= resultArray[n];
					resultList= new PrologList(currentResult,resultList);
				};
				args[0].unifyWith(resultList,newIndex);
			};
			c0.execute(newIndex);
		}
	}
	//
	public long domainSignatureOfSubgoal_1_InClause_1(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	public long domainSignatureOfSubgoal_1_InClause_3(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	abstract public Term getBuiltInSlot_E_world();
}
