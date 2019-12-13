// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;

import java.net.URI;

public class ActorAndURI {
	//
	protected ActorNumber actorNumber;
	protected URI uri;
	//
	public ActorAndURI(ActorNumber a, URI i) {
		actorNumber= a;
		uri= i;
	}
	//
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof ActorAndURI) ) {
				return false;
			} else {
				ActorAndURI i= (ActorAndURI) o;
				if (	i.uri.equals(uri) &&
					i.actorNumber==actorNumber
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	//
	@Override
	public int hashCode() {
		return actorNumber.hashCode() + uri.hashCode();
	}
	//
	@Override
	public String toString() {
		return "<" + actorNumber.toString() + ";" + uri.toString() + ">";
	}
}
