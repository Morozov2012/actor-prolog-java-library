// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public class ActorPrologPlainSubgoalType extends ActorPrologSubgoalType {
	//
	protected boolean isFunction;
	//
	protected static Term termSubroutine= new PrologSymbol(SymbolCodes.symbolCode_E_subroutine);
	protected static Term termFunction= new PrologSymbol(SymbolCodes.symbolCode_E_function);
	//
	public ActorPrologPlainSubgoalType(boolean givenIsFunction) {
		isFunction= givenIsFunction;
	}
	//
	public boolean isFunction() {
		return isFunction;
	}
	public boolean isActorSubgoal() {
		return false;
	}
	public boolean isTemporaryActor() {
		return false;
	}
	public boolean isControlMessage() {
		return false;
	}
	public boolean isDeferredMessage() {
		return false;
	}
	public boolean isBufferedMessage() {
		return false;
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[1];
		if (isFunction) {
			internalArray[0]= termFunction;
		} else {
			internalArray[0]= termSubroutine;
		};
		return new PrologStructure(SymbolCodes.symbolCode_E_plain_subgoal,internalArray);
	}
}
