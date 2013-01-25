// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.terms.*;

import java.net.URI;

public class ActorAndURI {
	public ActorNumber actorNumber;
	public URI uri;
	public ActorAndURI(ActorNumber a, URI i) {
		actorNumber= a;
		uri= i;
	}
	//
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
	public int hashCode() {
		return actorNumber.hashCode() + uri.hashCode();
	}
	//
	public String toString() {
		return "<" + actorNumber.toString() + ";" + uri.toString() + ">";
	}
}
