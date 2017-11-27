// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.system.*;
import morozov.terms.*;

public class TokenSymbol extends PrologToken {
	//
	protected int value;
	protected boolean isInQuotes;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenSymbol(int c, boolean flag, int position) {
		super(position);
		value= c;
		isInQuotes= flag;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.SYMBOL;
	}
	public int getSymbolCode() {
		return value;
	}
	public boolean isInQuotes() {
		return isInQuotes;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologSymbol(value),YesNo.boolean2TermYesNo(isInQuotes)};
		return new PrologStructure(SymbolCodes.symbolCode_E_symbol,arguments);
	}
	public String toString() {
		return SymbolNames.retrieveSymbolName(value).toRawString(null);
	}
}
