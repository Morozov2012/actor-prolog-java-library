// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public class ActorPrologMessageType extends ActorPrologSubgoalType {
	//
	protected boolean isControlMessage;
	protected boolean isDeferredMessage;
	protected boolean isBufferedMessage;
	//
	protected static Term termControl= new PrologSymbol(SymbolCodes.symbolCode_E_control);
	protected static Term termData= new PrologSymbol(SymbolCodes.symbolCode_E_data);
	protected static Term termDeferred= new PrologSymbol(SymbolCodes.symbolCode_E_deferred);
	protected static Term termQuick= new PrologSymbol(SymbolCodes.symbolCode_E_quick);
	protected static Term termBuffered= new PrologSymbol(SymbolCodes.symbolCode_E_buffered);
	protected static Term termSuperseding= new PrologSymbol(SymbolCodes.symbolCode_E_superseding);
	//
	public ActorPrologMessageType(
		boolean givenIsControlMessage,
		boolean givenIsDeferredMessage,
		boolean givenIsBufferedMessage) {
		isControlMessage= givenIsControlMessage;
		isDeferredMessage= givenIsDeferredMessage;
		isBufferedMessage= givenIsBufferedMessage;
	}
	//
	public boolean isFunction() {
		return false;
	}
	public boolean isActorSubgoal() {
		return false;
	}
	public boolean isTemporaryActor() {
		return false;
	}
	public boolean isControlMessage() {
		return isControlMessage;
	}
	public boolean isDeferredMessage() {
		return isDeferredMessage;
	}
	public boolean isBufferedMessage() {
		return isBufferedMessage;
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[3];
		if (isControlMessage) {
			internalArray[0]= termControl;
		} else {
			internalArray[0]= termData;
		};
		if (isDeferredMessage) {
			internalArray[1]= termDeferred;
		} else {
			internalArray[1]= termQuick;
		};
		if (isBufferedMessage) {
			internalArray[2]= termBuffered;
		} else {
			internalArray[2]= termSuperseding;
		};
		return new PrologStructure(SymbolCodes.symbolCode_E_message,internalArray);
	}
}
