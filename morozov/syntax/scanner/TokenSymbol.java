// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.terms.signals.*;

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
	@Override
	public PrologTokenType getType() {
		return PrologTokenType.SYMBOL;
	}
	@Override
	public long getSymbolCode(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	@Override
	public boolean isInQuotes(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return isInQuotes;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor != SymbolCodes.symbolCode_E_symbol) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 2) {
				return false;
			};
			argument= list[0];
			long v= argument.getSymbolValue(iX);
			if (value != v) {
				return false;
			};
			argument= list[1];
			long q= argument.getSymbolValue(iX);
			if (isInQuotes && q==SymbolCodes.symbolCode_E_yes) {
				return true;
			} else if (!isInQuotes && q==SymbolCodes.symbolCode_E_no) {
				return true;
			};
			return false;
		} catch (TermIsNotAStructure e) {
			return false;
		} catch (TermIsNotASymbol e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologSymbol(value),YesNoConverters.boolean2TermYesNo(isInQuotes)};
		return new PrologStructure(SymbolCodes.symbolCode_E_symbol,arguments);
	}
	@Override
	public String toString() {
		return SymbolNames.retrieveSymbolName(value).toRawString(null);
	}
}
