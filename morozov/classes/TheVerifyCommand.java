// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.terms.*;

public class TheVerifyCommand extends AsyncCall {
	public TheVerifyCommand(long aSignatureNumber, AbstractWorld world, boolean type, boolean useBuffer, Term[] list, boolean argumentMode) {
		super(aSignatureNumber,world,type,useBuffer,list,argumentMode);
	}
	public boolean isTheVerifyCommand() {
		return true;
	}
}
