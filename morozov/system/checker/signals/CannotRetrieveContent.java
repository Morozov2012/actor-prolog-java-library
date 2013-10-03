// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.checker.signals;

import morozov.run.*;
import morozov.system.checker.*;
import morozov.terms.*;

public final class CannotRetrieveContent extends LightweightException {
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