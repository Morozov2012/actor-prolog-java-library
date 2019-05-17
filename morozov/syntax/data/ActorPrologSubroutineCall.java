// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.run.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public class ActorPrologSubroutineCall extends ActorPrologSubgoal {
	//
	protected boolean targetParameterIsASlot;
	protected long targetSlotNameCode;
	protected int targetVariableNumber;
	protected ActorPrologSubgoalType type;
	protected ActorPrologAtom simpleAtom;
	//
	// protected ActorPrologPlainSubgoalType typePlainSubroutine= new ActorPrologPlainSubgoalType(false);
	//
	public ActorPrologSubroutineCall(
			boolean targetMode,
			long targetSlot,
			int targetVariable,
			ActorPrologSubgoalType t,
			ActorPrologAtom atom,
			int p) {
		super(ActorPrologSubgoalTag.SUBROUTINE_CALL,p);
		targetParameterIsASlot= targetMode;
		targetSlotNameCode= targetSlot;
		targetVariableNumber= targetVariable;
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
	public ActorPrologSubgoalType getType() {
		return type;
	}
	public ActorPrologAtom getSimpleAtom() {
		return simpleAtom;
	}
	public int getPosition() {
		return position;
	}
	//
	public void assignPrimaryFunctionVariable(int variableNumber, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		master.handleError(new SubroutineCallCannotBeFunctionCall(variableNumber,position),iX);
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[4];
		if (targetParameterIsASlot) {
			internalArray[0]= new PrologSymbol(targetSlotNameCode);
		} else {
			internalArray[0]= new PrologInteger(targetVariableNumber);
		};
		internalArray[1]= type.toActorPrologTerm();
		internalArray[2]= simpleAtom.toActorPrologTerm();
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_subgoal,internalArray);
	}
}
