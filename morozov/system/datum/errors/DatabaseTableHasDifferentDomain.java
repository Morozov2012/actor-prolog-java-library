// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.domains.*;

public class DatabaseTableHasDifferentDomain extends RuntimeException {
	public PrologDomain existentDomain;
	public PrologDomain requestedDomain;
	public DatabaseTableHasDifferentDomain(PrologDomain domain1, PrologDomain domain2) {
		existentDomain= domain1;
		requestedDomain= domain2;
	}
	public String toString() {
		return this.getClass().toString() + "(" + existentDomain.toString() + "," + requestedDomain.toString() + ")";
	}
}
