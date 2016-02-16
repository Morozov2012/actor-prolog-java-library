// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.*;

public class TheResetCommand extends AsyncCall {
	public TheResetCommand(long aSignatureNumber, AbstractInternalWorld world, boolean type, boolean useBuffer, Term[] list, boolean argumentMode) {
		super(aSignatureNumber,world,type,useBuffer,list,argumentMode);
	}
	public boolean isTheResetCommand() {
		return true;
	}
}
