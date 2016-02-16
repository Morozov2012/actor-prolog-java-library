// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

import java.util.HashSet;
import java.util.Iterator;

public abstract class ActorNumber extends Term {
	public boolean thisIsActorNumber() {
		return true;
	}
	public int hashCode() {
		return System.identityHashCode(this);
	}
	abstract public boolean isNumberOfTemporaryActor();
	//
	public static String toCompactString(HashSet<ActorNumber> actors) {
		StringBuilder buffer= new StringBuilder("[");
		boolean isFirstElement= true;
		Iterator<ActorNumber> actorsIterator= actors.iterator();
		while (actorsIterator.hasNext()) {
			ActorNumber actor= actorsIterator.next();
			if (!isFirstElement) {
				buffer.append(",");
			}
			if (actor.isNumberOfTemporaryActor()) {
				buffer.append("{TempActor}");
			}
			else {
				buffer.append(actor.toString());
			}
			isFirstElement= false;
		}
		buffer.append("]");
		return buffer.toString();
	}
}
