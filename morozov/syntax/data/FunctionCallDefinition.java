// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.system.converters.*;
import morozov.terms.*;

public class FunctionCallDefinition {
	//
	protected ActorPrologSubgoal subgoal;
	protected int variableNumber;
	protected boolean isClauseHeadingElement;
	protected int position;
	//
	protected boolean isNested= false;
	protected boolean isImplemented= false;
	//
	public FunctionCallDefinition(ActorPrologSubgoal s, int vN, boolean iH, int p) {
		subgoal= s;
		variableNumber= vN;
		isClauseHeadingElement= iH;
		position= p;
	}
	//
	public ActorPrologSubgoal getSubgoal() {
		return subgoal;
	}
	//
	public int getVariableNumber() {
		return variableNumber;
	}
	//
	public boolean isClauseHeadingElement() {
		return isClauseHeadingElement;
	}
	//
	public int getPosition() {
		return position;
	}
	//
	public void setIsNested(boolean v) {
		isNested= v;
	}
	//
	public boolean isNested() {
		return isNested;
	}
	//
	public void setIsImplemented(boolean v) {
		isImplemented= v;
	}
	//
	public boolean isImplemented() {
		return isImplemented;
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[4];
		internalArray[0]= new PrologInteger(variableNumber);
		internalArray[1]= subgoal.toActorPrologTerm();
		internalArray[2]= YesNoConverters.boolean2TermYesNo(isNested());
		internalArray[3]= YesNoConverters.boolean2TermYesNo(isClauseHeadingElement());
		return new PrologStructure(SymbolCodes.symbolCode_E_function_call,internalArray);
	}
}
