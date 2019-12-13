// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import target.*;

public class WrongArgumentIsUnknownAttribute extends RuntimeException {
	//
	protected long name;
	//
	public WrongArgumentIsUnknownAttribute(long code) {
		name= code;
	}
	//
	@Override
	public String toString() {
		return	this.getClass().toString() +
			"(" +
			(name < 0 ?
				SymbolNames.retrieveSymbolName(-name).toString() :
				String.format("%d",name) ) +
			")";
	}
}
