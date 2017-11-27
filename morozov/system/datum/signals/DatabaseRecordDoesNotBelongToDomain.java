// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.signals;

import morozov.run.*;
import morozov.terms.*;

public class DatabaseRecordDoesNotBelongToDomain extends LightweightException {
	//
	public Term item;
	public String text;
	public long position;
	//
	public DatabaseRecordDoesNotBelongToDomain(Term i, String t, long p, Throwable e) {
		super(e);
		item= i;
		text= t;
		position= p;
	}
	public DatabaseRecordDoesNotBelongToDomain(Term i, String t, long p) {
		item= i;
		text= t;
		position= p;
	}
}
