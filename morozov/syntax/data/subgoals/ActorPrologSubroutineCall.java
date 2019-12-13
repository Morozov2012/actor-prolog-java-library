// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.subgoals;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.data.subgoals.modes.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public class ActorPrologSubroutineCall extends ActorPrologSubgoal {
	//
	protected boolean targetParameterIsASlot;
	protected long targetSlotNameCode;
	protected int targetVariableNumber;
	protected Term targetVariableRoleTerm;
	protected ActorPrologSubgoalType type;
	protected ActorPrologAtom simpleAtom;
	//
	public ActorPrologSubroutineCall(
			boolean targetMode,
			long targetSlot,
			int targetVariable,
			Term targetRoleTerm,
			ActorPrologSubgoalType t,
			ActorPrologAtom atom,
			int p) {
		super(p);
		targetParameterIsASlot= targetMode;
		targetSlotNameCode= targetSlot;
		targetVariableNumber= targetVariable;
		targetVariableRoleTerm= targetRoleTerm;
		type= t;
		simpleAtom= atom;
	}
	//
	public boolean targetParameterIsASlot() {
		return targetParameterIsASlot;
	}
	public long getTargetSlotNameCode() {
		return targetSlotNameCode;
	}
	public int getTargetVariableNumber() {
		return targetVariableNumber;
	}
	public Term getTargetVariableRoleTerm() {
		return targetVariableRoleTerm;
	}
	public ActorPrologSubgoalType getType() {
		return type;
	}
	public ActorPrologAtom getSimpleAtom() {
		return simpleAtom;
	}
	//
	@Override
	public void assignPrimaryFunctionVariable(int variableNumber, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		master.handleError(new SubroutineCallCannotBeFunctionCall(variableNumber,position),iX);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[4];
		if (targetParameterIsASlot) {
			internalArray[0]= new PrologSymbol(targetSlotNameCode);
		} else {
			Term[] termArray= new Term[2];
			termArray[0]= new PrologInteger(targetVariableNumber);
			termArray[1]= targetVariableRoleTerm;
			internalArray[0]= new PrologStructure(SymbolCodes.symbolCode_E_var,termArray);
		};
		internalArray[1]= type.toActorPrologTerm();
		internalArray[2]= simpleAtom.toActorPrologTerm();
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_subgoal,internalArray);
	}
}
