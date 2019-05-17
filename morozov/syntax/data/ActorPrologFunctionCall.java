// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.run.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public class ActorPrologFunctionCall extends ActorPrologSubgoal {
	//
	protected boolean targetParameterIsASlot;
	protected long targetSlotNameCode;
	protected int targetVariableNumber;
	protected ActorPrologAtom simpleAtom;
	protected int functionVariable= noVariableNumber;
	//
	protected Term termPlainSubgoalFunction= null;
	//
	protected static Term termSelf= new PrologSymbol(SymbolCodes.symbolCode_E_self);
	protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	//
	public ActorPrologFunctionCall(
			boolean targetMode,
			long targetSlot,
			int targetVariable,
			ActorPrologAtom atom,
			int p) {
		super(ActorPrologSubgoalTag.FUNCTION_CALL,p);
		targetParameterIsASlot= targetMode;
		targetSlotNameCode= targetSlot;
		targetVariableNumber= targetVariable;
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
	public ActorPrologAtom getSimpleAtom() {
		return simpleAtom;
	}
	public int getPosition() {
		return position;
	}
	//
	public void assignPrimaryFunctionVariable(int variableNumber, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (functionVariable==noVariableNumber) {
			functionVariable= variableNumber;
		} else {
			master.handleError(new FunctionVariableIsAlreadyAssigned(variableNumber,functionVariable,position),iX);
		}
	}
	//
	public Term toActorPrologTerm() {
		if (termPlainSubgoalFunction==null) {
			Term termFunction= new PrologStructure(SymbolCodes.symbolCode_E_function,new Term[]{new PrologInteger(functionVariable)});
			termPlainSubgoalFunction= new PrologStructure(SymbolCodes.symbolCode_E_plain_subgoal,new Term[]{termFunction});
		};
		Term target;
		if (targetParameterIsASlot) {
			target= new PrologSymbol(targetSlotNameCode);
		} else {
			target= new PrologInteger(targetVariableNumber);
		};
		Term[] internalArray= new Term[4];
		internalArray[0]= target;
		internalArray[1]= termPlainSubgoalFunction;
		internalArray[2]= simpleAtom.toActorPrologTerm();
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_subgoal,internalArray);
	}
}
