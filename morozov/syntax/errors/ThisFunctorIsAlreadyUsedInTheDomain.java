// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

import target.*;

import morozov.run.*;

public class ThisFunctorIsAlreadyUsedInTheDomain extends ParserError {
	//
	protected long nameCode;
	//
	public ThisFunctorIsAlreadyUsedInTheDomain(long n, int position) {
		super(position);
		nameCode= n;
	}
	//
	public long getNameCode() {
		return nameCode;
	}
	//
	@Override
	public String toString() {
		SymbolName name= SymbolNames.retrieveSymbolName(nameCode);
		return this.getClass().toString() + "(name: " + name.toRawString(null) + "; position:" + Integer.toString(position) + ")";
	}
}
