// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.run.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.system.converters.*;
import morozov.terms.*;

public class ActorPrologNearFunctionCall extends ActorPrologSubgoal {
	//
	protected long functor;
	protected Term[] arguments;
	protected int functionVariable= noVariableNumber;
	//
	protected Term termPlainSubgoalFunction= null;
	//
	protected static Term termSelf= new PrologSymbol(SymbolCodes.symbolCode_E_self);
	protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	protected static Term termYes= new PrologSymbol(SymbolCodes.symbolCode_E_yes);
	//
	public ActorPrologNearFunctionCall(long f, Term[] array, int p) {
		super(ActorPrologSubgoalTag.FUNCTION_CALL,p);
		functor= f;
		arguments= array;
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
		Term[] atomArray= new Term[5];
		atomArray[0]= new PrologSymbol(functor);
		atomArray[1]= GeneralConverters.arrayToList(arguments);
		atomArray[2]= termNo;
		atomArray[3]= termYes;
		atomArray[4]= new PrologInteger(position);
		Term atom= new PrologStructure(SymbolCodes.symbolCode_E_atom,atomArray);
		Term[] internalArray= new Term[4];
		internalArray[0]= termSelf;
		internalArray[1]= termPlainSubgoalFunction;
		internalArray[2]= atom;
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_subgoal,internalArray);
	}
}
