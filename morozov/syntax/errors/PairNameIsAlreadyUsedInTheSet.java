// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

import target.*;

import morozov.run.*;

public class PairNameIsAlreadyUsedInTheSet extends ParserError {
	//
	protected long integerKey;
	protected boolean hasSymbolicName= false;
	//
	public PairNameIsAlreadyUsedInTheSet(long key, boolean isSymbol, int position) {
		super(position);
		integerKey= key;
		hasSymbolicName= isSymbol;
	}
	//
	public long getIntegerKey() {
		return integerKey;
	}
	public boolean hasSymbolicName() {
		return hasSymbolicName;
	}
	//
	@Override
	public String toString() {
		if (hasSymbolicName) {
			SymbolName name= SymbolNames.retrieveSymbolName(integerKey);
			return this.getClass().toString() + "(name: " + name.toRawString(null) + "; position:" + Integer.toString(position) + ")";
		} else {
			return this.getClass().toString() + "(name: " + Long.toString(integerKey) + "; position:" + Integer.toString(position) + ")";
		}
	}
}
