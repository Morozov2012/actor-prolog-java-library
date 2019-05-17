// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.run.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public abstract class ActorPrologSubgoal {
	//
	protected ActorPrologSubgoalTag tag;
	protected int position;
	//
	protected static int anonymousVariableNumber= 0;
	protected static int noVariableNumber= -1;
	//
	public ActorPrologSubgoal(ActorPrologSubgoalTag t, int p) {
		tag= t;
		position= p;
	}
	//
	public ActorPrologSubgoalTag getTag() {
		return tag;
	}
	public int getPosition() {
		return position;
	}
	//
	abstract public void assignPrimaryFunctionVariable(int variableNumber, ParserMasterInterface master, ChoisePoint iX) throws ParserError;
	//
	abstract public Term toActorPrologTerm();
}
