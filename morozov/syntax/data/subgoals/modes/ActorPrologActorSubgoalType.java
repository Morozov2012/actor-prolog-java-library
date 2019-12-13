// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.subgoals.modes;

import target.*;

import morozov.terms.*;

public class ActorPrologActorSubgoalType extends ActorPrologSubgoalType {
	//
	protected boolean isTemporaryActor;
	//
	protected static Term termConsistent= new PrologSymbol(SymbolCodes.symbolCode_E_consistent);
	protected static Term termTemporary= new PrologSymbol(SymbolCodes.symbolCode_E_temporary);
	//
	public ActorPrologActorSubgoalType(boolean givenIsTemporaryActor) {
		isTemporaryActor= givenIsTemporaryActor;
	}
	//
	@Override
	public boolean isFunction() {
		return false;
	}
	@Override
	public boolean isActorSubgoal() {
		return true;
	}
	@Override
	public boolean isTemporaryActor() {
		return isTemporaryActor;
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
		if (isTemporaryActor) {
			internalArray[0]= termTemporary;
		} else {
			internalArray[0]= termConsistent;
		};
		return new PrologStructure(SymbolCodes.symbolCode_E_actor_subgoal,internalArray);
	}
}
