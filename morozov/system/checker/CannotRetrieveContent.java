// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.terms.*;

public class CannotRetrieveContent extends LightweightException {
	// public Throwable exception;
	public Term exceptionName;
	public CannotRetrieveContent(Term e) {
		exceptionName= e;
	}
	public CannotRetrieveContent(Throwable e) {
		exceptionName= URL_Utils.exceptionToName(e);
	}
	public Term getExceptionName() {
		return exceptionName;
	}
}
