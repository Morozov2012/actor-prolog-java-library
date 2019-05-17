// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

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
	public boolean isFunction() {
		return false;
	}
	public boolean isActorSubgoal() {
		return true;
	}
	public boolean isTemporaryActor() {
		return isTemporaryActor;
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
		if (isTemporaryActor) {
			internalArray[0]= termTemporary;
		} else {
			internalArray[0]= termConsistent;
		};
		return new PrologStructure(SymbolCodes.symbolCode_E_actor_subgoal,internalArray);
	}
}
