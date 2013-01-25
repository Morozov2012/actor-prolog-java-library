// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

public class TokenSymbol extends PrologToken {
	protected int value;
	protected boolean includedIntoApostrophes;
	public TokenSymbol(int c, boolean flag, int position) {
		super(position);
		value= c;
		includedIntoApostrophes= flag;
	}
	public PrologTokenType getType() {
		return PrologTokenType.SYMBOL;
	}
	public int getSymbolCode() {
		return value;
	}
	public boolean isIncludedIntoApostrophes() {
		return includedIntoApostrophes;
	}
	public String toString() {
		return SymbolNames.retrieveSymbolName(value).toString(null);
		// return String.format("%s",value);
	}
}
