// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.terms.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

public abstract class JavaTestbed extends Alpha {
	//
	public JavaTestbed() {
	}
	public JavaTestbed(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	public void unifyTerms(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		System.out.printf("TESTBED(before):: a1=%s\n",a1);
		System.out.printf("TESTBED(before):: a2=%s\n",a2);
		a1.unifyWith(a2,iX);
		System.out.printf("TESTBED(after):: a1=%s\n",a1);
		System.out.printf("TESTBED(after):: a2=%s\n",a2);
	}
	//
	public static void writeOneTerm1s(ChoisePoint iX, Term a1) {
		System.out.printf("Argument1=%s\n",a1);
	}
	public static class WriteOneTerm1s extends Continuation {
		protected Term variable1;
		public WriteOneTerm1s(Continuation aC, Term a1) {
			c0= aC;
			variable1= a1;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s\n",variable1);
			c0.execute(iX);
		}
	}
	//
	public static void writeOneTerm1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		System.out.printf("Argument1=%s, Argument2=%s\n",result,a1);
		result.setNonBacktrackableValue(new PrologInteger(1000));
		// iX.pushTrail(a1);
	}
	public static class WriteOneTerm1ff extends Continuation {
		protected PrologVariable variable1;
		protected Term variable2;
		public WriteOneTerm1ff(Continuation aC, PrologVariable result, Term a1) {
			c0= aC;
			variable1= result;
			variable2= a1;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s\n",variable1,variable2);
			variable1.setNonBacktrackableValue(new PrologInteger(1000));
			c0.execute(iX);
		}
	}
	//
	public static void writeOneTerm1fs(ChoisePoint iX, Term a1) {
		System.out.printf("Argument1=%s\n",a1);
	}
	public static class WriteOneTerm1fs extends Continuation {
		protected Term variable1;
		public WriteOneTerm1fs(Continuation aC, Term a1) {
			c0= aC;
			variable1= a1;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s\n",variable1);
			c0.execute(iX);
		}
	}
	//
	public static void writeTwoTerms2s(ChoisePoint iX, Term a1, Term a2) {
		System.out.printf("Argument1=%s, Argument2=%s\n",a1,a2);
	}
	public static class WriteTwoTerms2s extends Continuation {
		protected Term variable1;
		protected Term variable2;
		public WriteTwoTerms2s(Continuation aC, Term a1, Term a2) {
			c0= aC;
			variable1= a1;
			variable2= a2;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s\n",variable1,variable2);
			c0.execute(iX);
		}
	}
	//
	public static void writeTwoTerms2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		System.out.printf("Argument1=%s, Argument2=%s, Argument3=%s\n",result,a1,a2);
		result.setNonBacktrackableValue(new PrologInteger(1000));
		// iX.pushTrail(a1);
	}
	public static class WriteTwoTerms2ff extends Continuation {
		protected PrologVariable variable1;
		protected Term variable2;
		protected Term variable3;
		public WriteTwoTerms2ff(Continuation aC, PrologVariable a1, Term a2, Term a3) {
			c0= aC;
			variable1= a1;
			variable2= a2;
			variable3= a3;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s, Argument3=%s\n",variable1,variable2,variable3);
			variable1.setNonBacktrackableValue(new PrologInteger(1000));
			c0.execute(iX);
		}
	}
	//
	public static void writeTwoTerms2fs(ChoisePoint iX, Term a1, Term a2) {
		System.out.printf("Argument1=%s, Argument2=%s\n",a1,a2);
	}
	public static class WriteTwoTerms2fs extends Continuation {
		protected Term variable1;
		protected Term variable2;
		public WriteTwoTerms2fs(Continuation aC, Term a1, Term a2) {
			c0= aC;
			variable1= a1;
			variable2= a2;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s\n",variable1,variable2);
			c0.execute(iX);
		}
	}
	//
	public static void writeMetaPredicate(ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term... arguments) {
		System.out.printf("MetaPredicate: predicateSignatureNumber=%s, subgoalIsCallOfFunction:%s, clauseIsFunction:%s, Arguments:%s\n",predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,arguments);
		for (int n=0; n < arguments.length; n++) {
			System.out.printf("Argument %d: %s: %s\n",n+1,arguments[n].getClass(),arguments[n]);
		};
		if (subgoalIsCallOfFunction && clauseIsFunction) {
			((PrologVariable)arguments[0]).setNonBacktrackableValue(new PrologInteger(1000));
		}
	}
	public static class WriteMetaPredicate extends Continuation {
		private long predicateSignatureNumber;
		private boolean subgoalIsCallOfFunction;
		private boolean clauseIsFunction;
		private Term[] argumentList;
		//
		public WriteMetaPredicate(Continuation aC, long aSignature, boolean aSICOF, boolean aCIF, Term... args) {
			c0= aC;
			predicateSignatureNumber= aSignature;
			subgoalIsCallOfFunction= aSICOF;
			clauseIsFunction= aCIF;
			argumentList= (Term[])args;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("MetaPredicate: predicateSignatureNumber=%s, subgoalIsCallOfFunction:%s, clauseIsFunction:%s, Arguments:%s\n",predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,argumentList);
			for (int n=0; n < argumentList.length; n++) {
				System.out.printf("Argument %d: %s: %s\n",n+1,argumentList[n].getClass(),argumentList[n]);
			};
			if (subgoalIsCallOfFunction && clauseIsFunction) {
				((PrologVariable)argumentList[0]).setNonBacktrackableValue(new PrologInteger(1000));
			};
			c0.execute(iX);
		}
	}
	//
	public static void writeMetaAtom(ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term... arguments) {
		System.out.printf("MetaAtom: predicateSignatureNumber=%s, subgoalIsCallOfFunction:%s, clauseIsFunction:%s, Arguments:%s\n",predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,arguments);
		for (int n=0; n < arguments.length; n++) {
			System.out.printf("Argument %d: %s: %s\n",n+1,arguments[n].getClass(),arguments[n]);
		};
		if (subgoalIsCallOfFunction && clauseIsFunction) {
			((PrologVariable)arguments[0]).setNonBacktrackableValue(new PrologInteger(1000));
		}
	}
	public static class WriteMetaAtom extends Continuation {
		private long predicateSignatureNumber;
		private boolean subgoalIsCallOfFunction;
		private boolean clauseIsFunction;
		private Term[] argumentList;
		//
		public WriteMetaAtom(Continuation aC, long aSignature, boolean aSICOF, boolean aCIF, Term... args) {
			c0= aC;
			predicateSignatureNumber= aSignature;
			subgoalIsCallOfFunction= aSICOF;
			clauseIsFunction= aCIF;
			argumentList= (Term[])args;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("MetaAtom: predicateSignatureNumber=%s, subgoalIsCallOfFunction:%s, clauseIsFunction:%s, Arguments:%s\n",predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,argumentList);
			for (int n=0; n < argumentList.length; n++) {
				System.out.printf("Argument %d: %s: %s\n",n+1,argumentList[n].getClass(),argumentList[n]);
			};
			if (subgoalIsCallOfFunction && clauseIsFunction) {
				((PrologVariable)argumentList[0]).setNonBacktrackableValue(new PrologInteger(1000));
			};
			c0.execute(iX);
		}
	}
	//
	public static void returnOneNumber1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologInteger(100),iX);
		//iX.pushTrail(a1);
		// iX.pushTrail(a1);
	}
	public static void returnOneNumber1s(ChoisePoint iX, Term a1) {
		System.out.printf("Argument1=%s\n",a1);
		if (a1 instanceof PrologVariable) {
			((PrologVariable)a1).setBacktrackableValue(new PrologInteger(210),iX);
			//iX.pushTrail(a1);
		}
	}
	public static class ReturnOneNumber1s extends Continuation {
		protected Term variable1;
		public ReturnOneNumber1s(Continuation aC, Term a1) {
			c0= aC;
			variable1= a1;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s\n",variable1);
			if (variable1 instanceof PrologVariable) {
				((PrologVariable)variable1).setBacktrackableValue(new PrologInteger(270),iX);
				//iX.pushTrail(variable1);
			};
			c0.execute(iX);
		}
	}
	//
	public static void returnOneNumber1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) {
		System.out.printf("Argument1=%s, Argument2=%s\n",result,a1);
		result.setNonBacktrackableValue(new PrologInteger(1000));
		a1.setBacktrackableValue(new PrologInteger(100),iX);
		// iX.pushTrail(result);
		//iX.pushTrail(a1);
	}
	public static void returnOneNumber1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		System.out.printf("Argument1=%s, Argument2=%s\n",result,a1);
		result.setNonBacktrackableValue(new PrologInteger(1000));
		// iX.pushTrail(a1);
		if (a1 instanceof PrologVariable) {
			((PrologVariable)a1).setBacktrackableValue(new PrologInteger(210),iX);
			//iX.pushTrail(a1);
		}
	}
	public static class ReturnOneNumber1ff extends Continuation {
		protected PrologVariable variable1;
		protected Term variable2;
		public ReturnOneNumber1ff(Continuation aC, PrologVariable result, Term a1) {
			c0= aC;
			variable1= result;
			variable2= a1;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s\n",variable1,variable2);
			variable1.setNonBacktrackableValue(new PrologInteger(1000));
			if (variable2 instanceof PrologVariable) {
				((PrologVariable)variable2).setBacktrackableValue(new PrologInteger(270),iX);
				//iX.pushTrail(variable2);
			};
			c0.execute(iX);
		}
	}
	//
	public static void returnOneNumber1fs(ChoisePoint iX, PrologVariable a1) {
		System.out.printf("Argument1=%s\n",a1);
		a1.setBacktrackableValue(new PrologInteger(100),iX);
		//iX.pushTrail(a1);
	}
	public static void returnOneNumber1fs(ChoisePoint iX, Term a1) {
		System.out.printf("Argument1=%s\n",a1);
		if (a1 instanceof PrologVariable) {
			((PrologVariable)a1).setBacktrackableValue(new PrologInteger(210),iX);
			//iX.pushTrail(a1);
		}
	}
	public static class ReturnOneNumber1fs extends Continuation {
		protected Term variable1;
		public ReturnOneNumber1fs(Continuation aC, Term a1) {
			c0= aC;
			variable1= a1;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s\n",variable1);
			if (variable1 instanceof PrologVariable) {
				((PrologVariable)variable1).setBacktrackableValue(new PrologInteger(270),iX);
				//iX.pushTrail(variable1);
			};
			c0.execute(iX);
		}
	}
	//
	public static void returnTwoNumbers2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		a1.setBacktrackableValue(new PrologInteger(110),iX);
		a2.setBacktrackableValue(new PrologInteger(120),iX);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	public static void returnTwoNumbers2s(ChoisePoint iX, Term a1, Term a2) {
		System.out.printf("Argument1=%s, Argument2=%s\n",a1,a2);
		if (a1 instanceof PrologVariable) {
			((PrologVariable)a1).setBacktrackableValue(new PrologInteger(210),iX);
			//iX.pushTrail(a1);
		};
		if (a2 instanceof PrologVariable) {
			((PrologVariable)a2).setBacktrackableValue(new PrologInteger(220),iX);
			//iX.pushTrail(a2);
		}
	}
	public static class ReturnTwoNumbers2s extends Continuation {
		protected Term variable1;
		protected Term variable2;
		public ReturnTwoNumbers2s(Continuation aC, Term a1, Term a2) {
			c0= aC;
			variable1= a1;
			variable2= a2;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s\n",variable1,variable2);
			if (variable1 instanceof PrologVariable) {
				((PrologVariable)variable1).setBacktrackableValue(new PrologInteger(210),iX);
				//iX.pushTrail(variable1);
			};
			if (variable2 instanceof PrologVariable) {
				((PrologVariable)variable2).setBacktrackableValue(new PrologInteger(220),iX);
				//iX.pushTrail(variable2);
			};
			c0.execute(iX);
		}
	}
	//
	public static void returnTwoNumbers2ff(ChoisePoint iX, PrologVariable result, PrologVariable a1, PrologVariable a2) {
		System.out.printf("Argument1=%s, Argument2=%s, Argument3=%s\n",result,a1,a2);
		result.setNonBacktrackableValue(new PrologInteger(1000));
		a1.setBacktrackableValue(new PrologInteger(110),iX);
		a2.setBacktrackableValue(new PrologInteger(120),iX);
		// iX.pushTrail(result);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	public static void returnTwoNumbers2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		System.out.printf("Argument1=%s, Argument2=%s, Argument3=%s\n",result,a1,a2);
		result.setNonBacktrackableValue(new PrologInteger(1000));
		// iX.pushTrail(a1);
		if (a1 instanceof PrologVariable) {
			((PrologVariable)a1).setBacktrackableValue(new PrologInteger(210),iX);
			//iX.pushTrail(a1);
		};
		if (a2 instanceof PrologVariable) {
			((PrologVariable)a2).setBacktrackableValue(new PrologInteger(220),iX);
			//iX.pushTrail(a2);
		}
	}
	public static class ReturnTwoNumbers2ff extends Continuation {
		protected PrologVariable variable1;
		protected Term variable2;
		protected Term variable3;
		public ReturnTwoNumbers2ff(Continuation aC, PrologVariable result, Term a1, Term a2) {
			c0= aC;
			variable1= result;
			variable2= a1;
			variable3= a2;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s, Argument3=%s\n",variable1,variable2,variable3);
			variable1.setNonBacktrackableValue(new PrologInteger(1000));
			if (variable2 instanceof PrologVariable) {
				((PrologVariable)variable2).setBacktrackableValue(new PrologInteger(210),iX);
				//iX.pushTrail(variable2);
			};
			if (variable3 instanceof PrologVariable) {
				((PrologVariable)variable3).setBacktrackableValue(new PrologInteger(220),iX);
				//iX.pushTrail(variable3);
			};
			c0.execute(iX);
		}
	}
	//
	public static void returnTwoNumbers2fs(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		System.out.printf("Argument1=%s, Argument2=%s\n",a1,a2);
		a1.setBacktrackableValue(new PrologInteger(110),iX);
		a2.setBacktrackableValue(new PrologInteger(120),iX);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	public static void returnTwoNumbers2fs(ChoisePoint iX, Term a1, Term a2) {
		System.out.printf("Argument1=%s, Argument2=%s\n",a1,a2);
		if (a1 instanceof PrologVariable) {
			((PrologVariable)a1).setBacktrackableValue(new PrologInteger(210),iX);
			//iX.pushTrail(a1);
		};
		if (a2 instanceof PrologVariable) {
			((PrologVariable)a2).setBacktrackableValue(new PrologInteger(220),iX);
			//iX.pushTrail(a2);
		}
	}
	public static class ReturnTwoNumbers2fs extends Continuation {
		protected Term variable1;
		protected Term variable2;
		public ReturnTwoNumbers2fs(Continuation aC, Term a1, Term a2) {
			c0= aC;
			variable1= a1;
			variable2= a2;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("Argument1=%s, Argument2=%s\n",variable1,variable2);
			if (variable1 instanceof PrologVariable) {
				((PrologVariable)variable1).setBacktrackableValue(new PrologInteger(210),iX);
				//iX.pushTrail(variable1);
			};
			if (variable2 instanceof PrologVariable) {
				((PrologVariable)variable2).setBacktrackableValue(new PrologInteger(220),iX);
				//iX.pushTrail(variable2);
			};
			c0.execute(iX);
		}
	}
	//
	public static void askMetaPredicate(ChoisePoint iX, long predicateSignatureNumber, boolean subgoalIsCallOfFunction, boolean clauseIsFunction, Term... arguments) {
		System.out.printf("askMetaPredicate: predicateSignatureNumber=%s, subgoalIsCallOfFunction:%s, clauseIsFunction:%s, Arguments:%s\n",predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,arguments);
		for (int n=0; n < arguments.length; n++) {
			System.out.printf("Argument %d: %s: %s\n",n+1,arguments[n].getClass(),arguments[n]);
			try {
				arguments[n].isInteger(100+n,iX);
			} catch (Backtracking b) {
				System.out.printf("Backtracking %s\n",b);
			}
		}
	}
	public static class AskMetaPredicate extends Continuation {
		private long predicateSignatureNumber;
		private boolean subgoalIsCallOfFunction;
		private boolean clauseIsFunction;
		private Term[] argumentList;
		//
		public AskMetaPredicate(Continuation aC, long aSignature, boolean aSICOF, boolean aCIF, Term... args) {
			c0= aC;
			predicateSignatureNumber= aSignature;
			subgoalIsCallOfFunction= aSICOF;
			clauseIsFunction= aCIF;
			argumentList= (Term[])args;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			System.out.printf("askMetaPredicate: predicateSignatureNumber=%s, subgoalIsCallOfFunction:%s, clauseIsFunction:%s, Arguments:%s\n",predicateSignatureNumber,subgoalIsCallOfFunction,clauseIsFunction,argumentList);
			for (int n=0; n < argumentList.length; n++) {
				System.out.printf("Argument %d: %s: %s\n",n+1,argumentList[n].getClass(),argumentList[n]);
				try {
					argumentList[n].isInteger(100+n,iX);
				} catch (Backtracking b) {
					System.out.printf("Backtracking %s\n",b);
				}
			};
			c0.execute(iX);
		}
	}
	//
	public void getJavaStub1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new ForeignWorldWrapper(null),iX);
		//iX.pushTrail(a1);
	}
}
