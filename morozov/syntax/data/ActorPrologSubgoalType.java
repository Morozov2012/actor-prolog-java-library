// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.terms.*;

public abstract class ActorPrologSubgoalType {
	abstract public boolean isFunction();
	abstract public boolean isActorSubgoal();
	abstract public boolean isTemporaryActor();
	abstract public boolean isControlMessage();
	abstract public boolean isDeferredMessage();
	abstract public boolean isBufferedMessage();
	abstract public Term toActorPrologTerm();
}
