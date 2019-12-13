// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

import java.util.HashSet;
import java.util.Iterator;

public abstract class ActorNumber extends Term {
	//
	private static final long serialVersionUID= 0xCB0DE9322E0B250EL; // -3815136907582823154L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","ActorNumber");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean thisIsActorNumber() {
		return true;
	}
	@Override
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
