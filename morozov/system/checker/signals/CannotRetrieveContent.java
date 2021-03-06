// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.checker.signals;

import morozov.run.*;
import morozov.system.files.*;
import morozov.terms.*;

public final class CannotRetrieveContent extends LightweightException {
	//
	protected Term exceptionName;
	//
	public CannotRetrieveContent(Term e) {
		exceptionName= e;
	}
	public CannotRetrieveContent(Throwable e) {
		exceptionName= SimpleFileName.channelExceptionToName(e);
	}
	//
	public Term getExceptionName() {
		return exceptionName;
	}
	@Override
	public String toString() {
		if (exceptionName != null) {
			return this.getClass().toString() + "(" + exceptionName.toString() + ")";
		} else {
			return this.getClass().toString();
		}
	}
}
