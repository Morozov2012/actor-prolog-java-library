// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.subgoals.modes;

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
	@Override
	public boolean isFunction() {
		return isFunction;
	}
	@Override
	public boolean isActorSubgoal() {
		return false;
	}
	@Override
	public boolean isTemporaryActor() {
		return false;
	}
	@Override
	public boolean isControlMessage() {
		return false;
	}
	@Override
	public boolean isDeferredMessage() {
		return false;
	}
	@Override
	public boolean isBufferedMessage() {
		return false;
	}
	//
	@Override
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
