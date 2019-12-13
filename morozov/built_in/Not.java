// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.run.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class Not extends Lambda {
	//
	public Not() {
	}
	public Not(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_world();
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
	public void negation(ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term... args) throws Backtracking {
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
			System.arraycopy(args,0,targetArguments,1,args.length);
		} else {
			targetArguments= args;
		};
		Continuation c1= new DomainSwitch(new SuccessTermination(),worldDomainSignatureNumber,targetWorld,Not.this,targetArguments);
		ChoisePoint newIndex= new ChoisePoint(iX);
		try {
			c1.execute(newIndex);
		} catch (Backtracking b) {
			newIndex.freeTrail();
			return;
		};
		throw Backtracking.instance;
	}
	//
	public class Negation extends Continuation {
		//
		private long predicateSignatureNumber;
		private boolean subgoalIsCallOfFunction;
		private boolean clauseIsFunction;
		private Term[] argumentList;
		//
		public Negation(Continuation aC, long aSignature, boolean aSICOF, boolean aCIF, Term... args) {
			c0= aC;
			predicateSignatureNumber= aSignature;
			subgoalIsCallOfFunction= aSICOF;
			clauseIsFunction= aCIF;
			argumentList= (Term[])args;
		}
		//
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			long worldDomainSignatureNumber;
			if (clauseIsFunction) {
				worldDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_3(predicateSignatureNumber);
			} else {
				worldDomainSignatureNumber= domainSignatureOfSubgoal_1_InClause_1(predicateSignatureNumber);
			};
			Term targetWorld= getBuiltInSlot_E_world();
			Term[] targetArguments;
			if (!subgoalIsCallOfFunction && clauseIsFunction) {
				targetArguments= new Term[argumentList.length+1];
				targetArguments[0]= new PrologVariable();
				System.arraycopy(argumentList,0,targetArguments,1,argumentList.length);
			} else {
				targetArguments= argumentList;
			};
			Continuation c1= new DomainSwitch(new SuccessTermination(),worldDomainSignatureNumber,targetWorld,Not.this,targetArguments);
			ChoisePoint newIndex= new ChoisePoint(iX);
			try {
				c1.execute(newIndex);
			} catch (Backtracking b) {
				newIndex.freeTrail();
				c0.execute(newIndex);
				return;
			};
			throw Backtracking.instance;
		}
	}
}
