// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.signals;

import morozov.run.*;
import morozov.terms.*;

public class UnknownDatabaseRecord extends LightweightException {
	//
	protected Term item;
	protected String text;
	protected long position;
	//
	public UnknownDatabaseRecord(Term i, String t, long p) {
		item= i;
		text= t;
		position= p;
	}
}
