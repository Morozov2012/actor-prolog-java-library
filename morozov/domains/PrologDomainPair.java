// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.domains;

public class PrologDomainPair {
	//
	protected PrologDomain firstDomain;
	protected PrologDomain secondDomain;
	//
	public PrologDomainPair(PrologDomain d1, PrologDomain d2) {
		firstDomain= d1;
		secondDomain= d2;
	}
	//
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof PrologDomainPair) ) {
				return false;
			} else {
				PrologDomainPair i= (PrologDomainPair) o;
				if (	(	i.firstDomain.equals(firstDomain) &&
						i.secondDomain.equals(secondDomain)
					) ||
					(	i.firstDomain.equals(secondDomain) &&
						i.secondDomain.equals(firstDomain)
						)) {
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
		return firstDomain.hashCode() + secondDomain.hashCode();
	}
	//
	@Override
	public String toString() {
		return "<" + firstDomain.toString() + ";" + secondDomain.toString() + ">";
	}
}
