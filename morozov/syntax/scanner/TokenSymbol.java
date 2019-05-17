// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.system.converters.*;
import morozov.terms.*;

public class TokenSymbol extends PrologToken {
	//
	protected long value;
	protected boolean isInQuotes;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenSymbol(long c, boolean flag, int position) {
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
	public long getSymbolCode(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	public boolean isInQuotes(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return isInQuotes;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologSymbol(value),YesNoConverters.boolean2TermYesNo(isInQuotes)};
		return new PrologStructure(SymbolCodes.symbolCode_E_symbol,arguments);
	}
	public String toString() {
		return SymbolNames.retrieveSymbolName(value).toRawString(null);
	}
}
