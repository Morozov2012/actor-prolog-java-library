// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import morozov.run.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public abstract class ActorPrologInitializer {
	//
	protected int position;
	//
	public ActorPrologInitializer(int p) {
		position= p;
	}
	//
	public int getPosition() {
		return position;
	}
	//
	public void checkWhetherThereAreNoArrays(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
	}
	public void checkWhetherThereAreNoSimpleConstructors(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
	}
	//
	abstract public Term toActorPrologTerm();
}
