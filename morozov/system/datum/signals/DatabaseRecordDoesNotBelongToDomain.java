// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.signals;

import morozov.run.*;
import morozov.terms.*;

public class DatabaseRecordDoesNotBelongToDomain extends LightweightException {
	//
	protected Term item;
	protected String text;
	protected long position;
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
	//
	public Term getItem() {
		return item;
	}
	public String getText() {
		return text;
	}
	public long getPosition() {
		return position;
	}
}
